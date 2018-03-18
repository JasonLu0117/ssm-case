package com.lym.dao;

import com.lym.entity.Role;

public interface RoleDao {

    int add(Role role);
    
    Role get(int id);
    
}
