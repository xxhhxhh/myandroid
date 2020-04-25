package com.xxhhxhh.interfaces;

public interface GetMessageShow
{
    public void getMessageRecommend();//推荐
    public void getMessageHot();//热门
    public void getMessageLatest();//最新
    public void getMessageWithLabel(String label);//按标签获取
    public void getMessageForUser(String username);//按用户获取
    public void getMessageSingleSearch(int id);//查询指定
}
