package com.lym.service;

import com.lym.entity.User;

public interface UserService {

    boolean addUser(User user) throws Exception;
    
    boolean activateUser(User user, String token) throws Exception;
    
    boolean register(User user) throws Exception;
    
    int deleteUser(int id);
    
    int updateUser(User user);
    
    User getUser(int id);
    
    User getUserByUsername(String username);
    
}
