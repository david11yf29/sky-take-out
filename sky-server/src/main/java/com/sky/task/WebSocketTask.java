package com.sky.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketTask {
    
    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired // ChatGPT 補充
    private ObjectMapper jacksonObjectMapper;

    /**
     * 通过WebSocket每隔5秒向客户端发送消息
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void sendMessageToClient() {
//        webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
//    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient() {
        try {
            Map<String, String> message = new HashMap<>();
            message.put("message", "这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));

            String jsonMessage = jacksonObjectMapper.writeValueAsString(message);
            webSocketServer.sendToAllClient(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


