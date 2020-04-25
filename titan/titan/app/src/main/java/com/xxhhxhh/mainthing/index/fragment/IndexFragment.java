package com.xxhhxhh.mainthing.index.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.example.titan.R;
import com.xxhhxhh.mainthing.index.viewpager.MessageViewPager;
import com.xxhhxhh.mainthing.pagechanges.SwitchSuiJiOrArticle;
import com.xxhhxhh.mainthing.pagechanges.SwitchSuiJiOrArticleTabSelectListener;
import com.xxhhxhh.transdata.requestcode.RequestCode;
import com.xxhhxhh.whenstart.UserLogin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class IndexFragment extends Fragment
    implements View.OnClickListener
{

    private PopupWindow popAdd;
    private Button addMessage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //根视图
        View rootView = inflater.inflate(R.layout.index, container, false);
        LinearLayout messageShow = rootView.findViewById(R.id.messageShow);

        TabLayout switchShow = rootView.findViewById(R.id.suiJiOrArticle);//一级标题栏
        MessageViewPager switchSuiJiOrArticle = new MessageViewPager(rootView.getContext());//页面切换
        switchSuiJiOrArticle.setScrollAble(true);//可滚动
        switchSuiJiOrArticle.setId(R.id.switchSuiJiOrArticle);
        messageShow.addView(switchSuiJiOrArticle);//加入视图
        //设置监听
        SwitchSuiJiOrArticleTabSelectListener switchSuiJiOrArticleTabSelectListener = new SwitchSuiJiOrArticleTabSelectListener(switchSuiJiOrArticle);
        switchShow.addOnTabSelectedListener(switchSuiJiOrArticleTabSelectListener);
        switchSuiJiOrArticle.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(switchShow));
        switchSuiJiOrArticle.setAdapter(new SwitchSuiJiOrArticle(getChildFragmentManager(), 1));//嵌套fragment调用childmanager

        LinearLayout add = (LinearLayout) inflater.inflate(R.layout.add_button, null);
        addMessage = (Button) rootView.findViewById(R.id.addMessage);
        FloatingActionButton addSuiJi = (FloatingActionButton)add.findViewById(R.id.addSuiJi);
        FloatingActionButton addArticle = (FloatingActionButton)add.findViewById(R.id.addArticle);
        addMessage.setOnClickListener(this);
        addSuiJi.setOnClickListener(this);
        addArticle.setOnClickListener(this);
        //弹窗
        popAdd = new PopupWindow();
        popAdd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popAdd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popAdd.setFocusable(true);
        popAdd.setContentView(add);
        return rootView;
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            //单击添加按钮
            case R.id.addMessage:
            {
                popAdd.showAsDropDown(addMessage);
            }break;
            //点击添加随记
            case R.id.addSuiJi:
            {
                if(UserLogin.IS_LOGIN)
                {
                    Intent intent = new Intent(getContext(), AddSuiJiActivity.class);
                    getActivity().startActivityForResult(intent, RequestCode.USER_WANT_LOGIN);
                }
                else
                {
                    UserLogin.getLogin(getContext());
                }
            }break;
            //添加文章
            case R.id.addArticle:
            {
                if(UserLogin.IS_LOGIN)
                {
                    UserLogin.getLogin(getContext());
                    Intent intent = new Intent(getContext(), AddArticleActivity.class);
                    getActivity().startActivityForResult(intent, RequestCode.USER_WANT_LOGIN);
                }
                else
                {
                    UserLogin.getLogin(getContext());
                }
            }break;

        }

    }


}
