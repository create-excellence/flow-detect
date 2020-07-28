package com.explore.socket;

import com.alibaba.fastjson.JSON;
import lombok.*;

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

}
