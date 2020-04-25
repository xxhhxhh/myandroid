package com.xxhhxhh.database.article;

import javax.persistence.*;
import java.io.Serializable;

//文章标签表
@Entity
@Table(name = "ArticleLabels")
public class ArticleLabels implements Serializable
{
    public ArticleLabels(){}

    //标签名称
    @Id
    @Column(name = "label_name", unique = true, length = 5)
    private String labelName;

    //标签查看数量
    @Column(name = "label_looks_number", columnDefinition = "int default 0")
    private int labelLooksNumber;

    //标签发表数量
    @Column(name = "label_fabiao_number", columnDefinition = "int default 0")
    private int labelFaBiaoNumber;

    //标签推荐等级
    @Column(name = "label_level", columnDefinition = "int default 0")
    private int labelLevel;

    public int getLabelFaBiaoNumber() { return labelFaBiaoNumber; }
    public void setLabelFaBiaoNumber(int labelFaBiaoNumber) { this.labelFaBiaoNumber = labelFaBiaoNumber; }
    public int getLabelLevel() { return labelLevel; }
    public int getLabelLooksNumber() { return labelLooksNumber;}
    public String getLabelName() { return labelName; }
    public void setLabelLevel(int labelLevel) { this.labelLevel = labelLevel; }
    public void setLabelLooksNumber(int labelLooksNumber) { this.labelLooksNumber = labelLooksNumber; }
    public void setLabelName(String labelName) { this.labelName = labelName; }
}
