package com.xxhhxhh.database.suiji;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EverySuiJiLabels")
public class EverySuiJiLabels implements Serializable
{
    public EverySuiJiLabels(){}

    @Id
    @ManyToOne(targetEntity = SuiJis.class)
    @JoinColumn(name = "suiji_id")
    private int suiji_id;

    @Id
    @ManyToOne(targetEntity = SuiJiLabels.class)
    @JoinColumn(name = "label_name")
    private SuiJiLabels label_name;


    public int getSuiji_id() { return suiji_id; }
    public SuiJiLabels getLabel_name() { return label_name; }
    public void setLabel_name(SuiJiLabels label_name) { this.label_name = label_name; }
    public void setSuiji_id(int suiji_id) { this.suiji_id = suiji_id; }
}
