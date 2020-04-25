package com.xxhhxhh.mainthing.login_and_register;


import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.titan.R;
import com.xxhhxhh.mainthing.login_and_register.handler.LoginAndRegisterThread;
import com.xxhhxhh.mainthing.login_and_register.timer.CountDownReceviceTime;


public class LoginFragment extends Fragment
        implements View.OnClickListener,
        AdapterView.OnItemSelectedListener
{

    private View rootView;
    private String phoneLocation;//手机号区域
    private String[] phoneLocations;
    private ViewPager login_register_switch;//页面切换
    private int loginType;
    private EditText username;//用户名
    private EditText password;//密码
    private EditText yanzhengma;//验证码
    private EditText phone;//手机号登录
    private Button reGet;//获取验证码按钮
    private LoginAndRegisterThread loginAndRegisterThread;
    private AlertDialog alertDialog;//进度对话框



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login_register, container, false);
        loginAndRegisterThread = new LoginAndRegisterThread(getContext(), null);
        loginAndRegisterThread.start();
        init();
        return rootView;
    }

    private void init()
    {
        phoneLocations = getResources().getStringArray(R.array.phoneLocations);//手机号区域
        //返回键
        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        //登录
        Button login = (Button)rootView.findViewById(R.id.phoneLogin);
        login.setOnClickListener(this);
        Button passwordLogin = (Button)rootView.findViewById(R.id.passwordLogin);
        passwordLogin.setOnClickListener(this);
        //切换登录方式
        Button switchLogin = (Button)rootView.findViewById(R.id.switchLogin);
        switchLogin.setOnClickListener(this);
        //忘记密码
        Button forgetPassword = (Button)rootView.findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(this);
        //更换了手机
        Button updatePhone = (Button)rootView.findViewById(R.id.updatePhone);
        updatePhone.setOnClickListener(this);
        //获取验证吗
        reGet = (Button)rootView.findViewById(R.id.reGet);
        reGet.setOnClickListener(this);
        //选择手机号区域
        Spinner spinner = (Spinner)rootView.findViewById(R.id.showPhoneLocation);
        spinner.setSelection(0, true);
        spinner.setOnItemSelectedListener(this);
        //输入框
        username = (EditText)rootView.findViewById(R.id.username);
        phone = (EditText)rootView.findViewById(R.id.phoneLoginEditText);
        password = (EditText)rootView.findViewById(R.id.password);
        yanzhengma = (EditText)rootView.findViewById(R.id.inputYanZhengMa);

        //页面切换
        loginType = 1;
        login_register_switch = getActivity().findViewById(R.id.login_register_switch);
        toSwitchLogin();
    }

    //切换登录方式
    private void toSwitchLogin()
    {
        if(loginType == 0)
        {
            rootView.findViewById(R.id.passwordLoginShow).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.phoneLoginShow).setVisibility(View.GONE);
        }
         if(loginType == 1)
        {
            rootView.findViewById(R.id.phoneLoginShow).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.passwordLoginShow).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //返回
            case R.id.goBack:
            {
                getActivity().finish();
            }break;
            //密码登录
            case R.id.passwordLogin:
            {
                String username = this.username.getText().toString();
                String password = this.password.getText().toString();
                //不为空
                if(!username.equals("") && !password.equals(""))
                {
                    //进行验证
                    showTipDialog();
                    loginAndRegisterThread.loginAndRegisterHandler.setUsername(username);
                    loginAndRegisterThread.loginAndRegisterHandler.setPassword(password);
                    loginAndRegisterThread.loginAndRegisterHandler.sendEmptyMessage(LoginAndRegisterThread.LoginAndRegisterHandler.PASSWORD_LOGIN);
                }
                else
                {
                    AlertDialog.Builder passwordWorng = new AlertDialog.Builder(getContext()).setTitle("账号或密码错误").setMessage("账号或密码错误");
                    passwordWorng.setPositiveButton("确定", null);
                    passwordWorng.show();
                }
            }break;
            //手机号登录
            case R.id.phoneLogin:
            {

                if(phone.getText().toString().length() >= 11 && phone.getText().toString().length() <= 15)
                {
                    showTipDialog();
                    loginAndRegisterThread.loginAndRegisterHandler.setUsername(phone.getText().toString());
                    loginAndRegisterThread.loginAndRegisterHandler.sendEmptyMessage(LoginAndRegisterThread.LoginAndRegisterHandler.PHONE_LOGIN);
                }
            }break;
            //获取验证码
            case R.id.reGet:
            {
                showTipDialog();
                loginAndRegisterThread.loginAndRegisterHandler.sendEmptyMessage(LoginAndRegisterThread.LoginAndRegisterHandler.SEND_TEXT);
                new CountDownReceviceTime(60000, 1000, reGet).start();
            }break;
            //忘记密码
            case R.id.forgetPassword:
            {
                login_register_switch.setCurrentItem(1);
            }break;
            //更换了手机
            case R.id.updatePhone:
            {
                login_register_switch.setCurrentItem(3);
            }break;
            //切换登录方式
            case R.id.switchLogin:
            {
                if(loginType == 0)
                {
                    loginType = 1;
                    toSwitchLogin();
                }
                else if(loginType == 1)
                {
                    loginType = 0;
                    toSwitchLogin();
                }

            }break;
        }

    }

    //提示对话框
    private void showTipDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext()).setView(LayoutInflater.
                from(getContext()).inflate(R.layout.yanzheng_dialog, null));
        alertDialog = alert.show();
        loginAndRegisterThread.loginAndRegisterHandler.setAlertDialog(alertDialog);
        //计时
        ConnectReadTime connectReadTime = new ConnectReadTime(20000, 1000, alertDialog);
        connectReadTime.start();
    }

    //计时器
    static class ConnectReadTime extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.

         */
        private AlertDialog alertDialog;

        public ConnectReadTime(long millisInFuture, long countDownInterval, AlertDialog alertDialog)
        {
            super(millisInFuture, countDownInterval);
            this.alertDialog = alertDialog;
        }

        @Override
        public void onTick(long millisUntilFinished)
        {

        }

        @Override
        public void onFinish()
        {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        phoneLocation = phoneLocations[position];
        Toast.makeText(getContext(), phoneLocation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        phoneLocation = "+86";
    }
}
