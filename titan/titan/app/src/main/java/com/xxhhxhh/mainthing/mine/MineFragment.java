package com.xxhhxhh.mainthing.mine;


import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.LoginAndRegisterActivity;
import com.example.titan.R;
import com.xxhhxhh.mainthing.SettingActivity;
import com.xxhhxhh.mainthing.mine.infolook.ShowInfoPopUpBaseWindow;
import com.xxhhxhh.mainthing.mine.popwindow.ChoseHeadImageModePopWindow;
import com.xxhhxhh.service.UserInfoSetIntentService;
import com.xxhhxhh.transdata.requestcode.RequestCode;
import com.xxhhxhh.whenstart.UserLogin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MineFragment extends Fragment implements View.OnClickListener
{

    private View rootView;//根视图
    public TextView usernameShow;
    private String username;
    private ImageView headImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mine, container, false);//页面根视图
        //设置
        Button settingButton = (Button) rootView.findViewById(R.id.showSetting);
        settingButton.setOnClickListener(this);

        //图像
        headImage = rootView.getRootView().findViewById(R.id.headImage);
        headImage.setOnClickListener(this);
        //用户名
        usernameShow = rootView.findViewById(R.id.showUsername);
        usernameShow.setOnClickListener(this);
        //查看文章
        TextView articleLook = rootView.findViewById(R.id.showArticle);
        //查看关注
        TextView theFocus = rootView.findViewById(R.id.showFocus);
        //查看黑名单
        TextView theBlackList = rootView.findViewById(R.id.showBlackList);
        //查看随记
        TextView theSuiJi = rootView.findViewById(R.id.showSuiJi);
        theSuiJi.setOnClickListener(this);
        articleLook.setOnClickListener(this);
        theFocus.setOnClickListener(this);
        theBlackList.setOnClickListener(this);
        usernameShow.setText(((UserInfoApplocation)getActivity().getApplication()).getUsername());
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SetUserInfo setUserInfo = new SetUserInfo(30000, 1000);
        setUserInfo.start();
        username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
        if(username == null || username.equals(""))
        {
            usernameShow.setText("未登录");
            headImage.setImageResource(R.drawable.ic_launcher_background);
            headImage.setClickable(false);
        }
        else
        {
            headImage.setClickable(true);
        }

    }


    class SetUserInfo extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SetUserInfo(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            String username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
            if(username != null && !username.equals(""))
            {
                usernameShow.setText(username);
                usernameShow.setClickable(false);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
                String imageString = sharedPreferences.getString("userHead", "");
                String username1 = sharedPreferences.getString("username", "");
                if( !imageString.equals("") && !username1.equals("") && username1.equals(username))
                {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(imageString, Base64.NO_WRAP));
                    headImage.setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                }
                onFinish();
            }
            else
            {
                usernameShow.setText("未登录");
                usernameShow.setClickable(true);
                headImage.setImageResource(R.drawable.ic_launcher_background);
                headImage.setClickable(false);
            }
        }

        @Override
        public void onFinish()
        {
            onStop();
        }
    }

    @Override
    public void onClick(View v) {


        //打开设置
       switch (v.getId())
        {
            case R.id.showUsername:
            {
                Intent intent = new Intent(getContext(), LoginAndRegisterActivity.class);
                startActivityForResult(intent, RequestCode.USER_WANT_LOGIN);
            }break;
            case R.id.showSetting:
            {
                if(UserLogin.IS_LOGIN)
                {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }
            break;
            //查看随记
            case R.id.showSuiJi:
            {
                if(UserLogin.IS_LOGIN)
                {
                    username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
                    ShowInfoPopUpBaseWindow popUpBaseWindow = new ShowInfoPopUpBaseWindow(getContext(), 1, username);
                    popUpBaseWindow.showAtLocation(rootView.findViewById(R.id.showSetting), Gravity.END, 0, 0);
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            //查看文章
            case R.id.showArticle:
            {
                if(UserLogin.IS_LOGIN)
                {
                    username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
                    ShowInfoPopUpBaseWindow popUpBaseWindow = new ShowInfoPopUpBaseWindow(getContext(), 2, username);
                    popUpBaseWindow.showAtLocation(rootView.findViewById(R.id.showSetting), Gravity.END, 0, 0);
                }
                 else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            //查看关注
            case R.id.showFocus:
            {
                if(UserLogin.IS_LOGIN)
                {
                    username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
                    ShowInfoPopUpBaseWindow popUpBaseWindow = new ShowInfoPopUpBaseWindow(getContext(), 3, username);
                    popUpBaseWindow.showAtLocation(rootView.findViewById(R.id.showSetting), Gravity.END, 0, 0);
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            //查看黑名单
            case R.id.showBlackList:
            {
                if(UserLogin.IS_LOGIN)
                {
                    username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
                    ShowInfoPopUpBaseWindow popUpBaseWindow = new ShowInfoPopUpBaseWindow(getContext(), 4, username);
                    popUpBaseWindow.showAtLocation(rootView.findViewById(R.id.showSetting), Gravity.END, 0, 0);
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }
            }break;
            case R.id.headImage:
            {
                ChoseHeadImageModePopWindow popWindow = new ChoseHeadImageModePopWindow(this);
                popWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
            }break;
        }

    }


}
