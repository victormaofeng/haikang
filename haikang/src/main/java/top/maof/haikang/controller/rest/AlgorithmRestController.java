package top.maof.haikang.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.maof.haikang.constant.URLConstantHolder;
import top.maof.haikang.enums.AlgorithmType;
import top.maof.haikang.model.Algorithm;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.AlgorithmService;

import java.util.List;

/**
 * @ClassName AlgorithmRestController
 * @Description TODO
 * @Author Quentin_zyj
 * @Date 2021/10/26 16:31
 */
@RestController
@RequestMapping(URLConstantHolder.S + URLConstantHolder.ALGORITHM_REST)
public class AlgorithmRestController {

    @Autowired
    private AlgorithmService algorithmService;

    @RequestMapping(URLConstantHolder.ALGORITHM_REST_GET_ALGORITHMS)
    Result<List<Algorithm>> getAlgorithms() {
        return Result.success(algorithmService.getAlgorithms());
    }

    @RequestMapping(URLConstantHolder.ALGORITHM_REST_GET_ALGORITHMS_BY_USER_ID)
    Result<List<Algorithm>> getAlgorithmsByUserId(@RequestParam("userId") int userId) {
        if (userId <= 0) {
            return Result.response_400();
        }
        return Result.success(algorithmService.getAlgorithmsByUserId(userId));
    }

    @RequestMapping(URLConstantHolder.ALGORITHM_REST_GET_ALGORITHMS_BY_TYPE)
    Result<List<Algorithm>> getAlgorithmsByType(int type) {
        if (AlgorithmType.isLegal(type) == false) {
            return Result.response_400();
        }
        return Result.success(algorithmService.getAlgorithmsByType(type));
    }

}
