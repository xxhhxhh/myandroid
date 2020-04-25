package com.xxhhxhh.database.article;

import com.xxhhxhh.database.suiji.SuiJiComments;
import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;

//文字回复
@Entity
@Table(name = "ArticleReply")
public class ArticleReply implements Serializable
{

    public ArticleReply(){}

    //标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reply_id;

    //回复的评论
    @ManyToOne(targetEntity = ArticleComments.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_comment_id", nullable = false)
    private ArticleComments article_comment_id;

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

    public String getTo_username()
    {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public UserInfo getReply_username() { return reply_username; }
    public void setReply_username(UserInfo reply_username) { this.reply_username = reply_username; }
    public ArticleComments getArticle_comment_id() { return article_comment_id; }
    public int getReply_eyi() { return reply_eyi; }
    public int getReply_id() { return reply_id; }
    public String getReply_message() { return reply_message; }
    public void setArticle_comment_id(ArticleComments article_comment_id) { this.article_comment_id = article_comment_id; }
    public void setReply_eyi(int reply_eyi) { this.reply_eyi = reply_eyi; }
    public void setReply_id(int reply_id) { this.reply_id = reply_id; }
    public void setReply_message(String reply_message) { this.reply_message = reply_message; }
}
