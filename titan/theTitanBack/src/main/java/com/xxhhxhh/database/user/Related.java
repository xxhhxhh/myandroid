package com.xxhhxhh.database.user;

import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

//用户关系表
@Entity
@Table(name = "Related")
public class Related implements Serializable
{
    public Related(){}

    //关系用户名外键
    @Id
    @ManyToOne(targetEntity = UserInfo.class)
    @JoinColumn(name = "username")
    private UserInfo username;
    //被关系用户
    @Id
    @ManyToOne(targetEntity = UserInfo.class)
    @JoinColumn(name = "to_username")
    private UserInfo to_username;
    //关系
    //0拉黑
    //1关注
    @Id
    @Column(columnDefinition = "int default 0")
    private int type;

    public UserInfo getUsername() { return username; }
    public void setUsername(UserInfo username) { this.username = username; }
    public int getType() { return type; }
    public UserInfo getTo_username() { return to_username; }
    public void setTo_username(UserInfo to_username) { this.to_username = to_username; }
    public void setType(int type) { this.type = type; }
}
