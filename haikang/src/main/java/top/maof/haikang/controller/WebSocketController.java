package top.maof.haikang.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/webSocket/{token}")
@Component
@Slf4j
public class WebSocketController {

    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>(16);


    public static Session getSession(String token) {
        return sessionPool.get(token);
    }


    //发送消息
    public static void sendMessage(Session session, String message) throws IOException {

        if (session != null) {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    //给指定用户发送信息
    public static void send(String token, String message) {
        Session session = getSession(token);
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        sessionPool.put(token, session);
    }

    // 关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "token") String token) {
        sessionPool.remove(token);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("浏览器消息:" + message);
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("发生错误");
        if (sessionPool.containsValue(session)) {
            List<String> list = new ArrayList<>();
            Set<Map.Entry<String, Session>> entries = sessionPool.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                if (entry.getValue() == session) {
                    list.add(entry.getKey());
                }
            }
            if (list.size() > 0) {
                for (String token : list) {
                    sessionPool.remove(token);
                }
            }
        }
    }

}
