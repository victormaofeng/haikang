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

    /** 算法的类型, 见{@link top.maof.haikang.enums.AlgorithmType} */
    private int type;

    private Date insertTime;

    private Date updateTime;

    @Transient
    private UserVO userVO;

}
