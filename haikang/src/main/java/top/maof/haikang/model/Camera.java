package top.maof.haikang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 毛逢
 * @version 1.0
 * @description: 海康摄像头
 * @email 3286408344@qq.com
 * @date 2021/5/19 10:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("摄像头 Model类")
public class Camera {
    @ApiModelProperty("主键")
    private Integer id;

    // 设备局域网ip
    @ApiModelProperty("摄像头所在局域网ip")
    private String localIp;

    // 设备局域网端口
    @ApiModelProperty("摄像头端口号")
    private int port;

    // 设备用户名(可以通过ie浏览器登录操作局域网内的设备)
    @ApiModelProperty("摄像头登录用户名")
    private String username;

    // 设备密码(可以通过ie浏览器登录操作局域网内的设备),注意 此处密码存储可不加密
    @ApiModelProperty("摄像头登录密码")
    @JsonIgnore
    private String password;

    // 序列号
    @ApiModelProperty("摄像头序列号")
    private String serialNumber;

    // 型号
    @ApiModelProperty("摄像头序型号")
    private String type;

}
