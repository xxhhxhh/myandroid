package com.xxhhxhh.mainthing.login_and_register.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class WriteIdentifyDialog extends AlertDialog.Builder
{
    //真实姓名
    private EditText realName;
    //身份证号
    private EditText idNumber;
    //用户名
    private String username;
    private UserInfoApplocation userInfoApplocation;
    public EditText getIdNumber() { return idNumber; }
    public EditText getRealName() { return realName; }

    public WriteIdentifyDialog(Context context, String username)
    {
        super(context);
        init();
        this.username = username;
        setTitle("身份认证");
        setPositiveButton("确定", (dialog, which) ->
        {
            if(which == Dialog.BUTTON_POSITIVE)
            {
                //判空
                String idNumberString = getIdNumber().getText().toString();
                String realNameString = getRealName().getText().toString();
                if (!idNumberString.equals("") && !realNameString.equals(""))
                {
                    new Thread() {
                        @Override
                        public void run() {
                            //设置身份认证信息
                            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.IDENTIFY_MESSAGE);
                            Map<String, String> data = new HashMap<>();
                            data.put("id_number", idNumberString);
                            data.put("real_name", realNameString);
                            data.put("username", username);
                            httpsUtil.setSendData(data);
                            httpsUtil.sendPost();
                        }}.start();
                    dialog.dismiss();
                }

                SetPasswordDialog setPasswordDialog = new SetPasswordDialog(context, username);
                setPasswordDialog.show();
        }
    });
    }

    //初始化
    public void init()
    {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.write_identify, null);
        setView(linearLayout);
        realName = (EditText)linearLayout.findViewById(R.id.writeRealName);
        idNumber = (EditText)linearLayout.findViewById(R.id.writeIdNumber);
        setPositiveButton("确定", null);
   }




}
