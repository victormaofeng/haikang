package top.maof.haikang.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.maof.haikang.result.Result;

/**
 * 异常统一处理类
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/22 8:30
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.response(500, e.getMessage());
    }
}
