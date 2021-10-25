package top.maof.haikang.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Date类型json化格式配置
 *
 * @author 毛逢
 * @version 1.0
 * @email 3286408344@qq.com
 * @date 2021/5/24 15:36
 */
@Configuration
@EnableWebMvc
// 必须要有这个注解，否则报错：JSON parse error: Cannot deserialize value of type
// `java.time.LocalDateTime` from String \"2020-06-04 15:07:54\": Failed to deserialize java.time.LocalDateTime:
// (java.time.format.DateTimeParseException) Text '2020-06-04 15:07:54' could not be parsed at index 10;
// nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException:
// Cannot deserialize value of type `java.time.LocalDateTime` from String \"2020-06-04 15:07:54\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2020-06-04 15:07:54' could not be parsed at index 10\n at [Source: (PushbackInputStream); line: 5, column: 17] (through reference chain: com.heartsuit.domain.Book[\"publishDate\"]
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charset.forName("UTF-8"));
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        fastJsonConverter.setFastJsonConfig(config);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(list);
        converters.add(fastJsonConverter);
    }
}