package com.xxhhxhh.mainthing.login_and_register.handler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.LoginAndRegisterActivity;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.mainthing.login_and_register.alertdialog.WriteIdentifyDialog;
import com.xxhhxhh.service.UserInfoIntentService;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
public class LoginAndRegisterThread extends Thread
{
    public LoginAndRegisterHandler loginAndRegisterHandler;

    private Context context;
    private String username;//可选，自动登录使用
    public LoginAndRegisterThread(Context context, String username)
    {
        this.context = context;
        this.username = username;
    }

    @Override
    public void run()
    {
        Looper.prepare();
        loginAndRegisterHandler = new LoginAndRegisterHandler(context);
        loginAndRegisterHandler.setUsername(username);
        //自动登录
        if(username != null)
        {
            loginAndRegisterHandler.sendEmptyMessage(LoginAndRegisterHandler.AUTO_LOGIN);
        }
        Looper.loop();
    }

    public static class LoginAndRegisterHandler extends Handler
    {
        public static final int PHONE_LOGIN = 123;//手机号登陆
        public static final int PASSWORD_LOGIN = 456;//密码登录
        public static final int SEND_TEXT = 789;//发送短信
        public static final int AUTO_LOGIN = 101112;//自动登录
        public String username;//用户名
        public String password;//密码
        public String sendText;//短信验证码
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public String getUsername() { return username; }
        public void setPassword(String password) { this.password = password; }
        public String getSendText() { return sendText; }
        public void setSendText(String sendText) { this.sendText = sendText; }
        private Context context;
        public void setContext(Context context) { this.context = context; }
        private AlertDialog alertDialog;
        public void setAlertDialog(AlertDialog alertDialog) { this.alertDialog = alertDialog; }

        public LoginAndRegisterHandler(Context context)
        {
            setContext(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg)
        {
            if(msg.what == PHONE_LOGIN)
            {
                phoneLogin();
            }
            else if(msg.what == PASSWORD_LOGIN)
            {
                passwordLogin();
            }
            else if(msg.what == SEND_TEXT)
            {
                phoneTextSend();
            }
            else if(msg.what == AUTO_LOGIN)
            {
                autoLogin();
            }
        }

        //自动登录
        public void autoLogin()
        {
            try
            {
                loginOnlyUsername(1);
            }
            catch (Exception E)
            {
                E.printStackTrace();
            }
            SQLiteDataBaseOpenHelper sqLiteDataBaseOpenHelper = new SQLiteDataBaseOpenHelper(context, MainActivity.databasePath
            , null, MainActivity.newDataBaseVersion);
            sqLiteDataBaseOpenHelper.getWritableDatabase().execSQL("update UserInfo set isallow_login=0 where username!='" + username + "';");
        }

        //用户名登录,type 1自动登录，0非自动
        public void loginOnlyUsername(int type) throws JSONException {
            //发送请求
            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.PHONE_LOGIN);
            String result = "";
            JSONObject jsonObject = new JSONObject();
            try
            {
                //注入数据
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                httpsUtil.setSendData(data);
                httpsUtil.sendPost();
                //获取返回结果
                result = httpsUtil.getResponse().body().string();
                jsonObject = new JSONObject(result);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                if(type != 1)
                {
                    alertDialog.dismiss();
                }
            }
            //返回登录类型和用户名
            if(!result.equals(""))
            {
                Intent intent = new Intent(context, UserInfoIntentService.class);
                try
                {
                    if(!jsonObject.getString("username").equals("null"))
                    {
                        intent.putExtra("username", jsonObject.getString("username"));
                        intent.putExtra("type", jsonObject.getInt("type"));
                        UserLogin.IS_LOGIN = true;
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                    if(type != 1)
                    {
                        alertDialog.dismiss();
                    }
                }
                //回到主activity
                backToMain(intent, type);

            }
            else
            {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                if(type != 1)
                {
                    alertDialog.dismiss();
                }
            }
        }

        //手机号登录
        public void phoneLogin()
        {
            if(getSendText() != null && getSendText().equals("1234"))
            {
                try
                {
                    loginOnlyUsername(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if(getSendText() == null)
            {
                Toast.makeText(context, "请先获取验证码", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
            else
            {
                Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }


        //密码登录
        public void passwordLogin()
        {
            //认证
            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.PASSWORD_LOGIN);
            Map<String, String> data = new HashMap<>();
            data.put("username", getUsername());
            data.put("password", getPassword());
            httpsUtil.setSendData(data);
            httpsUtil.sendPost();
            //
            Intent intent = new Intent(context, UserInfoIntentService.class);
            //返回结果
            try
            {
                String result = httpsUtil.getResponse().body().string();
                if(!result.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(result);
                    //得到用户名
                    setUsername(jsonObject.getString("username"));
                    intent.putExtra("username", getUsername());
                    UserLogin.IS_LOGIN = true;
                    backToMain(intent, 0);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }

        //发送短信
        public void phoneTextSend()
        {
            alertDialog.dismiss();
            setSendText("1234");
        }

        //返回主activity
        private void backToMain(Intent serviceIntent, int  type)
        {
//            Date date = new Date(System.currentTimeMillis());
            //数据库处理
//            SQLiteDataBaseOpenHelper sqLiteDataBaseOpenHelper = new SQLiteDataBaseOpenHelper(context, MainActivity.databasePath, null, MainActivity.newDataBaseVersion);
//            Cursor cursor = sqLiteDataBaseOpenHelper.getReadableDatabase().rawQuery("select username from UserInfo where username='" + username + "'", null);
//            if (cursor.moveToFirst())
//            {
//
//                sqLiteDataBaseOpenHelper.getWritableDatabase().execSQL("update UserInfo set login_date='" + date + "' " +
//                        " where username='" + username + "' and isallow_login = 1;");
//            }
//            else
//            {
//                MainActivity.loginType = 1;
//                sqLiteDataBaseOpenHelper.getWritableDatabase().execSQL("insert into UserInfo (username, login_date, isallow_login)" +
//                        " values('" + getUsername() + "', '" + date + "', 1);");
//            }
//            sqLiteDataBaseOpenHelper.close();
            context.startService(serviceIntent);
            if(type == 0)
            {
                //回到主activity
                if(alertDialog != null)
                {
                    alertDialog.dismiss();
                }
                ((LoginAndRegisterActivity)context).finish();
                ((LoginAndRegisterActivity) context).setResult(UserLogin.USER_IS_LOGIN);
            }
        }
    }

}

