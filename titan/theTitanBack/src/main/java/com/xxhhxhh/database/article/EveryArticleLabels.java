package com.xxhhxhh.database.article;


import javax.persistence.*;
import java.io.Serializable;

//每一个文章对应的标签
@Entity
@Table(name = "EveryArticleLabels")
public class EveryArticleLabels implements Serializable
{
    public EveryArticleLabels(){}

    @Id
    @ManyToOne(targetEntity = ArticleLabels.class)
    @JoinColumn(name = "label_name")
    private ArticleLabels label_name;

    @Id
    @ManyToOne(targetEntity = Articles.class)
    @JoinColumn(name = "article_id")
    private int article_id;

    public ArticleLabels getLabel_name() { return label_name; }
    public void setLabel_name(ArticleLabels label_name) { this.label_name = label_name; }
    public void setArticle_id(int article_id) { this.article_id = article_id; }
    public int getArticle_id() { return article_id; }
}
