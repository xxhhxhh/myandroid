package com.xxhhxhh.titanback.user.infoget;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GetUserSettingAction extends ActionSupport
{
    private String myLove;
    private String myOther;
    private String nickName;
    private int allowDetail;
    private int allowRecommend;
    private int allowLookSuiJi;
    private int allowLookArticle;
    private int strangeLogin;
    private String realName;
    private String idNumber;

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public int getAllowDetail() { return allowDetail; }
    public int getAllowLookArticle() { return allowLookArticle; }
    public int getAllowLookSuiJi() { return allowLookSuiJi; }
    public int getAllowRecommend() { return allowRecommend; }
    public String getMyLove() { return myLove; }
    public String getMyOther() { return myOther; }
    public String getNickName() { return nickName; }
    public void setAllowDetail(int allowDetail) { this.allowDetail = allowDetail; }
    public void setAllowLookArticle(int allowLookArticle) { this.allowLookArticle = allowLookArticle; }
    public void setAllowLookSuiJi(int allowLookSuiJi) { this.allowLookSuiJi = allowLookSuiJi; }
    public void setAllowRecommend(int allowRecommend) { this.allowRecommend = allowRecommend; }
    public void setMyLove(String myLove) { this.myLove = myLove; }
    public void setMyOther(String myOther) { this.myOther = myOther; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public int getStrangeLogin() { return strangeLogin; }
    public void setStrangeLogin(int strangeLogin) { this.strangeLogin = strangeLogin; }


    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserInfo userInfo = UserInfoDoUtil.findCustomUserInfo( servletRequest.getParameter("username"), session);
        //结果不为空，确值
        if(userInfo != null)
        {
            setAllowDetail(userInfo.getAllow_detail());
            setAllowLookArticle(userInfo.getAllow_look_article());
            setAllowLookSuiJi(userInfo.getAllow_look_suiji());
            setAllowRecommend(userInfo.getAllow_recommend());
            setMyLove(userInfo.getMy_love());
            setMyOther(userInfo.getMy_other());
            setNickName(userInfo.getNick_name());
            setStrangeLogin(userInfo.getStrange_login());
            setRealName(userInfo.getReal_name());
            setIdNumber(userInfo.getId_number());
            return SUCCESS;
        }

        return ERROR;
    }
}
