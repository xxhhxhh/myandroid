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
import com.xxhhxhh.mainthing.login_and_register.timer.CountDownReceviceTime;
import com.xxhhxhh.mainthing.pagechanges.LoginRegisterPageAdapter;

public class YanZhengMaReceviceFragment extends Fragment
    implements View.OnClickListener
{
    private View rootView;
    private EditText inputYanZhengMa;
    private ViewPager login_register_switch;//页面切换
    private int mode;//返回状态
    private Button reGet;//获取验证码

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recevice_yanzhengma, null);
        init();
        return  rootView;
    }

    private void init()
    {
        mode = getActivity().getIntent().getIntExtra("backTo", 0);

        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);

        reGet = (Button)rootView.findViewById(R.id.reGet);
        reGet.setOnClickListener(this);

        Button confirm = (Button)rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        inputYanZhengMa = (EditText)rootView.findViewById(R.id.inputYanZhengMa);

        login_register_switch = getActivity().findViewById(R.id.login_register_switch);
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
                if(inputYanZhengMa.getText().toString().equals("1234"))
                {
                    ForgetPasswordFragment.username = ((EditText)rootView.findViewById(R.id.username)).getText().toString();
                    login_register_switch.setCurrentItem(2);
                }
            }break;
            case R.id.reGet:
            {
                new CountDownReceviceTime(60000, 1000, reGet).start();
            }break;
        }

    }
}
