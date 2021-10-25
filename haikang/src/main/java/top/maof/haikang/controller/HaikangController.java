package top.maof.haikang.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.maof.haikang.api.HaikangApi;
import top.maof.haikang.model.Developer;
import top.maof.haikang.model.DeveloperCamera;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.DeveloperCameraService;
import top.maof.haikang.service.DeveloperService;

/**
 * 海康接口
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 */

@Controller
@ResponseBody
@Slf4j
@Api("海康接口")
@RequestMapping("/haikang")
public class HaikangController {
    @Autowired
    HaikangApi haikangApi;

    @Autowired
    DeveloperCameraService developerCameraService;

    @Autowired
    DeveloperService developerService;

    @ApiOperation("根据摄像头id获取对应的摄像头数据和开发者账号信息")
    @RequestMapping(value = "/{cameraId}", method = RequestMethod.GET)
    public Result<DeveloperCamera> get(@PathVariable("cameraId") @ApiParam("摄像头id") int cameraId) {
        DeveloperCamera developerCamera = developerCameraService.getByCameraId(cameraId);
        // 没有对应id的摄像头,返回请求参数错误
        if (developerCamera == null) return Result.response_400();
        Developer developer = developerCamera.getDeveloper();
        // access_token 没有或失效,重新请求萤石获取并保存
        if (developer.getAccessToken() == null || developer.getExpireTime() < System.currentTimeMillis()) {
            developer = haikangApi.getAccessToken(developer.getAppKey(), developer.getSecret());
            developer.setId(developerCamera.getDeveloperId());
            developerService.update(developer);
        }
        return Result.success(developerCamera);
    }
}
