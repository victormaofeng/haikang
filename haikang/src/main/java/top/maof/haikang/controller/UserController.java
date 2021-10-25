package top.maof.haikang.controller;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.maof.haikang.model.User;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.UserService;
import top.maof.haikang.utils.JWTUtil;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    @ResponseBody
    public Result get(String token) {
        int userId = JWTUtil.getUserId(token);
        User user = userService.get(userId);
        user.setPassword(null);
        return Result.success(user);
    }


    @GetMapping("/updatePwd")
    @ResponseBody
    public Result updatePwd(String token, String password, String newPassword) {
        int userId = JWTUtil.getUserId(token);
        User user = userService.get(userId);

        if (user != null) {
            Md5Hash md5Hash = new Md5Hash(password, user.getSalt(), 8);
            password = md5Hash.toHex();
            // 密码正确,更换新密码
            if (user.getPassword().equals(password)) {
                Md5Hash md5Hash1 = new Md5Hash(newPassword, user.getSalt(), 8);
                newPassword = md5Hash1.toHex();
                int i = userService.updatePwd(userId, newPassword);
                if (i > 0) {
                    return Result.success();
                }
            }
        }
        return Result.response(400, "用户名或密码错误");
    }


}
