package com.explore.socket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author PinTeh
 */
@Slf4j
@Component
@ServerEndpoint(value = "/connect/{rid}")
public class WebSocketServer {

    private static int onlineCount = 0;

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    private static final ConcurrentHashMap<String, Set<Session>> MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("cid") String cid, @PathParam("token") String token, Session session){
        if (!MAP.containsKey(cid)) {
            Set<Session> connects = new HashSet<>();
            connects.add(session);
            MAP.put(cid, connects);
        } else {
            MAP.get(cid).add(session);
        }
        log.info("[连接消息] cid:" + cid + " session:" + session.getId() + "当前连接数:" + MAP.get(cid).size());
        addOnlineCount();
    }

    @OnMessage
    public void onMessage(@PathParam("cid")String cid, @PathParam("token") String token, String message, Session session) {
        WebMessage webMessage = JSON.parseObject(message, WebMessage.class);
        sendMessage(webMessage.toJson(), MAP.get(cid),session);
        log.info("[收到消息] cid:" + cid + ", message:" + message);
    }

    @OnClose
    public void onClose(@PathParam("cid") String cid, Session session) {
        log.info("[连接退出消息] cid:" + cid);
        Set set= MAP.get(cid);
        if (set == null){
            return;
        }
        set.remove(session);
        subOnlineCount();
    }

    @OnError
    public void onError(@PathParam("cid")String cid, Session session, Throwable error) {
        subOnlineCount();
        error.printStackTrace();
    }

    public void sendMessageToAll(String cid, String message)  {
        Set<Session> sessions = MAP.get(cid);
        log.info("[发送消息] 当前连接数:" + sessions.size());
        for (Session connect : sessions) {
            try {
                connect.getBasicRemote().sendText(message);
            } catch (IOException e) {
               log.error("发送失败");
            }
        }
    }

    private void sendMessage(String message, Set<Session> sessions, Session session)  {
        log.info("[系统发送消息] 当前连接数:" + sessions.size());
        for (Session connect : sessions) {
            if (!connect.equals(session)) {
                try {
                    connect.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
