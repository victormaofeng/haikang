package top.maof.haikang.vo;

import lombok.Data;
import top.maof.haikang.model.Detection;

import java.util.List;

/**
 * 目标检测vo
 * 用户一次检测会有一个或多个文件上传服务器并指定目标检测算法来进行目标检测
 *
 * @author 毛逢
 * @date 2021/9/17 20:42
 */
@Data
public class DetectionVo {

    private Detection detection;

    private List<DetectFileVo> detectFileVos;

}
