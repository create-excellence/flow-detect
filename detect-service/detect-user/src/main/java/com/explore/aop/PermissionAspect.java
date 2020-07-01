package com.explore.aop;

import com.explore.service.IRoleService;
import com.explore.service.IUserService;
import com.explore.common.annotation.Permission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class PermissionAspect {
    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;

    /**
     * @Description: 角色权限验证
     * @Param: [join, permission]
     * @return: boolean
     * @Author: ashe
     * @Date: 2019/11/15
     */
    @Pointcut("execution(public * com.explore.controller.*.*(..)) within(com.explore.controller.*)")
    private void point() {
    }

    //切入点签名
    @Before(value = "point()&&@annotation(permission)")
    public void rolePermission(JoinPoint joinPoint, Permission permission) {
        printLog();
        List<String> userRoles = getRoles();             // 用户角色集合
        String[] roles = permission.roles();             // 满足条件的角色数组
        checkPermission(userRoles, roles);
    }

    /**
     * @Author 安羽兮
     * @Description 判断用户是否有权限访问
     * @Date 15:06 2019/12/7
     * @Param [roles, userRoles]
     * @Return boolean
     **/
    private boolean checkPermission(List<String> userRoles, String[] roles) {
        for (String role : userRoles) {
            for (String r : roles) {
                // 用户只要有一个角色满足条件即可
                if (role.equals(r)) {
                    return true;
                }
            }
        }
//        throw new AuthenticationException("权限不足");
//        throw new Exception("权限不足");
        return false;
    }

    private List<String> getRoles() {
        Long userId = userService.getUserIdByToken();
        return roleService.getRoles(userId);
    }

    public void printLog() {
        // Get request attribute
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();

        log.info("Request URL: [{}], URI: [{}], Request Method: [{}], IP: [{}]",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIp(request));
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip;
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

        for (String header : headers) {
            ip = request.getHeader(header);
            if (checkIP(ip)) {
                return ip;
            }
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }
}
