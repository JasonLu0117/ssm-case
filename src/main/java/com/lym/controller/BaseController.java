package com.lym.controller;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lym.vo.BaseVo;

/**
 * @description 对shiro登录/授权接口的异常处理。
 */
public abstract class BaseController {

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    
    // 登录认证异常
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    @ResponseBody
    public BaseVo authenticationException(HttpServletRequest request, HttpServletResponse response) {
        BaseVo baseVo = new BaseVo();
        baseVo.setStatus(FAILURE);
        baseVo.setMessage("no login!");
        return baseVo;
    }
    
    // 权限异常
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    @ResponseBody
    public BaseVo authorizationException(HttpServletRequest request, HttpServletResponse response) {
        BaseVo baseVo = new BaseVo();
        baseVo.setStatus(FAILURE);
        baseVo.setMessage("no authc!");
        return baseVo;
    }
    
}
