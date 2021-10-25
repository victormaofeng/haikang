package top.maof.haikang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 测试
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/24 15:36
 */
@Controller
@ApiIgnore
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "123";
    }
}
