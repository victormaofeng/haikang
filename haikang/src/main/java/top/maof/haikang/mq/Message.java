package top.maof.haikang.mq;

import lombok.Data;
import top.maof.haikang.model.Algorithm;

@Data
@Deprecated
public class Message {
    // 使用算法
    public Algorithm algorithm;
}
