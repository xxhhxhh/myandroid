package com.xxhhxhh.mainthing.login_and_register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordFragment extends Fragment
    implements View.OnClickListener
{

    private ViewPager login_register_switch;//页面切换
    private View rootView;
    private EditText newPassword;//新密码
    private EditText rePassword;//再次确认
    private int mode;//返回模式
    public static String username;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.forget_password, null);
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

        login_register_switch = (ViewPager)getActivity().findViewById(R.id.login_register_switch);

        newPassword = (EditText)rootView.findViewById(R.id.newPassword);
        rePassword = (EditText)rootView.findViewById(R.id.reInputPassword);
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
                if(!username.equals("") && !newPassword.getText().toString().equals("") && newPassword.getText().toString().equals(rePassword.getText().toString()))
                {
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            //设置身份认证信息
                            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SET_PASSWORD);
                            Map<String, String> data = new HashMap<>();
                            System.out.println(username);
                            data.put("password", newPassword.getText().toString());
                            data.put("username", username);
                            httpsUtil.setSendData(data);
                            httpsUtil.sendPost();
                        }
                    }.start();
                    login_register_switch.setCurrentItem(0);
                }
            }break;
        }

    }


}
