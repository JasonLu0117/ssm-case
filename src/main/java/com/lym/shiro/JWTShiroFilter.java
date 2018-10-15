package com.lym.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class JWTShiroFilter extends AccessControlFilter {

    @Autowired
    private RedisUtil redisUtil;
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
            ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (jwt != null && JWTUtil.parseToken(jwt) != null && JWTUtil.parseToken(jwt).get("username") != null && redisUtil.isExist("shiro_redis_jwt:" + JWTUtil.parseToken(jwt).get("username").toString())) {
            if (redisUtil.getStr("shiro_redis_jwt:" + JWTUtil.parseToken(jwt).get("username").toString()) != null && jwt.equals(redisUtil.getStr("shiro_redis_jwt:" + JWTUtil.parseToken(jwt).get("username").toString())) && JWTUtil.verifyToken(redisUtil.getStr("shiro_redis_jwt:" + JWTUtil.parseToken(jwt).get("username").toString()))) {
//                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(jwt, jwt);
//                try {
//                    // 委托realm进行登录认证
//                    getSubject(servletRequest, servletResponse).login(usernamePasswordToken);
                    return true;
//                } catch (Exception e) {
//                    return false;
//                }
            }
            redirectToLogin(servletRequest, servletResponse);
            return false;
        }
        redirectToLogin(servletRequest, servletResponse);
        return false;
    }

}
