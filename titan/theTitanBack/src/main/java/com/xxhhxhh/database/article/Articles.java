package com.xxhhxhh.database.article;

import com.xxhhxhh.database.user.UserInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

//文章
@Entity
@Table(name = "Articles")
public class Articles implements Serializable
{
    public Articles(){}

    //文章标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int article_id;

    @ManyToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserInfo username;

    //文章标题
    @Column(length = 50)
    private String article_title;

    //发表地点
    @Column(length = 25)
    private String location;

    //评论数量
    @Column(columnDefinition = "int default 0")
    private int comment_number;

    //文章url
    @Column(unique = true)
    private String url;

    //查看次数
    @Column(columnDefinition = "int default 0")
    private int looks_number;

    //赞数
    @Column(columnDefinition = "int default 0")
    private int goods;

    //发表日期
    @Column(columnDefinition = "timestamp default NOW()")
    private Timestamp the_date;

    //文件位置
    @Column(nullable = false, unique = true)
    private String file_location;

    public void setFile_location(String file_location) { this.file_location = file_location; }
    public String getFile_location() { return file_location; }
    public Timestamp getThe_date() { return the_date; }
    public void setThe_date(Timestamp the_date) { this.the_date = the_date; }
    public void setGoods(int goods) { this.goods = goods; }
    public int getGoods() { return goods; }
    public int getLooks_number() { return looks_number; }
    public void setLooks_number(int looks_number) { this.looks_number = looks_number; }
    public int getComment_number() { return comment_number; }
    public void setComment_number(int comment_number) { this.comment_number = comment_number; }
    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return location; }
    public int getArticle_id() { return article_id; }
    public String getArticle_title() { return article_title; }
    public void setArticle_id(int article_id) { this.article_id = article_id; }
    public void setArticle_title(String article_title) { this.article_title = article_title; }
    public void setUsername(UserInfo username) { this.username = username; }
    public UserInfo getUsername() { return username; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
