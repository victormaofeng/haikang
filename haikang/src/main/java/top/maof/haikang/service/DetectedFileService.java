package top.maof.haikang.service;

import top.maof.haikang.model.DetectedFile;
import top.maof.haikang.mq.Message;

import java.util.List;

public interface DetectedFileService {
    int insert(DetectedFile detectedFile);

    int update(DetectedFile detectedFile);

    int delete(int id);

    DetectedFile getDetectedFileById(int id);

    List<DetectedFile> gets(int page, int pageSize);

    Message getMessage(int id);
}
