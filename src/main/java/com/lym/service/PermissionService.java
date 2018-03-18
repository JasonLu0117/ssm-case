package com.lym.service;

import java.util.List;

import com.lym.entity.Permission;
import com.lym.vo.bo.UserPermissionBo;

public interface PermissionService {

    int addPermission(Permission permission) throws Exception;
    
    int addPermissionToRole(int roleId, int permissionId) throws Exception;
    
    int addPermissionAndRole(int roleId, Permission permission) throws Exception;
    
    List<Permission> getPermissionList(int roleId) throws Exception;
    
    UserPermissionBo getUserPermission(int userId, int roleId) throws Exception;
    
}
