package com.xxhhxhh.mainthing.setting;

import android.content.Context;
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

public class SettingUserPrivacyMenuFragment extends Fragment
    implements View.OnClickListener, Switch.OnCheckedChangeListener
{

    private LinearLayout main;
    private static final int[] viewIds =
            {
                    R.id.allowRecommend, R.id.allowLookSuiJi, R.id.allowLookArticle, R.id.allowDetail
            };//本视图id
    private FragmentManager manager;
    private UserInfoApplocation userInfoApplocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manager = getFragmentManager();
        SettingActivity.nowItem = 3;
        userInfoApplocation = (UserInfoApplocation) getActivity().getApplication();
        return makeSettingMenu(getContext());
    }

    public View makeSettingMenu(Context context)
    {

        main = new LinearLayout(context);
        main.setOrientation(LinearLayout.VERTICAL);
        View title = LayoutInflater.from(context).inflate(R.layout.setting_title, null);//标题栏
        ((Button)title.findViewById(R.id.goBack)).setOnClickListener(this);
        main.addView(title);
        //开关项
        UserInfoApplocation userInfoApplocation = (UserInfoApplocation)getActivity().getApplication();
        Switch allowRecommend = (Switch) ButtonFactory.buttonFactory(context, ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[0]);
        allowRecommend.setChecked(userInfoApplocation.getAllowRecommend() == 1);
        Switch allowLookSuiJi = (Switch)ButtonFactory.buttonFactory(context, ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[1]);
        allowLookSuiJi.setChecked(userInfoApplocation.getAllowLookSuiJi() == 1);
        Switch allowLookArticle = (Switch)ButtonFactory.buttonFactory(context, ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[2]);
        allowLookArticle.setChecked(userInfoApplocation.getAllowLookArticle() == 1);
        Switch allowDetail = (Switch)ButtonFactory.buttonFactory(context, ButtonFactory.SWITCH_BUTTON, R.style.settingOption, viewIds[3]);
        allowDetail.setChecked(userInfoApplocation.getAllowDetail() == 1);
        setClick(allowRecommend, R.string.allowRecommend, context);
        setClick(allowLookArticle, R.string.allowLookArticle, context);
        setClick(allowLookSuiJi, R.string.allowLookSuiJi, context);
        setClick(allowDetail, R.string.allowDetail, context);
        main.addView(allowRecommend);
        main.addView(allowLookSuiJi);
        main.addView(allowLookArticle);
        main.addView(allowDetail);

        return main;
    }

    public void setClick(View v, int stringId, Context context) {

        if(v != null)
        {
               Switch v1 = (Switch)v;
               v1.setOnCheckedChangeListener(this);
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
                manager.popBackStack();
            }break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked)
        {
            //允许推荐
            if(buttonView.getId() == viewIds[0])
            {
                userInfoApplocation.setAllowRecommend(1);
                Toast.makeText(getContext(), "允许推荐", Toast.LENGTH_SHORT).show();
            }
            //允许看随记
            else if(buttonView.getId() == viewIds[1])
            {
                userInfoApplocation.setAllowLookSuiJi(1);
                Toast.makeText(getContext(), "允许别人查看我的随记", Toast.LENGTH_SHORT).show();
            }
            //允许看文章
            else if(buttonView.getId() == viewIds[2])
            {
                userInfoApplocation.setAllowLookArticle(1);
                Toast.makeText(getContext(), "允许别人查看我的文章", Toast.LENGTH_SHORT).show();
            }
            //允许查看用户个人信息
            else if(buttonView.getId() == viewIds[3])
            {
                userInfoApplocation.setAllowDetail(1);
                Toast.makeText(getContext(), "允许别人查看我的信息", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //不允许推荐
            if(buttonView.getId() == viewIds[0])
            {
                userInfoApplocation.setAllowRecommend(0);
            }
            //不允许看随记
            else if(buttonView.getId() == viewIds[1])
            {
               userInfoApplocation.setAllowLookSuiJi(0);
            }
            //不允许看文章
            else if(buttonView.getId() == viewIds[2])
            {
               userInfoApplocation.setAllowLookArticle(0);
            }
            //不允许查看用户个人信息
            else if(buttonView.getId() == viewIds[3])
            {
                userInfoApplocation.setAllowDetail(0);
            }
        }

    }
}
