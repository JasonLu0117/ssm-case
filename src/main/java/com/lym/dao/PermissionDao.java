package com.lym.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lym.entity.Permission;
import com.lym.vo.bo.UserPermissionBo;

public interface PermissionDao {

    int add(Permission permission);
    
    int addToRole(@Param("roleId") int roleId, @Param("permissionId") int permissionId);
    
    List<Permission> getPermissionList(int roleId);
    
    UserPermissionBo get(@Param("userId") int userId, @Param("roleId") int roleId);
    
}
