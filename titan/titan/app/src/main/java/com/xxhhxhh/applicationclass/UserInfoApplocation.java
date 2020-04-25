package com.xxhhxhh.applicationclass;

import android.app.Application;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.whenstart.UserLogin;

public class UserInfoApplocation extends Application
{
    private String myLove;
    private  String myOther;
    private String nickName;
    private  int allowDetail;
    private  int allowRecommend;
    private  int allowLookSuiJi;
    private int allowLookArticle;
    private int starngeLogin;
    private String username;
    private String realName;
    private  String idNumber;
    private  int loginType;//0登录、1注册、2失败
    public void setAllowDetail(int allowDetail) { this.allowDetail = allowDetail; }
    public void setAllowLookArticle(int allowLookArticle) { this.allowLookArticle = allowLookArticle; }
    public void setAllowLookSuiJi(int allowLookSuiJi) { this.allowLookSuiJi = allowLookSuiJi; }
    public void setAllowRecommend(int allowRecommend) { this.allowRecommend = allowRecommend; }
    public void setMyLove(String myLove) { this.myLove = myLove; }
    public void setMyOther(String myOther) { this.myOther = myOther; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public void setStarngeLogin(int starngeLogin) { this.starngeLogin = starngeLogin; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public void setUsername(String username) { this.username = username; }
    public String getMyOther() { return myOther; }
    public String getIdNumber() { return idNumber; }
    public String getRealName() { return realName; }
    public String getNickName() { return nickName; }
    public String getMyLove() { return myLove; }
    public int getStarngeLogin() { return starngeLogin; }
    public int getAllowRecommend() { return allowRecommend; }
    public int getAllowLookSuiJi() { return allowLookSuiJi; }
    public int getAllowLookArticle() { return allowLookArticle; }
    public int getAllowDetail() { return allowDetail; }
    public String getUsername() { return username; }
    public int getLoginType() { return loginType; }
    public void setLoginType(int loginType) { this.loginType = loginType; }
    public void setRealName(String realName) { this.realName = realName; }

    //退出登录
    public void exit()
    {
        UserLogin.IS_LOGIN = false;
        SQLiteDataBaseOpenHelper sqLiteDataBaseOpenHelper = new SQLiteDataBaseOpenHelper(getApplicationContext(), MainActivity.databasePath
        , null, MainActivity.newDataBaseVersion);
        //自动登录关闭
        sqLiteDataBaseOpenHelper.getWritableDatabase().execSQL("update UserInfo set isallow_login=0 where username='" + getUsername() +"' ;");
        setUsername("");
    }
}
