package top.maof.haikang.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 毛逢
 * @version 1.0
 * @description: 萤石开发者
 * @email 3286408344@qq.com
 * @date 2021/5/19 10:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("开发者 model类")
public class Developer {

    @ApiModelProperty("主键")
    private Integer id;

    // 海康萤石个人开发者账号
    @ApiModelProperty("开发者appKey")
    private String appKey;

    // 海康萤石个人开发者密钥
    @ApiModelProperty("开发者appSecret")
    private String secret;

    // 海康萤石个人开发者令牌
    @ApiModelProperty("开发者accessToken令牌")
    private String accessToken;

    // 海康萤石个人开发者令牌过期时间
    @ApiModelProperty("开发者accessToken令牌过期时间")
    private long expireTime;
}
