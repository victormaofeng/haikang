package top.maof.haikang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * 静态资源映射配置
 * 将所有视频放在同一根目录下,并将文件根目录进行静态资源映射,
 * 并根据当前操作系统切换到指定的根目录
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 数组中第一个元素是windows系统中的根路径
     * 第二个元素是linux系统中的根路径
     */
    private static String[] staticResources = new String[]{"C:\\Users\\Administrator\\Desktop\\1\\",
            "/home/mf/upload"};


    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler("/**");
        resourceHandlerRegistration.addResourceLocations("classpath:/static/");
        if (staticResources != null)
            for (String s : staticResources) {
                resourceHandlerRegistration.addResourceLocations("file:" + s);
            }
//        resourceHandlerRegistration.addResourceLocations("file:C:\\Users\\Administrator\\Desktop\\1\\")
//                .addResourceLocations("classpath:/static/");  // 映射本地静态资源

    }

    public static String uploadRootPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.indexOf("windows") > -1) {
            // windows 系统
            return staticResources[0];
        } else {
            // linux 系统 或 mac 系统
            return staticResources[1];
        }
    }

}