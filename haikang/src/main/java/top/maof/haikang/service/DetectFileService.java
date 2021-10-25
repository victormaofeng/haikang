package top.maof.haikang.service;

import top.maof.haikang.model.DetectFile;

public interface DetectFileService {
    // 返回值为该记录存放数据库后的id,失败为0
    int insert(DetectFile detectFile);

    int update(DetectFile detectFile);

    DetectFile getDetectFileById(int id);

    int delete(int id);
}
