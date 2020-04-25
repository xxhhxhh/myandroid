package com.xxhhxhh.mainthing.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.xxhhxhh.mainthing.Factory.ButtonFactory;
import com.xxhhxhh.mainthing.LoginAndRegisterActivity;
import com.example.titan.R;
import com.xxhhxhh.mainthing.SettingActivity;

public class SettingUserSecurityMenuFragment extends Fragment
    implements Switch.OnCheckedChangeListener,
        View.OnClickListener
{

    private static final int[] viewIds =
            {
                    R.id.editUserPhone, R.id.editUserPassword, R.id.strangerLogin
            };//该界面组件
    private LinearLayout main;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manager = getFragmentManager();
        SettingActivity.nowItem = 2;
        return makeSettingMenu(getContext());
    }

    public View makeSettingMenu(Context context)
    {
        main = new LinearLayout(context);
        main.setOrientation(LinearLayout.VERTICAL);
        View title = LayoutInflater.from(context).inflate(R.layout.setting_title, null);//标题栏
        ((Button)title.findViewById(R.id.goBack)).setOnClickListener(this);
        main.addView(title);
        Button editUserPhone = (Button) ButtonFactory.buttonFactory(context, ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[0]);
        Button editUserPassword = (Button)ButtonFactory.buttonFactory(context,  ButtonFactory.ORDINARY_BUTTON, R.style.settingOption, viewIds[1]);
        Switch strangerLogin = (Switch)ButtonFactory.buttonFactory(context,  ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[2]);
        setClick(editUserPassword, R.string.editUserPassword, context);
        setClick(editUserPhone, R.string.editUserPhone, context);
        assert strangerLogin != null;
        strangerLogin.setOnCheckedChangeListener(this);
        strangerLogin.setText(context.getResources().getString(R.string.strangerLogin));

        main.addView(editUserPhone);
        main.addView(editUserPassword);
        main.addView(strangerLogin);
        return main;
    }


    public void setClick(View v, int stringId, Context context) {
        if(v != null)
        {
            Button v1 = (Button)v;
            v1.setOnClickListener(this);
            v1.setText(context.getResources().getString(stringId));
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                manager.popBackStack();
            }break;
            //修改手机
            case R.id.editUserPhone:
            {
                Intent intent = new Intent(getContext(), LoginAndRegisterActivity.class);
                intent.putExtra("edit", 3);
                intent.putExtra("backTo", 11);
                getContext().startActivity(intent);
            }break;
                //修改密码
            case R.id.editUserPassword:
            {
                Intent intent = new Intent(getContext(), LoginAndRegisterActivity.class);
                intent.putExtra("edit", 1);
                intent.putExtra("backTo", 11);
                getContext().startActivity(intent);
            }break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked)
        {
            if(buttonView.getId() == viewIds[2])
            {
                Toast.makeText(getContext(), "开启陌生登录提醒", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
