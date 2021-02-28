package com.explore.analyze.until;

import com.explore.analyze.client.UserClient;
import com.explore.common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class UserUtils {

    private final UserClient userClient;

    public UserUtils(UserClient userClient) {
        this.userClient = userClient;
    }

    public Integer getUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Const.TOKEN);
        Map res = userClient.getById(token);
        if(res!=null) {
            Map data = (Map) res.get("data");
            if(data!=null) {
                return (Integer) data.get("user_id");
            }
        }
        return null;
    }
}
