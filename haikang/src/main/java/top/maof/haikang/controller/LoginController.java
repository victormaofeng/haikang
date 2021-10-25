package top.maof.haikang.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import top.maof.haikang.model.User;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.UserService;
import top.maof.haikang.utils.JWTUtil;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/in", method = {RequestMethod.GET})
    @ApiOperation("登录")
    @ApiResponses({@ApiResponse(response = Result.class, code = 400, message = "用户名或密码错误"),
            @ApiResponse(response = Result.class, code = 200, message = "登录成功")})
    @ResponseBody
    public Result login(@ApiParam(name = "username", required = true, value = "用户名") String username,
                        @ApiParam(name = "password", required = true, value = "密码") String password,
                        @ApiIgnore HttpSession session) {

        log.info(username + "----" + password);
        User user = userService.selectByUsername(username);

        if (user != null) {
            Md5Hash md5Hash = new Md5Hash(password, user.getSalt(), 8);
            password = md5Hash.toHex();
            if (user.getPassword().equals(password)) {
                Map map = new HashMap<String, String>();
                map.put("id", user.getId() + "");
                String token = JWTUtil.getToken(map);
                return Result.success(token);
            }
        }
        return Result.response(400, "用户名或密码错误");
    }
}
