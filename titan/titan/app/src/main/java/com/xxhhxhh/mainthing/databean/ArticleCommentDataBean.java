package com.xxhhxhh.mainthing.databean;

import java.io.Serializable;

public class ArticleCommentDataBean implements Serializable
{

    public ArticleCommentDataBean(){}

    //评论标识
    private int article_comment_id;
    //评论者
    private String comment_username;
    //评论随记
    private int article_id;
    //评论内容
    private String comment_message;
    //恶意等级
    private int eyi_level;
    //赞数
    private int goods;
    //是否被查询过
    private int is_searched;
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
    public int getIs_searched() { return is_searched; }
    public void setIs_searched(int is_searched) { this.is_searched = is_searched; }
    public void setEyi_level(int eyi_level) { this.eyi_level = eyi_level; }
    public void setGoods(int goods) { this.goods = goods; }
    public void setComment_message(String comment_message) { this.comment_message = comment_message; }
    public String getComment_message() { return comment_message; }
    public int getGoods() { return goods; }
    public int getEyi_level() { return eyi_level; }
    public int getArticle_comment_id() { return article_comment_id; }
    public void setArticle_comment_id(int article_comment_id) { this.article_comment_id = article_comment_id; }
    public void setComment_username(String comment_username) { this.comment_username = comment_username; }
    public String getComment_username() { return comment_username; }
    public void setArticle_id(int article_id) { this.article_id = article_id; }
    public int getArticle_id() { return article_id; }
}
