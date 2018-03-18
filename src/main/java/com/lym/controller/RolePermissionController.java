package com.lym.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lym.entity.Permission;
import com.lym.entity.Role;
import com.lym.service.PermissionService;
import com.lym.service.RoleService;
import com.lym.vo.BaseVo;
import com.lym.vo.bo.UserPermissionBo;

@Controller
public class RolePermissionController {

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private PermissionService permissionService;
    
    @RequestMapping(value="/role", method=RequestMethod.POST)
    @ResponseBody
    public BaseVo addRole(HttpServletRequest request) {
        String roleName = request.getParameter("roleName");
        Role role = new Role();
        BaseVo baseVo = new BaseVo();
        try {
            role.setRoleName(roleName);
            roleService.addRole(role);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("add role succeed!");
        return baseVo;
    }
    
    @RequestMapping(value="/role/{id}", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo getRole(@PathVariable String id) {
        BaseVo baseVo = new BaseVo();
        Role role = null;
        try {
            role = roleService.getRole(Integer.valueOf(id));
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setData(role);
        return baseVo;
    }
    
    @RequestMapping(value="/permission", method=RequestMethod.POST)
    @ResponseBody
    public BaseVo addPermission(HttpServletRequest request) {
        String permissionCode = request.getParameter("permissionCode");
        String permissionName = request.getParameter("permissionName");
        Permission permission = new Permission();
        
        BaseVo baseVo = new BaseVo();
        try {
            permission.setPermissionCode(permissionCode);
            permission.setPermissionName(permissionName);
            permissionService.addPermission(permission);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("add permission succeed!");
        return baseVo;
    }
    
    @RequestMapping(value="/permission/to/role", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo addPermissionToRole(HttpServletRequest request) {
        int roleId = Integer.valueOf(request.getParameter("roleId"));
        int permissionId = Integer.valueOf(request.getParameter("permissionId"));
        
        BaseVo baseVo = new BaseVo();
        try {
            permissionService.addPermissionToRole(roleId, permissionId);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("add permission to role succeed!");
        return baseVo;
    }
    
    @RequestMapping(value="/permission/and/role", method=RequestMethod.POST)
    @ResponseBody
    public BaseVo addPermissionAndRole(HttpServletRequest request) {
        int roleId = Integer.valueOf(request.getParameter("roleId"));
        String permissionCode = request.getParameter("permissionCode");
        String permissionName = request.getParameter("permissionName");

        Permission permission = new Permission();
        BaseVo baseVo = new BaseVo();
        try {
            permission.setPermissionCode(permissionCode);
            permission.setPermissionName(permissionName);
            permissionService.addPermissionAndRole(roleId, permission);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setMessage("add permission and to role succeed!");
        return baseVo;
    }
    
    @RequestMapping(value="/permission/list", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo getPermissionList(HttpServletRequest request) {
        int roleId = Integer.valueOf(request.getParameter("roleId"));

        BaseVo baseVo = new BaseVo();
        List<Permission> list = null;
        try {
            list = permissionService.getPermissionList(roleId);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setData(list);
        return baseVo;
    }
    
    @RequestMapping(value="/permission/all", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo getUserPermission(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        int roleId = Integer.valueOf(request.getParameter("roleId"));
        
        BaseVo baseVo = new BaseVo();
        UserPermissionBo userPermissionBo = new UserPermissionBo();
        try {
            userPermissionBo = permissionService.getUserPermission(userId, roleId);
        } catch (Exception e) {
            baseVo.setStatus(FAILURE);
            baseVo.setMessage(e.toString());
            return baseVo;
        }
        
        baseVo.setStatus(SUCCESS);
        baseVo.setData(userPermissionBo);
        return baseVo;
    }
    
}
