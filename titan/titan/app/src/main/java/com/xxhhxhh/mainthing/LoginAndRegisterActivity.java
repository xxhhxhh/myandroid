package com.xxhhxhh.mainthing;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.titan.R;
import com.xxhhxhh.mainthing.pagechanges.LoginRegisterPageAdapter;


public class LoginAndRegisterActivity extends FragmentActivity
{

    private ViewPager loginRegisterPager;
    private int out_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        Intent intent = getIntent();
        int enter_mode = intent.getIntExtra("edit", 0);
        out_mode = intent.getIntExtra("backTo", 0);
        //页面切换
        loginRegisterPager = findViewById(R.id.login_register_switch);
        loginRegisterPager.setAdapter(new LoginRegisterPageAdapter(getSupportFragmentManager(), 1));
        loginRegisterPager.setCurrentItem(enter_mode);

    }


    @Override
    public void onBackPressed()
    {
        if(loginRegisterPager.getCurrentItem() != 0 && out_mode == 0)
        {
            loginRegisterPager.setCurrentItem(0);
        }
        else
        {
            super.onBackPressed();
        }

    }

}
