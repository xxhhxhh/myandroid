package com.xxhhxhh.database.user;

import com.xxhhxhh.database.article.ArticleLabels;
import com.xxhhxhh.database.suiji.SuiJiLabels;

import javax.persistence.*;
import java.io.Serializable;

//用户标签表
@Entity
@Table(name = "UserLabels")
public class UserLabels implements Serializable
{
    public UserLabels(){}

    @Id
    @OneToOne(targetEntity = UserInfo.class)
    @JoinColumn(name = "username", referencedColumnName = "username", unique = true)
    private UserInfo username;
    //三个等级随记喜爱标签
    @OneToOne(targetEntity = SuiJiLabels.class)
    @JoinColumn(name = "suiji_love1", referencedColumnName = "label_name")
    private SuiJiLabels suiji_love1;
    @OneToOne(targetEntity = SuiJiLabels.class)
    @JoinColumn(name = "suiji_love2", referencedColumnName = "label_name")
    private SuiJiLabels suiji_love2;
    @OneToOne(targetEntity = SuiJiLabels.class)
    @JoinColumn(name = "suiji_love3", referencedColumnName = "label_name")
    private SuiJiLabels suiji_love3;
    //三个等级的文章喜爱标签
    @OneToOne(targetEntity = ArticleLabels.class)
    @JoinColumn(name = "article_love1", referencedColumnName = "label_name")
    private ArticleLabels article_love1;
    @OneToOne(targetEntity = ArticleLabels.class)
    @JoinColumn(name = "article_love2", referencedColumnName = "label_name")
    private ArticleLabels article_love2;
    @OneToOne(targetEntity = ArticleLabels.class)
    @JoinColumn(name = "article_love3", referencedColumnName = "label_name")
    private ArticleLabels article_love3;

    public void setUsername(UserInfo username) { this.username = username; }
    public SuiJiLabels getSuiji_love1() { return suiji_love1; }
    public UserInfo getUsername() { return username; }
    public void setSuiji_love1(SuiJiLabels suiji_love1) { this.suiji_love1 = suiji_love1; }
    public SuiJiLabels getSuiji_love2() { return suiji_love2; }
    public void setSuiji_love2(SuiJiLabels suiji_love2) { this.suiji_love2 = suiji_love2; }
    public ArticleLabels getArticle_love1() { return article_love1; }
    public ArticleLabels getArticle_love2() { return article_love2; }
    public ArticleLabels getArticle_love3() { return article_love3; }
    public SuiJiLabels getSuiji_love3() { return suiji_love3; }
    public void setArticle_love1(ArticleLabels article_love1) { this.article_love1 = article_love1; }
    public void setArticle_love2(ArticleLabels article_love2) { this.article_love2 = article_love2; }
    public void setArticle_love3(ArticleLabels article_love3) { this.article_love3 = article_love3; }
    public void setSuiji_love3(SuiJiLabels suiji_love3) { this.suiji_love3 = suiji_love3; }
}
