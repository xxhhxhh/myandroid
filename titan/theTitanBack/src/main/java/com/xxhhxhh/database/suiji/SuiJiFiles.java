package com.xxhhxhh.database.suiji;

import javax.persistence.*;
import java.io.Serializable;

//随记文件表
@Entity
@Table(name = "SuiJiFiles")
public class SuiJiFiles implements Serializable
{
    public SuiJiFiles(){}

    //文件位置
    @Id
    private String file_location;
    //随记id
    @ManyToOne(targetEntity = SuiJis.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "suiji_id", nullable = false)
    private SuiJis suiji_id;
    //一组图片的中的图片位置
    @Column
    private int group_id;

    public String getFile_location() { return file_location; }
    public void setFile_location(String file_location) { this.file_location = file_location; }
    public SuiJis getSuiji_id() { return suiji_id; }
    public void setSuiji_id(SuiJis suiji_id) { this.suiji_id = suiji_id; }
    public int getGroup_id() { return group_id; }
    public void setGroup_id(int group_id) { this.group_id = group_id; }
}
