package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.Detection;
import top.maof.haikang.vo.DetectDetailVo;

import java.util.List;

@Mapper
public interface DetectionMapper {

    @Insert("insert into t_detection(id,title,content,user_id,insert_time,update_time,path,type,is_detect) " +
            "values(null,#{title},#{content},#{userId},#{insertTime},#{updateTime},#{path},#{type},#{isDetect})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Detection detection);

    @Update("update t_detection set title=#{title},content=#{content},insert_time=#{insertTime},update_time=#{updateTime}," +
            "path=#{path},type=#{type},is_detect=#{isDetect}" +
            " where id = #{id}")
    int update(Detection detection);

    @Select("select id,title,content,insert_time,update_time,path,type,is_detect from t_detection " +
            "where id = #{id}")
    Detection select(int id);

    @Select("select id,title,content,insert_time,update_time,path,type,is_detect from t_detection " +
            "where user_id = #{userId}")
    List<Detection> selectsByUser(int userId);


    @Select("select count(id) from t_detection " +
            "where user_id = #{userId} and is_detect = #{isDetect}")
    int selectsCountByUserAndType(int userId, boolean isDetect);


    @Select("select id,title,content,insert_time,update_time,path,type,is_detect from t_detection " +
            "where user_id = #{userId} and is_detect = #{isDetect} ORDER BY id DESC " +
            "limit #{start},#{len} ")
    List<Detection> selectsByUserAndType(int userId,boolean isDetect, int start, int len);


    @Select("select count(id) from t_detection " +
            "where user_id = #{userId}")
    int selectsCountByUser(int userId);

    @Select("select id,title,content,insert_time,update_time,path,type,is_detect from t_detection " +
            "where user_id = #{userId} " +
            "limit #{start},#{len}")
    List<Detection> selectsByUserPage(int userId, int start, int len);

    @Delete("delete from t_detection where id=#{id}")
    int delete(int id);

    @Select("select count(id) from v_detect_reslut " +
            "where user_id = #{userId} and is_detect = #{isDetect} and id = #{id}")
    int selectDetailCountByUserAndType(int userId, boolean isDetect,int id);


    @Select("select id,title,content,user_id,is_detect,path2,eid,type,path1,insertTime,path,insertTime1  from v_detect_reslut " +
            "where user_id = #{userId} and is_detect = #{isDetect} and id = #{id} " +
            "limit #{start},#{len}")
    List<DetectDetailVo> selectDetailByUserAndType(int userId, boolean isDetect, int start, int len, int id);

}
