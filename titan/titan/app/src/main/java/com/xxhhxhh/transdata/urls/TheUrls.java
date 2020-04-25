package com.xxhhxhh.transdata.urls;

import java.net.PortUnreachableException;

public class TheUrls
{
    /**
     * 此处存储url常量，上半部分用于本地测试
     * */
    //手机号登录注册
    public static final String PHONE_LOGIN = "http://10.0.2.2:8080/titan/phoneLogin";
    //密码登录
    public static final String PASSWORD_LOGIN = "http://10.0.2.2:8080/titan/passwordLogin";
    //用户自定义信息修改
    public static final String CUSTOM_SETTING = "http://10.0.2.2:8080/titan/setCustomUserInfo";
    //用户状态信息修改
    public static final String SWITCH_SETTING = "http://10.0.2.2:8080/titan/setSwitchUserInfo";
    //获取设置信息
    public static final String USER_SETTING = "http://10.0.2.2:8080/titan/getSetting";
    //保存标签查看信息
    public static final String SAVE_LABEL_LOOK = "http://10.0.2.2:8080/titan/saveLabelLook";
    //获取随记主要信息
    public static final String ONE_SUIJI_MAIN = "http://10.0.2.2:8080/titan/oneSuiJiMain";
    //获取随记文件
    public static final String ONE_MEDIA = "http://10.0.2.2:8080/titan/oneMedia";
    //获取完整文章信息
    public static final String ONE_ARTICLE = "http://10.0.2.2:8080/titan/oneArticle";
    //存储文章
    public static final String SAVE_ARTICLE = "http://10.0.2.2:8080/titan/saveArticle";
    //存储随记
    public static final String SAVE_SUIJI  = "http://10.0.2.2:8080/titan/saveSuiJi";
    //文章评论保存
    public static final String SAVE_ARTICLE_COMMENT_OR_REPLY = "http://10.0.2.2:8080/titan/saveArticleComment";
    //随记评论保存
    public static final String SAVE_SUIJI_COMMENT_OR_REPLY = "http://10.0.2.2:8080/titan/saveSuiJiComment";
    //文章评论获取
    public static final String GET_ARTICLE_COMMENT = "http://10.0.2.2:8080/titan/articleComment";
    //文章回复获取
    public static final String GET_ARTICLE_REPLY = "http://10.0.2.2:8080/titan/articleReply";
    //随记评论获取
    public static final String GET_SUIJI_COMMENT = "http://10.0.2.2:8080/titan/suijiComment";
    //随记回复获取
    public static final String GET_SUIJI_REPLY = "http://10.0.2.2:8080/titan/suijiReply";
    //修改手机
    public static final String SET_PHONE = "http://10.0.2.2:8080/titan/setPhone";
    //写入身份认证
    public static final String IDENTIFY_MESSAGE = "http://10.0.2.2:8080/titan/identifyDo";
    //密码修改
    public static final String SET_PASSWORD = "http://10.0.2.2:8080/titan/setPassword";
    //获取标签
    public static final String GET_LABELS = "http://10.0.2.2:8080/titan/getLabels";
    //保存用户关系
    public static final String SAVE_USER_RELATED = "http://10.0.2.2:8080/titan/userRelated";
    //获取用户关系
    public static final String GET_USER_RELATED = "http://10.0.2.2:8080/titan/getUserRelated";
    //点赞
    public static final String GIVE_GOOD = "http://10.0.2.2:8080/titan/giveGood";
    //举报
    public static final String TO_REPORT = "http://10.0.2.2:8080/titan/toReport";
    //获取头像
    public static final String GET_HEAD = "http://10.0.2.2:8080/titan/getHead";
    //存储头像
    public static final String SAVE_HEAD = "http://10.0.2.2:8080/titan/saveHead";
    //存储内容查看
    public static final String SAVE_LOOK = "http://10.0.2.2:8080/titan/saveLook";
    //结果查询
    public static final String RESULT_FIND = "http://10.0.2.2:8080/titan/search";
//
////    //真机
//    //手机号登录注册
//    public static final String PHONE_LOGIN = "http://www.xxhhxhh.cn:8080/titan/phoneLogin";
//    //密码登录
//    public static final String PASSWORD_LOGIN = "http://www.xxhhxhh.cn:8080/titan/passwordLogin";
//    //用户自定义信息修改
//    public static final String CUSTOM_SETTING = "http://www.xxhhxhh.cn:8080/titan/setCustomUserInfo";
//    //用户状态信息修改
//    public static final String SWITCH_SETTING = "http://www.xxhhxhh.cn:8080/titan/setSwitchUserInfo";
//    //获取设置信息
//    public static final String USER_SETTING = "http://www.xxhhxhh.cn:8080/titan/getSetting";
//    //保存标签查看信息
//    public static final String SAVE_LABEL_LOOK = "http://www.xxhhxhh.cn:8080/titan/saveLabelLook";
//    //获取随记主要信息
//    public static final String ONE_SUIJI_MAIN = "http://www.xxhhxhh.cn:8080/titan/oneSuiJiMain";
//    //获取随记文件
//    public static final String ONE_MEDIA = "http://www.xxhhxhh.cn:8080/titan/oneMedia";
//    //获取完整文章信息
//    public static final String ONE_ARTICLE = "http://www.xxhhxhh.cn:8080/titan/oneArticle";
//    //存储文章
//    public static final String SAVE_ARTICLE = "http://www.xxhhxhh.cn:8080/titan/saveArticle";
//    //存储随记
//    public static final String SAVE_SUIJI  = "http://www.xxhhxhh.cn:8080/titan/saveSuiJi";
//    //文章评论保存
//    public static final String SAVE_ARTICLE_COMMENT_OR_REPLY = "http://www.xxhhxhh.cn:8080/titan/saveArticleComment";
//    //随记评论保存
//    public static final String SAVE_SUIJI_COMMENT_OR_REPLY = "http://www.xxhhxhh.cn:8080/titan/saveSuiJiComment";
//    //文章评论获取
//    public static final String GET_ARTICLE_COMMENT = "http://www.xxhhxhh.cn:8080/titan/articleComment";
//    //文章回复获取
//    public static final String GET_ARTICLE_REPLY = "http://www.xxhhxhh.cn:8080/titan/articleReply";
//    //随记评论获取
//    public static final String GET_SUIJI_COMMENT = "http://www.xxhhxhh.cn:8080/titan/suijiComment";
//    //随记回复获取
//    public static final String GET_SUIJI_REPLY = "http://www.xxhhxhh.cn:8080/titan/suijiReply";
//    //修改手机
//    public static final String SET_PHONE = "http://www.xxhhxhh.cn:8080/titan/setPhone";
//    //写入身份认证
//    public static final String IDENTIFY_MESSAGE = "http://www.xxhhxhh.cn:8080/titan/identifyDo";
//    //密码修改
//    public static final String SET_PASSWORD = "http://www.xxhhxhh.cn:8080/titan/setPassword";
//    //获取标签
//    public static final String GET_LABELS = "http://www.xxhhxhh.cn:8080/titan/getLabels";
//    //保存用户关系
//    public static final String SAVE_USER_RELATED = "http://www.xxhhxhh.cn:8080/titan/userRelated";
//    //获取用户关系
//    public static final String GET_USER_RELATED = "http://www.xxhhxhh.cn:8080/titan/getUserRelated";
//    //点赞
//    public static final String GIVE_GOOD = "http://www.xxhhxhh.cn:8080/titan/giveGood";
//    //举报
//    public static final String TO_REPORT = "http://www.xxhhxhh.cn:8080/titan/toReport";
//    //获取头像
//    public static final String GET_HEAD = "http://www.xxhhxhh.cn:8080/titan/getHead";
//    //存储头像
//    public static final String SAVE_HEAD = "http://www.xxhhxhh.cn:8080/titan/saveHead";
//    //存储内容查看
//    public static final String SAVE_LOOK = "http://www.xxhhxhh.cn:8080/titan/saveLook";
//    //结果查询
//    public static final String RESULT_FIND = "http://www.xxhhxhh.cn:8080/titan/search";
}
