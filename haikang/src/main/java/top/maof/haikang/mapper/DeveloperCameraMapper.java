package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.DeveloperCamera;

import java.util.List;

@Mapper
public interface DeveloperCameraMapper {

    @Insert("insert into t_developer_camera(id, developer_id, camera_id) values (#{id}, #{developerId}, #{cameraId})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(DeveloperCamera developerCamera);

    List<DeveloperCamera> getsByCameraId(Integer cameraId);

    @Select("select id, developer_id, camera_id from t_developer_camera where id=#{id}")
    DeveloperCamera getDeveloperCameraById(int id);

    @Delete("delete from t_developer_camera where id=#{id}")
    int deleteById(int id);

    @Update("update t_developer_camera set developer_id=#{developerId}, camera_id=#{cameraId} where id=#{id}")
    int update(DeveloperCamera developerCamera);
}
