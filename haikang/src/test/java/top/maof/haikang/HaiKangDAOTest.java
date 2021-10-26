package top.maof.haikang;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.maof.haikang.mapper.DeveloperCameraMapper;
import top.maof.haikang.model.DeveloperCamera;

import javax.annotation.Resource;

/**
 * @ClassName HaiKangDAOTest
 * @Description TODO
 * @Author Quentin_zyj
 * @Date 2021/10/25 20:19
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HaiKangDAOTest {

    @Resource
    private DeveloperCameraMapper developerCameraMapper;

    @Test
    public void test1() {
        DeveloperCamera developerCamera = new DeveloperCamera();
        developerCamera.setCameraId(2);
        developerCamera.setDeveloperId(2);
        developerCameraMapper.insert(developerCamera);
    }

    @Test
    public void test2() {
        System.out.println(developerCameraMapper.getsByCameraId(1));
    }

}
