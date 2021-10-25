package top.maof.haikang.vo;

import lombok.Data;
import top.maof.haikang.model.DetectFile;
import top.maof.haikang.model.DetectedFile;

import java.util.List;

@Data
public class DetectFileVo {

    private DetectFile detectFile;

    private List<DetectedFile> detectedFileList;
}
