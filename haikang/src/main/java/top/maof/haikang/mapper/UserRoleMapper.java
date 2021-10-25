package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.maof.haikang.model.UserRole;


@Mapper
public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}