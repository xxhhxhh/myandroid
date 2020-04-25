package com.xxhhxhh.mainthing;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.titan.R;
import com.xxhhxhh.mainthing.setting.SettingMainMenuFragment;
import com.xxhhxhh.service.UserInfoSetIntentService;

public class SettingActivity extends FragmentActivity
{

    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static int nowItem = 0;
    private SettingMainMenuFragment mainMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mainMenu = new SettingMainMenuFragment();
        transaction.add(R.id.showSettingMenu, mainMenu, "mainMenu").addToBackStack("setting");
        transaction.show(mainMenu).commit();
    }



    @Override
    public void onBackPressed()
    {
        System.out.println(nowItem);
        if (nowItem == 0)
        {
            //修改用户信息
            Intent intent = new Intent(this, UserInfoSetIntentService.class);
            startService(intent);
            this.finish();
        }
        else
        {
           manager.popBackStack();
        }
    }
}
