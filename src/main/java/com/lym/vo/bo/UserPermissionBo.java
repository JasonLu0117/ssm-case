package com.lym.vo.bo;

import java.util.List;

public class UserPermissionBo {

    private int userId;
    private String userName;
    private String nickName;
    private String roleName;
    private List<String> perimissionCodeList;
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public List<String> getPerimissionCodeList() {
        return perimissionCodeList;
    }
    public void setPerimissionCodeList(List<String> perimissionCodeList) {
        this.perimissionCodeList = perimissionCodeList;
    }
}
