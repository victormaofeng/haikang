package top.maof.haikang.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 消息统一格式
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("后台统一响应类")
public class Result<T> {
    // 状态码
    @ApiModelProperty(value = "状态码", example = "200")
    private Integer status;

    // 消息
    @ApiModelProperty(value = "消息", example = "请求成功")
    private String message;

    // 时间戳
    @ApiModelProperty(value = "时间戳", example = "2021-05-31 15-19-40")
    @JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss")
    private Date timestamp = new Date();

    // 数据
    private T data;

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <E> Result response(Integer status, String message, E data) {
        return new Result<E>(status, message, data);
    }

    public static Result response(Integer status, String message) {
        return new Result(status, message);
    }

    public static <E> Result success(E data) {
        return new Result<E>(200, "成功", data);
    }

    public static Result success() {
        return new Result(200, "成功");
    }

    public static Result response_400() {
        return new Result(400, "请求参数不符合服务器要求");
    }

    public static Result response_401() {
        return new Result(401, "登录过期或者未登录");
    }

    public static Result response_404() {
        return new Result(404, "文件或记录不存在");
    }

    public static Result response_403() {
        return new Result(403, "服务器拒绝执行当前请求");
    }

    public static Result response_423() {
        return new Result(423, "当前资源被锁定");
    }

    public static Result response_500() {
        return new Result(500, "服务器错误");
    }

    public static Result response_501() {
        return new Result(501, "服务器不支持当前请求");
    }
}
