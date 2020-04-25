package com.xxhhxhh.mainthing.databean;

public class RelatedBean
{
    private int type;//关系类型
    private String username;//关系用户
    private String toUsername;//被关系用户

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToUsername() {
        return toUsername;
    }
}
