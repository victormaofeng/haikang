package top.maof.haikang.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import top.maof.haikang.model.Developer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 毛逢
 * @version 1.0
 * @description: 海康API
 * @email 3286408344@qq.com
 * @date 2021/5/21 16:38
 * 参考API文档
 * https://open.ys7.com/doc/zh/book/index/user.html
 */
@Component
@Slf4j
public class HaikangApi {
    @Autowired
    @Qualifier("restTemplate1")
    private RestTemplate restTemplate;

    // 协议
    public final static int PROTOCOL_EZOPEN = 1;
    public final static int PROTOCOL_HLS = 2;
    public final static int PROTOCOL_RTMP = 3;
    public final static int PROTOCOL_FLV = 4;

    // 视频清晰度
    public final static int QUALITY_HD = 1;
    public final static int QUALITY_SD = 2;

    // api调用成功时状态码
    private final static String SUCCESS_CODE = "200";

    // 直播地址请求地址
    private final static String LIVE_URL = "https://open.ys7.com/api/lapp/v2/live/address/get";

    // access_token 请求地址
    private final static String ACCESS_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";


    /**
     * 获取直播地址
     * https://open.ys7.com/doc/zh/book/index/address_v2.html
     *
     * @param: [accessToken, deviceSerial 设备号, protocol 协议, quality 视频清晰度]
     * @author 毛逢
     * @eamil 3286408344@qq.com
     * @date: 2021/5/24 15:40
     */
    public Live getLive(String accessToken, String deviceSerial, Integer protocol, Integer quality) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Host", "open.ys7.com");
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.put("accessToken", Collections.singletonList(accessToken));
        map.put("deviceSerial", Collections.singletonList(deviceSerial));
        map.put("protocol", Collections.singletonList(protocol));
        map.put("quality", Collections.singletonList(quality));
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity(map, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(LIVE_URL, HttpMethod.POST, httpEntity, String.class);
        String body = exchange.getBody();
        Response<Live> developerResponse = JSONObject.parseObject(body, new TypeReference<Response<Live>>() {
        });
        if (SUCCESS_CODE.equals(developerResponse.getCode())) {
            Live data = developerResponse.getData();
            return data;
        }
        return null;
    }

    /**
     * 直播地址类
     *
     * @author 毛逢
     * @version 1.0
     * @email 3286408344@qq.com
     * @date 2021/5/21 22:02
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Live {
        private String id;
        private String url;
        private String expireTime;
    }

    /**
     * 使用appKey, appSecret去获取access_token
     *
     * @param: [appKey, appSecret]
     * @author 毛逢
     * @eamil 3286408344@qq.com
     * @date: 2021/5/24 15:43
     */
    public Developer getAccessToken(String appKey, String appSecret) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Host", "open.ys7.com");
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.put("appKey", Collections.singletonList(appKey));
        map.put("appSecret", Collections.singletonList(appSecret));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(map, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, httpEntity, String.class);
        Response<Developer> developerResponse = JSONObject.parseObject(exchange.getBody(), new TypeReference<Response<Developer>>() {
        });
        // log.info(developerResponse.toString());

        if (SUCCESS_CODE.equals(developerResponse.getCode())) {
            Developer data = developerResponse.getData();
            data.setAppKey(appKey);
            data.setSecret(appSecret);
            return data;
        }
        return null;
    }


    /**
     * 海康API数据返回接口, 内部类
     *
     * @author 毛逢
     * @version 1.0
     * @email 3286408344@qq.com
     * @date 2021/5/21 16:40
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Response<T> {
        String code;
        String msg;
        T data;
    }
}
