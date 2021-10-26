package top.maof.haikang.model;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import top.maof.haikang.vo.UserVO;

import java.util.Date;

@Data
public class Algorithm {
    private int id;

    // 算法名称
    private String name;

    // 算法介绍
    private String synopsis;

    // 谁写的算法
    private int userId;

    private Date insertTime;

    private Date updateTime;

    @Transient
    private UserVO userVO;

}
