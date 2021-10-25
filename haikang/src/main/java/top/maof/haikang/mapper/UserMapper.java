package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.maof.haikang.model.User;

@Mapper
public interface UserMapper {
    // 查询语句避免用*
    @Select("select id,username,password,nickname,salt,email,phone_number from t_user where id = #{id}")
    User get(Integer id);

    User getWithRole(Integer id);

    User getWithRolePerm(Integer id);

    @Select("select id,username,password,nickname,salt,email,phone_number from t_user where username = #{username}")
    User selectByUsername(String username);

    @Update("update t_user set password=#{password} where id=#{id}")
    int updatePwd(int id, String password);
}