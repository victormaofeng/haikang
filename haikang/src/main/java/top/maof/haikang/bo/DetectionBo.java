package top.maof.haikang.bo;

import lombok.Data;
import top.maof.haikang.model.Detection;
import top.maof.haikang.model.User;

@Data
public class DetectionBo {

    private Detection detection;

    private User user;

}
