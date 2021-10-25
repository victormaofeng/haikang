package top.maof.haikang.service;

import top.maof.haikang.model.DeveloperCamera;

import java.util.List;


public interface DeveloperCameraService {
    DeveloperCamera getByCameraId(Integer cameraId);

    List<DeveloperCamera> getsByCameraId(Integer cameraId);
}
