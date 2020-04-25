package com.xxhhxhh.whenstart;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import com.xxhhxhh.mainthing.LoginAndRegisterActivity;
import com.xxhhxhh.mainthing.dialog.RequestLoginDialog;
import com.xxhhxhh.transdata.requestcode.RequestCode;

public class UserLogin {

    public static boolean IS_LOGIN = false;//登录成功识别
    public static final int USER_IS_LOGIN = 4;//登录返回码

    //进入登录
    public  static void toLogin(Context context)
    {
        while(context instanceof ContextWrapper && !(context instanceof Activity))
        {
            context = ((ContextWrapper)context).getBaseContext();
        }
        Activity activity = (Activity)context;
        Intent intent = new Intent(activity.getBaseContext(), LoginAndRegisterActivity.class);
        activity.startActivityForResult(intent, RequestCode.USER_WANT_LOGIN);
    }

    //请求登录
    public static void requestLogin(Context context)
    {
        if(!IS_LOGIN)
        {
            RequestLoginDialog requestLoginDialog = new RequestLoginDialog(context);
            requestLoginDialog.show();
        }
    }

    //获取自动登录信息
    public static void getLogin(Context context)
    {
        //自动登录
        if(IS_LOGIN)
        {

        }
        else
        {
            requestLogin(context);
        }
    }
}
