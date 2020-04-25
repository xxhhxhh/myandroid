package com.xxhhxhh.database.suiji;

import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

//随记回复
@Entity
@Table(name = "SuiJiReply")
public class SuiJiReply implements Serializable
{
    public SuiJiReply(){}

    //标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reply_id;

    //回复的评论
    @ManyToOne(targetEntity = SuiJiComments.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "suiji_comment_id", nullable = false)
    private SuiJiComments suiji_comment_id;

    //回复者
    @ManyToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserInfo reply_username;

    //恶意等级
    @Column(columnDefinition = "int default 0")
    private int reply_eyi;

    //回复内容
    @Column(length = 100)
    private String reply_message;

    //被回复者
    @Column(length = 15, nullable = false)
    private String to_username;

    public String getTo_username() {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public void setReply_id(int reply_id) { this.reply_id = reply_id; }
    public void setReply_eyi(int reply_eyi) { this.reply_eyi = reply_eyi; }
    public String getReply_message() { return reply_message; }
    public int getReply_id() { return reply_id; }
    public int getReply_eyi() { return reply_eyi; }
    public UserInfo getReply_username() { return reply_username; }
    public void setReply_username(UserInfo reply_username) { this.reply_username = reply_username; }
    public void setSuiji_comment_id(SuiJiComments suiji_comment_id) { this.suiji_comment_id = suiji_comment_id; }
    public SuiJiComments getSuiji_comment_id() { return suiji_comment_id; }
    public void setReply_message(String reply_message) { this.reply_message = reply_message; }
}
