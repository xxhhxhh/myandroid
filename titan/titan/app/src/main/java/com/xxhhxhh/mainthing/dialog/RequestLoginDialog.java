package com.xxhhxhh.mainthing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.xxhhxhh.whenstart.UserLogin;

public class RequestLoginDialog extends AlertDialog.Builder
    implements DialogInterface.OnClickListener
{
    public RequestLoginDialog(Context context)
    {
        super(context);
        setTitle("登录请求");
        setMessage("请先登录");
        setPositiveButton("进入登录界面", this);
        setNegativeButton("取消", this);
    }


    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if(which == AlertDialog.BUTTON_POSITIVE)
        {
            UserLogin.toLogin(getContext());
        }
    }
}
