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
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    // 未实现单设备登录的判断方法
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        // 登录拦截和判断，当header中没有jwt，或者header中的jwt在redis中无法找到时，需要重新登录
        if (jwt != null && redisUtil.isExist(jwt)) {
            // 完成上一步验证后，按jwt的方式，验证该jwt是否正确，正确则说明该jwt正确，且未过期，直接返回true，不再走登录流程（当然加上登录没有影响）
            if (JWTUtil.verify(jwt)) {
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
        }
        redirectToLogin(servletRequest, servletResponse);
        return false;
    }

    // 实现了单设备登录的判断方法
    /*
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        // 登录拦截和判断，当header中没有jwt，或者header中的jwt在redis中无法找到时，需要重新登录
        if (jwt != null && JWTUtil.getClaim(jwt, "username") != null && redisUtil.isExist(JWTUtil.getClaim(jwt, "username"))) {
            // 完成上一步验证后，按jwt的方式，验证该jwt是否正确，正确则说明该jwt正确，且未过期，直接返回true，不再走登录流程（当然加上登录没有影响）
            if (redisUtil.getStr(JWTUtil.getClaim(jwt, "username")) != null && jwt.equals(redisUtil.getStr(JWTUtil.getClaim(jwt, "username"))) && JWTUtil.verify(jwt)) {
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
    */

}
