package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.DetectFile;


@Mapper
public interface DetectFileMapper {

    @Insert("insert into t_detect_file(path, type, detection_id, insert_time, update_time) values(#{path}, #{type}, #{detectionId}" +
            ", NOW(), NOW())")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(DetectFile detectFile);

    @Update("update t_detect_file set path=#{path}, type=#{type}, detection_id=#{detectionId}, " +
            "insert_time=#{insertTime}, update_time=NOW()")
    int update(DetectFile detectFile);

    @Select("select id, path, type, detection_id, insert_time, update_time from t_detect_file where id=#{id}")
    DetectFile getDetectFileById(int id);

    @Delete("delete from t_detect_file where id=#{id}")
    int delete(int id);

}
