package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import top.maof.haikang.model.Role;


@Mapper
public interface RoleMapper {

    @Insert("insert into t_role(id, level, synopsis) values(#{level}, #{synopsis})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Role role);


}