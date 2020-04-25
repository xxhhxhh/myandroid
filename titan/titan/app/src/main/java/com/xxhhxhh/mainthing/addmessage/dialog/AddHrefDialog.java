package com.xxhhxhh.mainthing.addmessage.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;

public class AddHrefDialog extends AlertDialog.Builder
    implements DialogInterface.OnClickListener
{

    private Context context;
    private AddArticleFragment addArticleFragment;
    private EditText editText;

    public AddHrefDialog(Context context, Fragment fragment)
    {
        super(context);
        this.context = context;
        //初始化模式
        addArticleFragment = (AddArticleFragment) fragment;
        setView(initView());
        setPositiveButton("确定",  this);
        setNegativeButton("取消", this);
        setTitle("添加链接");
    }

    //初始化视图
    private LinearLayout initView()
    {
        LinearLayout main = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.add_href_dialog, null);
        editText = main.findViewById(R.id.editHref);
        return main;
    }


    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if (which == DialogInterface.BUTTON_POSITIVE)
        {
            //添加链接
            addArticleFragment.getWebView().loadUrl("javascript:addHref('" + editText.getText() + "')");
        }
    }
}
