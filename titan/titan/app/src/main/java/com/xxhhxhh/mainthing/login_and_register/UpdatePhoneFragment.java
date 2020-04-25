package com.xxhhxhh.mainthing.login_and_register;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.mainthing.login_and_register.handler.LoginAndRegisterThread;
import com.xxhhxhh.mainthing.login_and_register.timer.CountDownReceviceTime;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePhoneFragment extends Fragment
    implements View.OnClickListener
{

    private ViewPager login_register_switch;//页面切换
    private View rootView;
    private EditText oldPhone;//旧手机号
    private EditText newPhone;//新手机号
    private EditText realName;//真实姓名
    private EditText idNumber;//身份证号
    private EditText inputYanZhengma;//验证码
    private int mode;//返回模式
    private Button reGet;
    private UserInfoApplocation userInfoApplocation;
    private AlertDialog alertDialog;//验证对话框
    private LoginAndRegisterThread loginAndRegisterThread;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.update_phone, null);
        userInfoApplocation = (UserInfoApplocation) getActivity().getApplication();
        alertDialog = new AlertDialog.Builder(getContext()).setTitle("验证中").setView(
                LayoutInflater.from(getContext()).inflate(R.layout.yanzheng_dialog, null)).create();
        loginAndRegisterThread = new LoginAndRegisterThread(getContext(), null);
        loginAndRegisterThread.start();
        init();
        return rootView;
    }

    private void init()
    {
        mode = getActivity().getIntent().getIntExtra("backTo", 0);
        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);

        Button confirm = (Button)rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        reGet = (Button)rootView.findViewById(R.id.reGet);
        reGet.setText("获取");
        reGet.setOnClickListener(this);

        oldPhone = (EditText)rootView.findViewById(R.id.username);
        newPhone = (EditText)rootView.findViewById(R.id.newPhone);
        realName = (EditText)rootView.findViewById(R.id.realName);
        idNumber = (EditText)rootView.findViewById(R.id.idNumber);
        inputYanZhengma = (EditText)rootView.findViewById(R.id.inputYanZhengMa);

        login_register_switch = (ViewPager)getActivity().findViewById(R.id.login_register_switch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                if(mode == 11)
                {
                    getActivity().finish();
                }
                else if(mode == 0)
                {
                    login_register_switch.setCurrentItem(0);
                }
            }break;
            case R.id.confirm:
            {
                alertDialog.show();
                //判断
                if(!oldPhone.getText().toString().equals(""))
                {
                    if(!idNumber.getText().toString().equals(""))
                    {
                        if(!realName.getText().toString().equals(""))
                        {
                            if(inputYanZhengma.getText().toString().equals("1234"))
                            {
                                //开启线程
                                SetPhoneThread setPhoneThread = new SetPhoneThread();
                                setPhoneThread.setAlertDialog(alertDialog);
                                Map<String, String> data = new HashMap<>();
                                data.put("idNumber", idNumber.getText().toString());
                                data.put("realName", realName.getText().toString());
                                data.put("oldUsername", oldPhone.getText().toString());
                                data.put("username", newPhone.getText().toString());
                                setPhoneThread.setData(data);
                                setPhoneThread.start();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "身份验证必填", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "身份验证必填", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "用户名必填", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }break;
            case R.id.reGet:
            {
                new CountDownReceviceTime(60000, 1000, reGet).start();
            }break;
        }
    }

    class SetPhoneThread extends Thread
    {
        private SetPhoneHandler setPhoneHandler;
        private AlertDialog alertDialog;
        private Map<String, String> data = new HashMap<>();
        public void setData(Map<String, String> data){this.data =data;}
        public void setAlertDialog(AlertDialog alertDialog) { this.alertDialog = alertDialog; }

        @Override
        public void run()
        {
            Looper.prepare();
            setPhoneHandler = new SetPhoneHandler();
            setPhoneHandler.sendEmptyMessage(123);
            Looper.loop();
        }

        class SetPhoneHandler extends Handler
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                if(msg.what == 123)
                {
                    HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SET_PHONE);
                    httpsUtil.setSendData(data);
                    httpsUtil.sendPost();
                    try
                    {
                        String a = httpsUtil.getResponse().body().string();
                        JSONObject jsonObject = new JSONObject(a);
                        if(jsonObject.getInt("isSuccess") != 1)
                        {
                            Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                        SQLiteDataBaseOpenHelper sqLiteDataBaseOpenHelper = new SQLiteDataBaseOpenHelper(getContext(), MainActivity.databasePath,
                                null, MainActivity.newDataBaseVersion);
                        sqLiteDataBaseOpenHelper.getWritableDatabase().execSQL("update UserInfo set username='" + data.get("username") + "'" +
                                " where username='" + data.get("oldUsername") + "'");
                        sqLiteDataBaseOpenHelper.close();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                        alertDialog.dismiss();
                    }
                    getActivity().finish();
                }
            }
        }
    }
}
