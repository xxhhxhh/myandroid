package com.xxhhxhh.database.suiji;


import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//发表内容表
@Entity
@Table(name = "SuiJis")
public class SuiJis implements Serializable
{
    public SuiJis(){}

    //标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int suiji_id;
    //用户名
    @ManyToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserInfo username;
    //随记文本
    @Column(length = 160)
    private String main_message;
    //发表地点
    @Column(length = 50)
    private String location;
    //是否有文件
    @Column(columnDefinition = "int default 0")
    private int have_file;
    //评论数量
    @Column(columnDefinition = "int default 0")
    private int comment_number;
    //查看数量
    @Column(columnDefinition = "int default 0")
    private int looks_number;
    //赞数
    @Column(columnDefinition = "int default 0")
    private int goods;
    //发表时间
    @Column(columnDefinition = "timestamp default NOW()")
    private Timestamp the_date;
    //文件类型,0图片，1视频
    @Column(columnDefinition = "int default 0")
    private int file_type;

    public int getFile_type() { return file_type; }
    public void setFile_type(int file_type) { this.file_type = file_type; }
    public Timestamp getThe_date() { return the_date; }
    public void setThe_date(Timestamp the_date) { this.the_date = the_date; }
    public void setGoods(int goods) { this.goods = goods; }
    public int getGoods() { return goods; }
    public int getLooks_number() { return looks_number; }
    public void setLooks_number(int looks_number) { this.looks_number = looks_number; }
    public int getComment_number() { return comment_number; }
    public void setComment_number(int comment_number) { this.comment_number = comment_number; }
    public void setUsername(UserInfo username) { this.username = username; }
    public UserInfo getUsername() { return username; }
    public int getHave_file() { return have_file; }
    public int getSuiji_id() { return suiji_id; }
    public String getLocation() { return location; }
    public String getMain_message() { return main_message; }
    public void setHave_file(int have_file) { this.have_file = have_file; }
    public void setLocation(String location) { this.location = location; }
    public void setMain_message(String main_message) { this.main_message = main_message; }
    public void setSuiji_id(int suiji_id) { this.suiji_id = suiji_id; }
}
