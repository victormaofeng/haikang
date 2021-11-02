package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.DetectedFile;
import top.maof.haikang.mq.Message;

import java.util.List;

@Mapper
public interface DetectedFileMapper {
    @Select("select id, path, type, detect_file_id, algo_id, insert_time, update_time from t_detected_file where id = #{id}")
    DetectedFile getDetectedFileById(int id);

    @Select("select id, path, type, detect_file_id, algo_id, insert_time, update_time from t_detected_file " +
            "where detect_file_id = #{detectFileId}")
    List<DetectedFile> getsByDetectFile(int detectFileId);

    @Select("select id, path, type, detect_file_id, algo_id, insert_time, update_time from t_detected_file " +
            "limit #{start},#{pageSize}")
    List<DetectedFile> gets(int start, int pageSize);


    @Update("update t_detected_file set path=#{path}, type=#{type}, detect_file_id=#{detectFileId}, algo_id=#{algoId}, " +
            "insert_time=#{insertTime}, update_time=NOW()")
    int update(DetectedFile detectedFile);

    @Insert("insert into t_detected_file(path, type, detect_file_id, algo_id, insert_time, update_time) " +
            "values(#{path}, #{type}, #{detectFileId}, #{algoId}, NOW(), NOW())")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(DetectedFile detectedFile);

    @Delete("delete from t_detected_file where id=#{id}")
    int delete(int id);
    //消息传给前台的数据
    @Select("select * from v_detection_detected where id = #{id}")
    Message getMessage(int id);
}
