package top.maof.haikang.service;

import top.maof.haikang.model.Algorithm;

import java.util.List;

public interface AlgorithmService {

    List<Algorithm> getAlgorithms();

    List<Algorithm> getAlgorithmsByUserId(int userId);

    List<Algorithm> getAlgorithmsByType(int type);
}
