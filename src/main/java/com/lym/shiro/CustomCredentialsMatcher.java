package com.lym.shiro;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import com.lym.service.UserService;
import com.lym.util.MD5Util;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 输入的密码
        String tokenCredentials = String.valueOf(token.getPassword());
        // 数据库中的密码
        String dbCredentials = info.getCredentials().toString();
        // 将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        boolean res = false;
        try {
            res = MD5Util.validPassword(tokenCredentials, dbCredentials);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

}
