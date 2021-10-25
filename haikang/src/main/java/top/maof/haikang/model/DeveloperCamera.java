package top.maof.haikang.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("开发者-摄像头 Model类")
public class DeveloperCamera {
    private Integer id;
    private Integer developerId;
    private Integer cameraId;
    private Developer developer;
    private Camera camera;
}
