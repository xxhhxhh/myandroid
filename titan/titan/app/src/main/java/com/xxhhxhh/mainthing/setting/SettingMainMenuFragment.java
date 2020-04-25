package com.xxhhxhh.mainthing.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.Factory.ButtonFactory;
import com.example.titan.R;
import com.xxhhxhh.mainthing.SettingActivity;
import com.xxhhxhh.whenstart.UserLogin;

public class SettingMainMenuFragment extends Fragment
    implements Switch.OnCheckedChangeListener,
        View.OnClickListener
{
    private static final int[] viewIds =
            {
                    R.id.setUserInfo, R.id.setUserPrivacy, R.id.setUserSecurity, R.id.logOut, R.id.setYouthModel
            };//该界面组件
    private LinearLayout main;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        manager = getFragmentManager();
        SettingActivity.nowItem = 0;
       return makeSettingMenu(getContext());
    }

    //视图创建
    public View makeSettingMenu(Context context)
    {
        main = new LinearLayout(context);
        main.setOrientation(LinearLayout.VERTICAL);
        View title = LayoutInflater.from(context).inflate(R.layout.setting_title, null);//标题栏
        ((Button)title.findViewById(R.id.goBack)).setOnClickListener(this);
        main.addView(title);
        //选项栏
        Button setUserInfo = (Button) ButtonFactory.buttonFactory(context, ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[0]);
        Button setUserPrivacy = (Button)ButtonFactory.buttonFactory(context, ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[1]);
        Button setUserSecurity = (Button)ButtonFactory.buttonFactory(context, ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[2]);
        Switch youthModel = (Switch)ButtonFactory.buttonFactory(context, ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[4]);
        youthModel.setOnCheckedChangeListener(this);

        //单独设置文本和监听
        setClick(setUserInfo, R.string.userInfo, context);
        setClick(setUserPrivacy, R.string.privacy, context);
        setClick(setUserSecurity, R.string.security, context);
        setClick(youthModel, R.string.youthModel, context);

        main.addView(setUserInfo);
        main.addView(setUserPrivacy);
        main.addView(setUserSecurity);
        main.addView(youthModel);

        //占位
        TextView textView = new TextView(context);
        main.addView(textView);

        //注销按钮
        Button logOut = (Button) ButtonFactory.buttonFactory(context, ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[3]);
        setClick(logOut, R.string.logOut, context);
        main.addView(logOut);
        return main;
    }

    //设置监听,0单击，1开关
    public void setClick(View v, int stringId, Context context)
    {
       if(v != null)
       {
           Button v1 = (Button)v;
           v1.setOnClickListener(this);
           v1.setText(context.getResources().getString(stringId));
       }
       else
       {
           Log.d("异常", "视图为空，无法指定监听");
       }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                getActivity().onBackPressed();
            }break;
            case R.id.setUserInfo:
            {
                if(UserLogin.IS_LOGIN)
                {
                    SettingUserInfoMenuFragment infoMenu = new SettingUserInfoMenuFragment();
                    manager.beginTransaction().replace(R.id.showSettingMenu ,infoMenu, "infoMenu").addToBackStack("setting").commit();
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            case R.id.setUserPrivacy:
            {
                if(UserLogin.IS_LOGIN)
                {
                    SettingUserPrivacyMenuFragment privacyMenu = new SettingUserPrivacyMenuFragment();
                    manager.beginTransaction().replace(R.id.showSettingMenu , privacyMenu, "privacyMenu").addToBackStack("setting").commit();
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            case R.id.setUserSecurity:
            {
                if(UserLogin.IS_LOGIN)
                {
                    SettingUserSecurityMenuFragment settingUserSecurityMenu = new SettingUserSecurityMenuFragment();
                    manager.beginTransaction().replace(R.id.showSettingMenu , settingUserSecurityMenu, "securityMenu").addToBackStack("setting").commit();
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }

            }break;
            case R.id.logOut:
            {
                ((UserInfoApplocation)getActivity().getApplication()).exit();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("headImage", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userHead", "");
                Toast.makeText(getContext(), "注销", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //青少年模式
        if(buttonView.getId() == viewIds[4])
        {
            //打开
            if(isChecked)
            {
                Toast.makeText(getContext(), "已开启青少年模式", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), "已关闭青少年模式", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
