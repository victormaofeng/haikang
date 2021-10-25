package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.maof.haikang.model.Perm;


@Mapper
public interface PermMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Perm record);

    int insertSelective(Perm record);

    Perm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Perm record);

    int updateByPrimaryKey(Perm record);
}