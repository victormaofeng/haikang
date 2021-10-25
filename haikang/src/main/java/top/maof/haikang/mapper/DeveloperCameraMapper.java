package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.maof.haikang.model.DeveloperCamera;

import java.util.List;

@Mapper
public interface DeveloperCameraMapper {
    List<DeveloperCamera> getsByCameraId(Integer cameraId);
}
