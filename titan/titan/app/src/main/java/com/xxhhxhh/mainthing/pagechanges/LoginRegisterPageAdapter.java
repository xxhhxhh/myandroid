package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.xxhhxhh.mainthing.login_and_register.ForgetPasswordFragment;
import com.xxhhxhh.mainthing.login_and_register.LoginFragment;
import com.xxhhxhh.mainthing.login_and_register.UpdatePhoneFragment;
import com.xxhhxhh.mainthing.login_and_register.YanZhengMaReceviceFragment;

public class LoginRegisterPageAdapter extends FragmentPagerAdapter
{
    public LoginRegisterPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            //验证码
            case 1:
            {
                return new YanZhengMaReceviceFragment();
            }
            //忘记密码
            case 2:
            {
                return new ForgetPasswordFragment();
            }
            //更换手机号
            case 3:
            {
                return new UpdatePhoneFragment();
            }
            default: return new LoginFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}
