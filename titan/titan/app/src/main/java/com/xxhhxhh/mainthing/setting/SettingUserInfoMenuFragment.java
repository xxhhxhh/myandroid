package com.xxhhxhh.mainthing.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.SettingActivity;
import com.xxhhxhh.mainthing.dialog.ShowLoadDialog;
import com.xxhhxhh.mainthing.dialog.TipDialog;
import com.xxhhxhh.transdata.util.THESEARCH;

public class SettingUserInfoMenuFragment extends Fragment
    implements View.OnClickListener
{

    private boolean type = false;
    private static final int[] viewIds =
            {
                    R.id.loveThing, R.id.nickName, R.id.otherMessage
            };//该界面组件
    private static  final int[] viewTextIds =
            {
                    R.string.loveThing, R.string.nickName, R.string.otherMessage
            };//该页面文本视图提示文本
    private LinearLayout main;
    private FragmentManager manager;
    private UserInfoApplocation userInfoApplocation;
    //屏蔽检测初始化
    private THESEARCH thesearch = new THESEARCH();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manager = getFragmentManager();
        SettingActivity.nowItem = 1;
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
        //每一行
        for(int i = 0; i < viewIds.length; i++)
        {
            LinearLayout one = new LinearLayout(context);
            one.setMinimumHeight(0);
            one.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            one.setWeightSum(0.1f);
            one.setOrientation(LinearLayout.HORIZONTAL);
            //文本
            TextView textView = new TextView(context);
            textView.setTextAppearance(R.style.userInfoText);
            textView.setText(viewTextIds[i]);
            EditText editText = new EditText(context);
            editText.setEnabled(false);
            editText.setId(viewIds[i]);
            editText.setTextAppearance(R.style.userInfoEdit);
            one.addView(textView);
            one.addView(editText);
            main.addView(one);
        }

        UserInfoApplocation userInfoApplocation = (UserInfoApplocation)getActivity().getApplication();
        ((EditText)main.findViewById(R.id.loveThing)).setHint(userInfoApplocation.getMyLove());
        ((EditText)main.findViewById(R.id.otherMessage)).setHint(userInfoApplocation.getMyOther());
        ((EditText)main.findViewById(R.id.nickName)).setHint(userInfoApplocation.getNickName());

        //按钮行
        View endLine = LayoutInflater.from(context).inflate(R.layout.edit_userinfo, null);
        Button button1 = endLine.findViewById(R.id.editUserInfo);
        Button button2 = endLine.findViewById(R.id.saveUserInfo);
        setClick(button1, R.string.editUserInfo, context);
        setClick(button2, R.string.saveUserInfo, context);
        main.addView(endLine);
        return main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                manager.popBackStack();
            }break;
            //编辑
            case R.id.editUserInfo:
            {
                if(!getInput())
                {
                    setInput(true);
                    for(int id : viewIds)
                    {
                        EditText editText = (EditText)main.findViewById(id);
                        editText.setEnabled(true);
                    }
                }
                else
                {
                    setInput(false);
                    for(int id : viewIds)
                    {
                        EditText editText = (EditText)main.findViewById(id);
                        editText.setEnabled(false);
                    }
                }
            }break;
           //保存
            case R.id.saveUserInfo:
            {
                ShowLoadDialog showLoadDialog = new ShowLoadDialog(getContext());
                AlertDialog alertDialog = showLoadDialog.create();
                alertDialog.show();

                String myother = ((EditText)main.findViewById(R.id.otherMessage)).getText().toString();
                String mylove = ((EditText)main.findViewById(R.id.loveThing)).getText().toString();
                String nickname = ((EditText)main.findViewById(R.id.nickName)).getText().toString();

                if(nickname.length() <= 15)
                {
                    thesearch.setName(nickname);
                    if(thesearch.toStart().equals("no"))
                    {
                        alertDialog.dismiss();
                        TipDialog tipDialog = new TipDialog(getContext(), "昵称内容非法");
                        tipDialog.create().show();
                    }
                    else
                    {
                        userInfoApplocation.setNickName(nickname);
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    TipDialog tipDialog = new TipDialog(getContext(), "昵称长度超限");
                    tipDialog.create().show();
                }

                if(myother.length() <= 50)
                {
                    thesearch.setName(myother);
                    if(thesearch.toStart().equals("no"))
                    {
                        alertDialog.dismiss();
                        TipDialog tipDialog = new TipDialog(getContext(), "昵称内容非法");
                        tipDialog.create().show();
                    }
                    else
                    {
                        userInfoApplocation.setMyOther(myother);
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    TipDialog tipDialog = new TipDialog(getContext(), "备注长度超限");
                    tipDialog.create().show();
                }

                if(mylove.length() <= 20)
                {
                    thesearch.setName(mylove);
                    if(thesearch.toStart().equals("no"))
                    {
                        alertDialog.dismiss();
                        TipDialog tipDialog = new TipDialog(getContext(), "昵称内容非法");
                        tipDialog.create().show();
                    }
                    else
                    {
                        userInfoApplocation.setMyLove(((EditText)main.findViewById(R.id.loveThing)).getText().toString());
                        alertDialog.dismiss();
                    }
                }
                else
                {
                    TipDialog tipDialog = new TipDialog(getContext(), "爱好长度超限");
                    tipDialog.create().show();
                }

                setInput(false);
                for(int id : viewIds)
                {
                    EditText editText = (EditText)main.findViewById(id);
                    editText.setEnabled(false);
                }
            }break;
        }

    }

    public void setClick(View v, int stringId, Context context)
    {
        if(v != null)
        {
            Button v1 = (Button)v;
            v1.setText(context.getResources().getString(stringId));
            v1.setOnClickListener(this);
        }
    }

    //激活编辑框
    private void setInput(boolean type)
    {
        this.type = type;
    }

    private boolean getInput()
    {
        return  this.type;
    }
}
