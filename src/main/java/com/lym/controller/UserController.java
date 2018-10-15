package com.lym.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lym.annotation.Cros;
import com.lym.entity.User;
import com.lym.service.PermissionService;
import com.lym.service.UserService;
import com.lym.shiro.JWTUtil;
import com.lym.shiro.RedisUtil;
import com.lym.util.CaptchaUtil;
import com.lym.util.constants.Constants;
import com.lym.vo.BaseVo;
import com.lym.vo.bo.UserPermissionBo;

@Controller
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class);

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionService permissionService;
    
    @RequestMapping(value="/user", method=RequestMethod.POST)
    @ResponseBody
    public BaseVo addUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String idcard = request.getParameter("idcard");
        String headpic = request.getParameter("headpic");
        String enabled = request.getParameter("enabled");
        
        User user = new User();
        BaseVo baseVo = new BaseVo();
        
        try {
            user.setUsername(username);
            user.setNickname(nickname);
            user.setPassword(password);
            user.setEmail(email);
            user.setIdcard(idcard);
            user.setHeadpic(headpic);
            user.setEnabled(Integer.valueOf(enabled));
            user.setCreateat(new Timestamp(new java.util.Date().getTime()));
            user.setUpdateat(new Timestamp(new java.util.Date().getTime()));
        
            userService.addUser(user);
        } catch(Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        return baseVo;
    }
    
    @RequestMapping(value="/user/activate", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo activateUser(HttpServletRequest request) {
        String id = request.getParameter("id");
        String token = request.getParameter("token");

        BaseVo baseVo = new BaseVo();
        User user = null;
        boolean res = false;
        try {
            user = userService.getUser(Integer.valueOf(id));

            if (System.currentTimeMillis() > user.getTokenEnableTime()) {
                baseVo.setStatus(FAILURE);
                baseVo.setMessage("token has disabled");
                return baseVo;
            }

            res = userService.activateUser(user, token);

            if (!res) {
                baseVo.setStatus(FAILURE);
                baseVo.setMessage("user register failed!");
                return baseVo;
            }
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }

        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("user register succeed!");
        return baseVo;
    }
    
    @Cros
    @RequestMapping(value="/user/{id}", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo getUser(@PathVariable String id) {
        BaseVo baseVo = new BaseVo();
        User user = null;
        
        try {
            user = userService.getUser(Integer.valueOf(id));
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setData(user);
        return baseVo;
    }

    @RequestMapping(value="/user", method=RequestMethod.PUT)
    @ResponseBody
    public BaseVo updateUser(HttpServletRequest request) {
        String id = request.getParameter("id");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String idcard = request.getParameter("idcard");
        String headpic = request.getParameter("headpic");
        String enabled = request.getParameter("enabled");
        
        BaseVo baseVo = new BaseVo();
        User user = null;
        try {
            user = userService.getUser(Integer.valueOf(id));
            if (user == null) {
                baseVo.setStatus(FAILURE);
                baseVo.setMessage("update failed!");
                return baseVo;
            }
            
            if (nickname != null && !nickname.equals("")) {
                user.setNickname(nickname);
            }
            if (password != null && !password.equals("")) {
                user.setPassword(password);
            }
            if (email != null && !email.equals("")) {
                user.setEmail(email);
            }
            if (idcard != null && !idcard.equals("")) {
                user.setIdcard(idcard);
            }
            if (headpic != null && !headpic.equals("")) {
                user.setHeadpic(headpic);
            }
            if (enabled != null && !enabled.equals("")) {
                user.setEnabled(Integer.valueOf(enabled));
            }
                
            userService.updateUser(user);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setData(user);
        return baseVo;
    }
    
    @RequestMapping(value="/user/captcha.jpg", method=RequestMethod.GET)
    @ResponseBody
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        // 通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        CaptchaUtil util = CaptchaUtil.Instance();
        // 将验证码输入到session中，用来验证
        String code = util.getString();
//        request.getSession().setAttribute("code", code);
        SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_CAPTCHA, code);
        // 输出打web页面
        try {
            ImageIO.write(util.getImage(), "jpg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value="/user/register", method=RequestMethod.POST)
    @ResponseBody
    public BaseVo register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User user = new User();
        BaseVo baseVo = new BaseVo();
        
        try {
            user.setUsername(username);
            user.setNickname(nickname);
            user.setPassword(password);
            user.setEmail(email);
            user.setIdcard("");
            user.setHeadpic("");
            user.setEnabled(0);
            user.setCreateat(new Timestamp(new java.util.Date().getTime()));
            user.setUpdateat(new Timestamp(new java.util.Date().getTime()));
        
            boolean res = userService.register(user);
            
            if (!res) {
                baseVo.setStatus(FAILURE);
                baseVo.setMessage("register fail!");
                return baseVo;
            }
        } catch(Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("register succeed!");
        return baseVo;
    }
    
    @RequestMapping(value="/user/login", method=RequestMethod.GET)
    @ResponseBody
    public String login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        int roleId = Integer.valueOf(request.getParameter("roleId"));
        Subject subject = SecurityUtils.getSubject();
        
        try {
            try {
//                CaptchaUtil.checkCode(code);
            } catch (Exception e) {
                return "code error";
            }
            
            User user = userService.getUserByUsername(username);
            UserPermissionBo userPermissionBo = permissionService.getUserPermission(user.getId(), roleId);
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("username", username);
            map.put("password", password);
            map.put("role", userPermissionBo.getRoleName());
            map.put("permission", userPermissionBo.getPermissionCodeList());
            String jwt = JWTUtil.createToken(map);
            UsernamePasswordToken token = new UsernamePasswordToken(jwt, jwt);
            subject.login(token);

            System.out.println("jwt:" + jwt);
            redisUtil.addStr("shiro_redis_jwt:" + username, jwt);
            // 过期时间为7200秒，即2小时
            redisUtil.expire("shiro_redis_jwt:" + username, 7200);
            
            return "user/index";
        } catch (IncorrectCredentialsException ice) {
            // 密码错误异常
            logger.error("password error!");
        } catch (UnknownAccountException uae) {
            // 未知用户名异常
            logger.error("username error!");
        } catch (ExcessiveAttemptsException eae) {
            // 错误登录过多的异常
            logger.error("times error!");
        } catch (AuthenticationException ae) {
            // 登录失败异常
            logger.error("login fail!");
        } catch (Exception e) {
            // 获取用户失败
            logger.error("get user info fail!");
        }
        
        return "redirect:/failure.jsp";
    }
    
    @RequestMapping(value="/user/logout", method=RequestMethod.GET)
    @ResponseBody
    public String logout(HttpServletRequest request) {
        // 删除redis中的jwt
        if (request.getHeader("Authorization") != null) {
            redisUtil.delStr("shiro_redis_jwt:" + JWTUtil.parseToken(request.getHeader("Authorization")).get("username").toString());
        }
//        if (request.getParameter("username") != null) {
//            redisUtil.delStr("shiro_redis_jwt:" + request.getParameter("username"));
//        }
        
        // 删除session
        // SecurityUtils.getSubject().getSession().removeAttribute(Constants.SESSION_USER_INFO);
        // Shiro中的登出方法，才会真实的将JSESSIONID删除
        SecurityUtils.getSubject().logout();
        return "logout";
    }
    
}
