package com.lym.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    
    private int id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String idcard;
    private String headpic;
    private int enabled;
    private Timestamp createat;
    private Timestamp updateat;
    private int activeState;
    private String token;
    private long tokenEnableTime;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getIdcard() {
        return idcard;
    }
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
    public String getHeadpic() {
        return headpic;
    }
    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }
    public int getEnabled() {
        return enabled;
    }
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
    public Timestamp getCreateat() {
        return createat;
    }
    public void setCreateat(Timestamp createat) {
        this.createat = createat;
    }
    public Timestamp getUpdateat() {
        return updateat;
    }
    public void setUpdateat(Timestamp updateat) {
        this.updateat = updateat;
    }
    public int getActiveState() {
        return activeState;
    }
    public void setActiveState(int activeState) {
        this.activeState = activeState;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public long getTokenEnableTime() {
        return tokenEnableTime;
    }
    public void setTokenEnableTime(long tokenEnableTime) {
        this.tokenEnableTime = tokenEnableTime;
    }
}
