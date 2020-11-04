package com.explore.socket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.explore.client.CameraClient;
import com.explore.client.FlowClient;
import com.explore.client.UserClient;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.controller.FlowController;
import com.explore.servcie.impl.WarningServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author PinTeh
 */
@Slf4j
@Component
@ServerEndpoint(value = "/connect/{cid}")
public class WebSocketServer {

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    @Autowired
    public void setUserClient(UserClient userService){
        WebSocketServer.userClient=userService;
    }

    @Autowired
    public void setFlowClient(FlowClient flowClient){
        WebSocketServer.flowClient=flowClient;
    }

    @Autowired
    public void setCameraClient(CameraClient cameraClient){
        WebSocketServer.cameraClient=cameraClient;
    }

    private static  UserClient userClient;

    private static   FlowClient flowClient;

    private static    CameraClient cameraClient;
    @Autowired
    private WarningServiceImpl warningService;

    private static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }



    private static final ConcurrentHashMap<String, Session> MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> tokenMAP = new ConcurrentHashMap<>();



    @OnOpen
    public void onOpen(@PathParam("cid") String cid,  Session session){
        Integer userId = getUserId(cid);
        if(userId==null) return;
        if (!MAP.containsKey(cid)) {
            MAP.put(cid, session);
        }
        tokenMAP.put(userId.toString(),cid);
        addOnlineCount();
        log.info("[连接消息] cid:" + cid  + "当前连接数:" + WebSocketServer.getOnlineCount());

    }

    @OnMessage
    public void onMessage(@PathParam("cid")String cid, String message, Session session) {
        if(message.equals("init")){
            Integer user_id = getUserId(cid);
            ServerResponse serverResponse = cameraClient.getByUserId(user_id);
            if(serverResponse.isSuccess()){
                if(serverResponse.getData()!=null){
                    List<Map> result = new ArrayList<>();
                    List<LinkedHashMap> cameraList =(List<LinkedHashMap>) ((LinkedHashMap)serverResponse.getData()).get("records");
                    for(LinkedHashMap o:cameraList){
                        Map map = new HashMap();
                        map.put("camera",o);
                        Flow flow = flowClient.getById((Integer) o.get("id")).getData();
                        Long num = 0L;
                        if(flow != null) num = flow.getFlow();
                        map.put("flow",num);
                        result.add(map);
                    }

                    this.sendMessageToAll(String.valueOf(cid)
                            , WebMessage.createInitMessage(result, LocalDateTime.now()).toJson());
                }

            }
        }
        log.info("[收到消息] cid:" + cid + ", message:" + message);
    }

    @OnClose
    public void onClose(@PathParam("cid") String cid, Session session) {
        log.info("[连接退出消息] cid:" + cid);
        Integer userId = getUserId(cid);
        MAP.remove(cid);
        if(userId==null) return;
        tokenMAP.remove(userId.toString());
        subOnlineCount();
    }

    @OnError
    public void onError(@PathParam("cid")String cid, Session session, Throwable error) {
        log.info("[发生错误消息] cid:" + cid);
        Integer userId = getUserId(cid);
        MAP.remove(cid);
        if(userId==null) return;
        tokenMAP.remove(userId.toString());
        subOnlineCount();
        error.printStackTrace();
    }

    public void sendMessageToAll(String cid, String message)  {
        //log.info("[发送消息] 当前连接数:" + sessions.size());
        Session connect = MAP.get(cid);
        if(connect ==null) return;
        try {
            connect.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送失败");
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

    private Integer getUserId(String token){
        Map res =  userClient.getById(token);
        if(res==null) return null;
        Map data = (Map) res.get("data");
        if(data==null) return null;
        return (Integer) data.get("user_id");
    }

}
