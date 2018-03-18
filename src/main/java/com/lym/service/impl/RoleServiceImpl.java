package com.lym.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lym.dao.RoleDao;
import com.lym.entity.Role;
import com.lym.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleDao roleDao;
    
    public int addRole(Role role) throws Exception {
        return roleDao.add(role);
    }
    
    public Role getRole(int id) throws Exception {
        return roleDao.get(id);
    }

}
