package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.Developer;

@Mapper
public interface DeveloperMapper {

    @Insert("insert into t_developer(id, app_key, secret, access_token, expire_time) " +
            "values (#{appKey}, #{secret}, #{accessToken}, #{expireTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Developer developer);

    int update(Developer developer);

    @Select("select id, app_key, secret, access_token, expire_time from t_developer where id=#{id}")
    Developer getDeveloperById(int id);

    @Delete("delete from t_developer where id=#{id}")
    int deleteById(int id);
}
