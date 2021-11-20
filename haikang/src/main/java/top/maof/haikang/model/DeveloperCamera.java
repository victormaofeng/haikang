package top.maof.haikang.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("开发者-摄像头 Model类")
public class DeveloperCamera {
    private Integer id;
    private Integer developerId;
    private Integer cameraId;
    @Transient
    private Developer developer;

    @Transient
    private Camera camera;
}
