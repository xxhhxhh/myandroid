package com.xxhhxhh.database.suiji;

import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

//随记评论表
@Entity
@Table(name = "SuiJiComments")
public class SuiJiComments implements Serializable
{
    public SuiJiComments(){}

    //评论标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int suiji_comment_id;
    //评论者
    @ManyToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserInfo comment_username;
    //评论随记
    @ManyToOne(targetEntity = SuiJis.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "suiji_id", nullable = false)
    private SuiJis suiji_id;
    //评论内容
    @Column(length = 100)
    private String comment_message;
    //恶意等级
    @Column(columnDefinition = "int default 0")
    private int eyi_level;
    //赞数
    @Column(columnDefinition = "int default 0")
    private int goods;
    //回复数量
    @Column(columnDefinition = "int default 0")
    private int reply_number;

    public int getReply_number() { return reply_number; }
    public void setReply_number(int reply_number) { this.reply_number = reply_number; }
    public SuiJis getSuiji_id() { return suiji_id; }
    public int getEyi_level() { return eyi_level; }
    public int getGoods() { return goods; }
    public int getSuiji_comment_id() { return suiji_comment_id; }
    public String getComment_message() { return comment_message; }
    public void setComment_message(String comment_message) { this.comment_message = comment_message; }
    public UserInfo getComment_username() { return comment_username; }
    public void setComment_username(UserInfo comment_username) { this.comment_username = comment_username; }
    public void setEyi_level(int eyi_level) { this.eyi_level = eyi_level; }
    public void setGoods(int goods) { this.goods = goods; }
    public void setSuiji_comment_id(int suiji_comment_id) { this.suiji_comment_id = suiji_comment_id; }
    public void setSuiji_id(SuiJis suiji_id) { this.suiji_id = suiji_id; }
}
