package com.xxhhxhh.database.user;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

//用户信息表
@Entity
@Table(name = "UserInfo")
public class UserInfo
{
    public UserInfo(){}

    @Id
    @Column(length = 15)
    private String username;//用户名
    @Column(length = 15)
    private String password;//密码
    @Column(unique = true, length = 15)
    private String real_name;//真实姓名
    @Column(unique = true, length = 20)
    private String id_number;//身份证号
    @Column(unique = true, length = 20)
    private String nick_name;//昵称
    //用户爱好
    @Column(length = 80)
    private String my_love;
    //用户备注
    @Column
    private String my_other;
    //登录时间
    @Column(columnDefinition = "timestamp default NOW()")
    private Timestamp login_date;
    //注册日期
    @Column(columnDefinition = "timestamp default NOW()")
    private Timestamp register_date;
    //用户随记数量
    @Column(columnDefinition = "int default 0")
    private int user_suiji;
    //用户文章数量
    @Column(columnDefinition = "int default 0")
    private int user_article;
    //用户粉丝数
    @Column(columnDefinition = "int default 0")
    private int user_fans;
    //用户关注数
    @Column(columnDefinition = "int default 0")
    private int user_focus;
    //用户拉黑
    @Column(columnDefinition = "int default 0")
    private int user_dislike;
    //用户恶意等级
    @Column(columnDefinition = "int default 0")
    private int user_eyi;
    //陌生登录提醒
    @Column(columnDefinition = "int default 0")
    private int strange_login;
    //允许看随记
    @Column(columnDefinition = "int default 0")
    private int allow_look_suiji;
    //允许看文章
    @Column(columnDefinition = "int default 0")
    private int allow_look_article;
    //允许推荐
    @Column(columnDefinition = "int default 0")
    private int allow_recommend;
    //允许看详细信息
    @Column(columnDefinition = "int default 0")
    private int allow_detail;
    //头像
    @Column
    private Blob user_head_image;

    public Blob getUser_head_image() { return user_head_image; }
    public void setUser_head_image(Blob user_head_image) { this.user_head_image = user_head_image; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }
    public String getNick_name() { return nick_name; }
    public String getReal_name() { return real_name; }
    public void setNick_name(String nick_name) { this.nick_name = nick_name; }
    public void setReal_name(String real_name) { this.real_name = real_name; }
    public Timestamp getLogin_date() { return login_date; }
    public Timestamp getRegister_date() { return register_date; }
    public int getAllow_detail() { return allow_detail; }
    public int getAllow_look_article() { return allow_look_article; }
    public int getAllow_look_suiji() { return allow_look_suiji; }
    public int getStrange_login() { return strange_login; }
    public int getAllow_recommend() { return allow_recommend; }
    public int getUser_article() { return user_article; }
    public int getUser_dislike() { return user_dislike; }
    public int getUser_eyi() { return user_eyi; }
    public int getUser_fans() { return user_fans; }
    public int getUser_focus() { return user_focus; }
    public int getUser_suiji() { return user_suiji; }
    public String getId_number() { return id_number; }
    public String getMy_love() { return my_love; }
    public String getMy_other() { return my_other; }
    public void setAllow_detail(int allow_detail) { this.allow_detail = allow_detail; }
    public void setAllow_look_article(int allow_look_article) { this.allow_look_article = allow_look_article; }
    public void setAllow_look_suiji(int allow_look_suiji) { this.allow_look_suiji = allow_look_suiji; }
    public void setAllow_recommend(int allow_recommend) { this.allow_recommend = allow_recommend; }
    public void setId_number(String id_number) { this.id_number = id_number; }
    public void setMy_love(String my_love) { this.my_love = my_love; }
    public void setMy_other(String my_other) { this.my_other = my_other; }
    public void setRegister_date(Timestamp register_date) { this.register_date = register_date; }
    public void setLogin_date(Timestamp login_date) { this.login_date = login_date; }
    public void setStrange_login(int strange_login) { this.strange_login = strange_login; }
    public void setUser_article(int user_article) { this.user_article = user_article; }
    public void setUser_dislike(int user_dislike) { this.user_dislike = user_dislike; }
    public void setUser_eyi(int user_eyi) { this.user_eyi = user_eyi; }
    public void setUser_fans(int user_fans) { this.user_fans = user_fans; }
    public void setUser_focus(int user_focus) { this.user_focus = user_focus; }
    public void setUser_suiji(int user_suiji) { this.user_suiji = user_suiji; }
}
