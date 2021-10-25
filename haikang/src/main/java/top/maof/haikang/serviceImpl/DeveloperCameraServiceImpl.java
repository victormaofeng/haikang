package top.maof.haikang.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.maof.haikang.mapper.DeveloperCameraMapper;
import top.maof.haikang.model.DeveloperCamera;
import top.maof.haikang.service.DeveloperCameraService;

import java.util.List;

@Service
@Slf4j
public class DeveloperCameraServiceImpl implements DeveloperCameraService {
    @Autowired
    DeveloperCameraMapper developerCameraMapper;

    @Override
    public DeveloperCamera getByCameraId(Integer cameraId) {
        List<DeveloperCamera> developerCameras = this.getsByCameraId(cameraId);
        if (developerCameras != null && developerCameras.size() > 0) {
            return developerCameras.get(0);
        }
        return null;
    }

    @Override
    public List<DeveloperCamera> getsByCameraId(Integer cameraId) {
        return developerCameraMapper.getsByCameraId(cameraId);
    }
}
