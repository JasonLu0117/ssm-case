package com.lym.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lym.dao.PermissionDao;
import com.lym.entity.Permission;
import com.lym.service.PermissionService;
import com.lym.vo.bo.UserPermissionBo;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    
    public int addPermission(Permission permission) throws Exception {
        return permissionDao.add(permission);
    }
    
    public int addPermissionToRole(int roleId, int permissionId) throws Exception {
        return permissionDao.addToRole(roleId, permissionId);
    }
    
    public int addPermissionAndRole(int roleId, Permission permission) throws Exception {
        try {
            permissionDao.add(permission);
        } catch (Exception e) {
            throw e;
        }
        
        int permissionId = permission.getId();
        return permissionDao.addToRole(roleId, permissionId);
    }
    
    public List<Permission> getPermissionList(int roleId) throws Exception {
        return permissionDao.getPermissionList(roleId);
    }
    
    public UserPermissionBo getUserPermission(int userId, int roleId) throws Exception {
        return permissionDao.get(userId, roleId);
    }
    
}
