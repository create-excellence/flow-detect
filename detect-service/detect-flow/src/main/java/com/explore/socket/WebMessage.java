package com.explore.socket;

import com.alibaba.fastjson.JSON;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PinTeh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebMessage {

    /**
     * 操作码
     */
    private int c;

    private Object d;

    /**
     * 操作类型
     */
    private String op;


    public String toJson(){
        return JSON.toJSONString(this);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class U{
        private Integer id;
        private String name;
    }

    public static WebMessage create(String message){
        WebMessage webMessage = new WebMessage();
        webMessage.setOp("MESSAGE");
        webMessage.setD(message);
        return webMessage;
    }

    public static WebMessage createFlowMessage(String number, LocalDateTime now,Integer id){
        WebMessage webMessage = new WebMessage();
        webMessage.setOp("MESSAGE");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("mm:ss");
        Map<String,Object> map = new HashMap<>(2);
        map.put("number",number);
        map.put("now",df.format(now));
        map.put("cid",id);
        webMessage.setD(map);
        return webMessage;
    }

    public static WebMessage createInitMessage(List<Map> data, LocalDateTime now){
        WebMessage webMessage = new WebMessage();
        webMessage.setOp("INIT");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String,Object> map = new HashMap<>(2);
        map.put("data",data);
        map.put("now",df.format(now));
        webMessage.setD(map);
        return webMessage;
    }
}
