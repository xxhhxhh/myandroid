package com.xxhhxhh.mainthing.databean;

import java.io.Serializable;

public class SuiJiCommentDataBean implements Serializable
{
    public SuiJiCommentDataBean(){}

    //评论标识
    private int suiji_comment_id;
    //评论者
    private String comment_username;
    //评论随记
    private int suiji_id;
    //评论内容
    private String comment_message;
    //恶意等级
    private int eyi_level;
    //赞数
    private int goods;
    //回复数量
    private int reply_number;
    //昵称
    private String nickName;
    //用户头像
    private String userHead;

    public String getUserHead() { return userHead; }
    public void setUserHead(String userHead) { this.userHead = userHead; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public int getReply_number() { return reply_number; }
    public void setReply_number(int reply_number) { this.reply_number = reply_number; }
    public int getEyi_level() { return eyi_level; }
    public int getGoods() { return goods; }
    public int getSuiji_comment_id() { return suiji_comment_id; }
    public String getComment_message() { return comment_message; }
    public void setComment_message(String comment_message) { this.comment_message = comment_message; }
    public void setEyi_level(int eyi_level) { this.eyi_level = eyi_level; }
    public void setGoods(int goods) { this.goods = goods; }
    public void setSuiji_comment_id(int suiji_comment_id) { this.suiji_comment_id = suiji_comment_id; }
    public int getSuiji_id() { return suiji_id; }
    public String getComment_username() { return comment_username; }
    public void setComment_username(String comment_username) { this.comment_username = comment_username; }
    public void setSuiji_id(int suiji_id) { this.suiji_id = suiji_id; }
}
