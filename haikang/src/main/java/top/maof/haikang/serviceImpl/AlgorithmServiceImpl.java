package top.maof.haikang.serviceImpl;

import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.AlgorithmMapper;
import top.maof.haikang.model.Algorithm;
import top.maof.haikang.service.AlgorithmService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AlgorithmServiceImpl
 * @Description TODO
 * @Author Quentin_zyj
 * @Date 2021/10/26 16:29
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    @Resource
    private AlgorithmMapper algorithmMapper;

    @Override
    public List<Algorithm> getAlgorithms() {
        return algorithmMapper.getAlgorithms();
    }

    @Override
    public List<Algorithm> getAlgorithmsByUserId(int userId) {
        return algorithmMapper.getAlgorithmsByUserId(userId);
    }

    @Override
    public List<Algorithm> getAlgorithmsByType(int type) {
        return algorithmMapper.getAlgorithmsByType(type);
    }
}
