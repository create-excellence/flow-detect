package com.explore.until;

import com.explore.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserUtils {

    @Autowired
    UserClient userClient;

    public Integer getUserId(){
        Map<String,Integer> data = userClient.getById();
        if(data==null) return null;
        return  data.get("user_id");
    }
}
