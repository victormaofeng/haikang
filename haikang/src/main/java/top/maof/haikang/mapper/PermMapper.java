package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.*;
import top.maof.haikang.model.Perm;


@Mapper
public interface PermMapper {

    @Delete("delete from t_perm where id=#{id}")
    int deleteByPrimaryKey(Integer id);

    @Insert("insert into t_perm(id, permission, synopsis) values(#{permission}, #{synopsis})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Perm record);

    @Insert("insert into t_perm(id, permission, synopsis) values(#{permission}, #{synopsis})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insertSelective(Perm record);

    @Select("select id, permission, synopsis from t_perm where id=#{id}")
    Perm selectByPrimaryKey(Integer id);

    @Update("update t_perm set permission=#{permission}, synopsis=#{synopsis}")
    int updateByPrimaryKeySelective(Perm record);

    @Update("update t_perm set permission=#{permission}, synopsis=#{synopsis}")
    int updateByPrimaryKey(Perm record);
}