package com.xxhhxhh.database.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "AdminUserInfo")
public class AdminUserInfo implements Serializable
{
    //识别码
    @Id
    @Column(name = "identifyText", length = 15)
    private String identifyText;
    //用户名
    @Column(name = "username", length = 15, unique = true)
    private String username;
    //密码
    @Column(name = "password", length = 15)
    private String passowrd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentifyText() {
        return identifyText;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setIdentifyText(String identifyText) {
        this.identifyText = identifyText;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }
}
