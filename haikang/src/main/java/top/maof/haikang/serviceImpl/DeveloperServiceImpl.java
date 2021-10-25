package top.maof.haikang.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.DeveloperMapper;
import top.maof.haikang.model.Developer;
import top.maof.haikang.service.DeveloperService;

@Service
public class DeveloperServiceImpl implements DeveloperService {
    @Autowired
    DeveloperMapper developerMapper;

    @Override
    public int update(Developer developer) {
        return developerMapper.update(developer);
    }
}
