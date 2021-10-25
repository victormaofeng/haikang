package top.maof.haikang.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.DetectFileMapper;
import top.maof.haikang.model.DetectFile;
import top.maof.haikang.service.DetectFileService;

import javax.annotation.Resource;


@Service
public class DetectFileServiceImpl implements DetectFileService {

    @Autowired
    private DetectFileMapper detectFileMapper;

    @Override
    public int insert(DetectFile detectFile) {
        // 返回自增主键, 插入失败返回0
        return detectFileMapper.insert(detectFile) == 0 ? 0 : detectFile.getId();
    }

    @Override
    public int update(DetectFile detectFile) {
        return detectFileMapper.update(detectFile);
    }

    @Override
    public DetectFile getDetectFileById(int id) {
        return detectFileMapper.getDetectFileById(id);
    }

    @Override
    public int delete(int id) {
        return detectFileMapper.delete(id);
    }
}
