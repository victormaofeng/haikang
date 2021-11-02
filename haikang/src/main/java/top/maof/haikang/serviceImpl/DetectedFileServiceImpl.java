package top.maof.haikang.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.DetectedFileMapper;
import top.maof.haikang.model.DetectedFile;
import top.maof.haikang.mq.Message;
import top.maof.haikang.service.DetectedFileService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DetectedFileServiceImpl implements DetectedFileService {

    @Autowired
    private DetectedFileMapper detectedFileMapper;

    @Override
    public int insert(DetectedFile detectedFile) {
        return detectedFileMapper.insert(detectedFile) == 0 ? 0 : detectedFile.getId();
    }

    @Override
    public int update(DetectedFile detectedFile) {
        return detectedFileMapper.update(detectedFile);
    }

    @Override
    public int delete(int id) {
        return detectedFileMapper.delete(id);
    }


    @Override
    public DetectedFile getDetectedFileById(int id) {
        return detectedFileMapper.getDetectedFileById(id);
    }


    @Override
    public List<DetectedFile> gets(int page, int pageSize) {
        if (page < 1) return null;
        return detectedFileMapper.gets((page - 1) * pageSize, pageSize);
    }
    @Override
    public Message getMessage(int id){
        return detectedFileMapper.getMessage(id);
    }
}
