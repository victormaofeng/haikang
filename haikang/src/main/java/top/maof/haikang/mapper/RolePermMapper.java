package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.maof.haikang.model.RolePerm;

@Mapper
public interface RolePermMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePerm record);

    int insertSelective(RolePerm record);

    RolePerm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePerm record);

    int updateByPrimaryKey(RolePerm record);
}