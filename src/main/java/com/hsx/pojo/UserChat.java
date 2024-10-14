package com.hsx.pojo;



public class UserChat {
    private String userId;

    /**
     * 我的头像，如果没有默认给一张
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nick;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}