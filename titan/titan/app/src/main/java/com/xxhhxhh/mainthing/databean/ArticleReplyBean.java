package com.xxhhxhh.mainthing.databean;

import java.io.Serializable;

public class ArticleReplyBean
{

    //标识
    private int reply_id;

    //回复的评论
    private int article_comment_id;

    //回复者
    private String reply_username;

    //恶意等级
    private int reply_eyi;

    //回复内容
    private String reply_message;
    //被回复者
    private String to_username;
    //昵称
    private String nickName;
    //被回复者昵称
    private String toNickName;

    public String getToNickName() { return toNickName; }
    public void setToNickName(String toNickName) { this.toNickName = toNickName; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }
    public String getTo_username() {
        return to_username;
    }
    public int getReply_eyi() { return reply_eyi; }
    public int getReply_id() { return reply_id; }
    public String getReply_message() { return reply_message; }
    public void setReply_eyi(int reply_eyi) { this.reply_eyi = reply_eyi; }
    public void setReply_id(int reply_id) { this.reply_id = reply_id; }
    public void setReply_message(String reply_message) { this.reply_message = reply_message; }
    public String getReply_username() { return reply_username; }
    public void setArticle_comment_id(int article_comment_id) { this.article_comment_id = article_comment_id; }
    public int getArticle_comment_id() { return article_comment_id; }
    public void setReply_username(String reply_username) { this.reply_username = reply_username; }
}
