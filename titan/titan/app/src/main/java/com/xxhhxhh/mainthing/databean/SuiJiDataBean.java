package com.xxhhxhh.mainthing.databean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class SuiJiDataBean
{
    public SuiJiDataBean(){}

    //标识
    private int suiji_id;
    //用户名
    private String username;
    //随记文本
    private String main_message;
    //发表地点
    private String location;
    //是否有文件,文件数量
    private int have_file;
    //评论数量
    private int comment_number;
    //查看数量
    private int looks_number;
    //赞数
    private int goods;
    //发表时间
    private String the_date;
    //标签
    private String labels;
    //文件类型
    private int fileType;
    //bitmap
    private Map<Integer, Bitmap> bitmapMap;
    //视频文件路径
    //用户头像
    private String userHead;
    //昵称
    private String nickName;

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public String getUserHead() { return userHead; }
    public void setUserHead(String userHead) { this.userHead = userHead; }
    public Map<Integer, Bitmap> getBitmapMap() { return bitmapMap; }
    public void setBitmapMap(Map<Integer, Bitmap> bitmapMap) { this.bitmapMap = bitmapMap; }
    public int getFileType() { return fileType; }
    public void setFileType(int fileType) { this.fileType = fileType; }
    public void setLabels(String labels) { this.labels = labels; }
    public JSONArray getLabels()  {
        try
        {
            return new JSONArray(labels);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new JSONArray();
    }
    public void setThe_date(String the_date) { this.the_date = the_date; }
    public String getThe_date() { return the_date; }
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public void setGoods(int goods) { this.goods = goods; }
    public int getGoods() { return goods; }
    public int getLooks_number() { return looks_number; }
    public void setLooks_number(int looks_number) { this.looks_number = looks_number; }
    public int getComment_number() { return comment_number; }
    public void setComment_number(int comment_number) { this.comment_number = comment_number; }
    public int getHave_file() { return have_file; }
    public int getSuiji_id() { return suiji_id; }
    public String getLocation() { return location; }
    public String getMain_message() { return main_message; }
    public void setHave_file(int have_file) { this.have_file = have_file; }
    public void setLocation(String location) { this.location = location; }
    public void setMain_message(String main_message) { this.main_message = main_message; }
    public void setSuiji_id(int suiji_id) { this.suiji_id = suiji_id; }



}
