package com.xxhhxhh.mainthing.databean;

import org.json.JSONArray;

import java.io.Serializable;

public class ArticleDataBean implements Serializable
{

    //文章标识
    private int article_id;

    //用户名
    private String username;

    //文章标题
    private String article_title;

    //发表地点
    private String location;

    //评论数量
    private int comment_number;

    //文章url
    private String url;

    //查看次数
    private int looks_number;

    //赞数
    private int goods;

    //发表日期
    private String the_date;

    //标签
    private String labels;

    //昵称
    private String nickName;

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public void setLabels(JSONArray labels) { this.labels = labels.toString(); }
    public String getLabels() { return labels; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getArticle_id() { return article_id; }
    public int getGoods() { return goods; }
    public void setArticle_id(int article_id) { this.article_id = article_id; }
    public void setGoods(int goods) { this.goods = goods; }
    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return location; }
    public int getLooks_number() { return looks_number; }
    public String getUrl() { return url; }
    public int getComment_number() { return comment_number; }
    public String getArticle_title() { return article_title; }
    public String getThe_date() { return the_date; }
    public void setArticle_title(String article_title) { this.article_title = article_title; }
    public void setComment_number(int comment_number) { this.comment_number = comment_number; }
    public void setLooks_number(int looks_number) { this.looks_number = looks_number; }
    public void setThe_date(String the_date) { this.the_date = the_date; }
    public void setUrl(String url) { this.url = url; }

}
