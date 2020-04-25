package com.xxhhxhh.database.article;

import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

//文章评论
@Entity
@Table(name = "ArticleComments")
public class ArticleComments implements Serializable
{
    public ArticleComments(){}

    //评论标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int article_comment_id;
    //评论者
    @ManyToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserInfo comment_username;
    //评论随记
    @ManyToOne(targetEntity = Articles.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Articles article_id;
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
    public void setEyi_level(int eyi_level) { this.eyi_level = eyi_level; }
    public void setGoods(int goods) { this.goods = goods; }
    public void setComment_message(String comment_message) { this.comment_message = comment_message; }
    public UserInfo getComment_username() { return comment_username; }
    public void setComment_username(UserInfo comment_username) { this.comment_username = comment_username; }
    public String getComment_message() { return comment_message; }
    public int getGoods() { return goods; }
    public int getEyi_level() { return eyi_level; }
    public void setArticle_id(Articles article_id) { this.article_id = article_id; }
    public Articles getArticle_id() { return article_id; }
    public int getArticle_comment_id() { return article_comment_id; }
    public void setArticle_comment_id(int article_comment_id) { this.article_comment_id = article_comment_id; }
}
