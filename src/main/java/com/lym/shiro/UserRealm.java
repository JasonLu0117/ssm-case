package com.lym.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.lym.entity.User;
import com.lym.service.UserService;
import com.lym.util.constants.Constants;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取登录的用户名
        String username = (String) getAvailablePrincipal(arg0);
        User user = userService.getUserByUsername(username);
        
        List<String> roles = new ArrayList<String>();
        String roleName = (String) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_ROLE);
        roles.add(roleName);
        List<String> permissions = (List<String>) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_PERMISSION);
//        List<String> permissions = new ArrayList<String>(); 
        
        if (user.getUsername().equals(username)) {
//            roles.add("角色1");
//            roles.add("角色2");
            info.addRoles(roles);
//            permissions.add("权限1");
//            permissions.add("权限2");
            info.addStringPermissions(permissions);
        } else {
            throw new AuthorizationException();
        }
        
        return info;
    }

    // 登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) arg0; 
        User user = userService.getUserByUsername(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException("user is null!");
        }
        // principal：认证的实体信息，可以是username，也可以是其他。
        Object principal = user.getUsername();
        // credentials：密码。
        Object credentials = user.getPassword();
        // realmName：当前realm对象的name，调用父类的getName()方法即可。
        String realmName = getName();
        // 盐值：取用户信息中唯一的字段来生成盐值，避免由于两个用户原始密码相同，加密后的密码也相同。
//        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUsername());
        // AuthenticatingRealm使用CredentialsMatcher进行密码匹配，也可以自定义实现。
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
        
        // session中不需要保存密码。
        // user.remove("password");
        user.setPassword("");
        // 将用户信息放入session中。
        SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_INFO, user);
        // 设置session超时时间为30分钟。
        SecurityUtils.getSubject().getSession().setTimeout(6000 * 30);
        
        return info;
    }

}
