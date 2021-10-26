package top.maof.haikang.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import top.maof.haikang.model.Algorithm;

import java.util.List;

@Mapper
public interface AlgorithmMapper {

    @Insert("insert into t_algorithm (id, name, synopsis, user_id, insert_time, update_time) " +
            "values(name, synopsis, user_id, NOW(), NOW())")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Algorithm algorithm);

    List<Algorithm> getAlgorithmsByUserId(@Param("userId") int userId);

    List<Algorithm> getAlgorithms();

}
