package com.ycjw.minesecurity.model;

import java.util.Date;

/**
 * 用户类
 */
public class User {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户电话
     */
    private String phoneNum;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 性别：1-男，0-女
     */
    private int userSex;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户注册时间
     */
    private Date registerDate;

    /**
     * 是否完善资料
     */
    private boolean isComplete = false;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userName='" + userName + '\'' +
                ", userSex=" + userSex +
                ", password='" + password + '\'' +
                ", registerDate=" + registerDate +
                ", isComplete=" + isComplete +
                '}';
    }
}
