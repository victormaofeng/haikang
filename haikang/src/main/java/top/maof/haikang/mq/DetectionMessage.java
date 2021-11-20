package top.maof.haikang.mq;

import lombok.Data;
import lombok.ToString;
import top.maof.haikang.model.DetectedFile;

import java.util.Date;

/**
 * 目标检测或重识别时java和python通信类
 *
 * @author 毛逢
 * @date 2021/9/18 9:49
 */
@Data
@ToString
public class DetectionMessage {

    // 文件根路径
    private String rootPath;

    private int detectId;


    private boolean isDetect;




    // 重识别时需要识别的对象的图片
    private String path;
    private String type;





    // 待检测的文件位置(相对地址)
    private String detectPath;
    private String detectType;


    // 检测完毕后生成的新文件存放位置(相对地址)
    private String detectedPath;
    private String detectedType;

    // 使用算法
    private int algoId;

    private String token;

    public DetectedFile detectedFile() {
        DetectedFile detectedFile = new DetectedFile();
        detectedFile.setAlgoId(algoId);
        detectedFile.setDetectFileId(detectId);
        detectedFile.setInsertTime(new Date());
        detectedFile.setPath(detectedPath);
        detectedFile.setType(detectedType);
        return detectedFile;
    }
}