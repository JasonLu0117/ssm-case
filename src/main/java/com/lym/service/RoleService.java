package com.lym.service;

import com.lym.entity.Role;

public interface RoleService {

    int addRole(Role role) throws Exception;
    
    Role getRole(int id) throws Exception;
}
