package com.lym.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lym.dao.UserDao;
import com.lym.entity.User;
import com.lym.service.UserService;
import com.lym.util.MD5Util;
import com.lym.util.MailUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    
    public boolean addUser(User user) throws Exception {
        String email = user.getEmail();
        String pwd = null;
        String token = null;
        long currentTime = System.currentTimeMillis();
        long tokenEnableTime = currentTime + 1000*60*2;

        try {
            pwd = MD5Util.getEncryptedPwd(user.getPassword());
            token = MD5Util.getEncryptedPwd(email + pwd + currentTime);
        } catch (NoSuchAlgorithmException e1) {
            logger.error(e1);
            throw e1;
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1);
            throw e1;
        }
        
        user.setPassword(pwd);
        user.setActiveState(0);
        user.setToken(token);
        user.setTokenEnableTime(tokenEnableTime);
        userDao.add(user);
        
        String subject = "主题：验证码";
        StringBuffer sb = new StringBuffer();
        sb.append("<div>您的验证码为：</div>");
        sb.append(token);
        sb.append("<div>请在两分钟内输入，并完成激活</div>");
        String text = sb.toString();
        
        try {
            MailUtil.sendMail(email, "发现人XXX", "收件人XXX", subject, text);
        } catch (MessagingException e) {
            logger.error(e);
            throw e;
        }
          
//         测试回滚
//        try {
//            Map<String, Object> paramMap = new HashMap<String, Object>();
//            String result = HttpUtil.httpClientPut("http://10.240.4.3:8080/account", paramMap);
//        } catch (Exception e) {
//            throw e;
//        }
        
        return true;
    }
    
    public boolean activateUser(User user, String token) throws Exception {
        String tokenInDb = user.getToken();
        if (token.equals(tokenInDb)) {
            user.setActiveState(1);
            userDao.update(user);
            return true;
        }
        
        return false;
    }
    
    public boolean register(User user) throws Exception {
        String email = user.getEmail();
        String pwd = null;
        String token = null;
        long currentTime = System.currentTimeMillis();
        long tokenEnableTime = currentTime + 1000*60*2;

        try {
            pwd = MD5Util.getEncryptedPwd(user.getPassword());
            token = MD5Util.getEncryptedPwd(email + pwd + currentTime);
        } catch (NoSuchAlgorithmException e1) {
            logger.error(e1);
            throw e1;
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1);
            throw e1;
        }
        
        user.setPassword(pwd);
        user.setActiveState(0);
        user.setToken(token);
        user.setTokenEnableTime(tokenEnableTime);
        userDao.add(user);
        
        return true;
    }
    
    public int deleteUser(int id) {
        return userDao.delete(id);
    }
    
    public int updateUser(User user) {
        return userDao.update(user);
    }
    
    public User getUser(int id) {
        return userDao.get(id);
    }

    public User getUserByUsername(String username) {
        return userDao.getByUsername(username);
    }
    
}
