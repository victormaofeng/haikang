package top.maof.haikang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Http请求类配置
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/24 15:36
 */
@Configuration
public class RestConfig {
    @Bean
    public RestTemplate restTemplate1() {
        return restTemplate("utf-8");
    }

    @Bean
    public RestTemplate restTemplate2() {
        return restTemplate("gbk");
    }

    @Bean
    public RestTemplate restTemplate3() {
        return restTemplate("gb2312");
    }

    public static RestTemplate restTemplate(String charset) {
//        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpComponentsClientHttpRequestFactory.setConnectTimeout(10 * 1000);
//        httpComponentsClientHttpRequestFactory.setReadTimeout(20 * 1000);
//        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName(charset));
                break;
            }
        }
        return restTemplate;
    }
}
