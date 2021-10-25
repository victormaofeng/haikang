package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.maof.haikang.model.Developer;

@Mapper
public interface DeveloperMapper {
    int update(Developer developer);
}
