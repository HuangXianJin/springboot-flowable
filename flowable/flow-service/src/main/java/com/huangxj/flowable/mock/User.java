package com.huangxj.flowable.mock;

/**
 * TODO
 *
 * @author huangxj
 * @version 1.0
 * @date 2022/4/7 11:49
 */
public class User {

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    private String userId;

    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
