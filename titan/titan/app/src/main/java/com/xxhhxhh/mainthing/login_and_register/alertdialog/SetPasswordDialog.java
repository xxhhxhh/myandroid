package com.xxhhxhh.mainthing.login_and_register.alertdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class SetPasswordDialog extends AlertDialog.Builder
    implements DialogInterface.OnClickListener
{
    private Context context;
    private String username;
    private LinearLayout mainView;//视图
    public SetPasswordDialog(Context context, String username)
    {
        super(context);

        mainView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.set_password_dialog, null);
        setView(mainView);
        setPositiveButton("确定", this);
        setNegativeButton("取消", this);
        setTitle("设置密码");
        this.context = context;
        this.username = username;
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        //设置密码
        String password = ((EditText)mainView.findViewById(R.id.password)).getText().toString();
        String rePassword = ((EditText)mainView.findViewById(R.id.rePassword)).getText().toString();
        if(which == Dialog.BUTTON_POSITIVE && password.equals(rePassword))
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    //设置身份认证信息
                    HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SET_PASSWORD);
                    Map<String, String> data = new HashMap<>();
                    data.put("password", password);
                    data.put("username", username);
                    httpsUtil.setSendData(data);
                    httpsUtil.sendPost();
                }
            }.start();
            dialog.dismiss();
        }
        else if(which == Dialog.BUTTON_NEGATIVE)
        {
            dialog.dismiss();
        }
    }
}
