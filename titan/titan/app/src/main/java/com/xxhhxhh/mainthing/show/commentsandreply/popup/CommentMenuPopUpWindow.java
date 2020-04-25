package com.xxhhxhh.mainthing.show.commentsandreply.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.dialog.ToReportDialog;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.whenstart.UserLogin;

public class CommentMenuPopUpWindow extends PopupWindow
    implements View.OnClickListener
{

    /**单击评论显示弹窗
     * @param commentType /*0代表评论，1回复，2回复的回复
     * @param context /*上下文
     * @param username /*回复者
     * @param toUsername /*被回复者
     * @param type /*0随记、1文章
     * */
    private Context context;
    private int type;
    private String toUsername;
    private int id;
    private Fragment fragment;

    public CommentMenuPopUpWindow(Context context, int type, Fragment fragment, String toUsername, int id)
    {
        this.context = context;
        this.toUsername = toUsername;
        this.id = id;
        this.fragment =fragment;
        initView();
        this.type = type;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
    }

    private void initView()
    {
        LinearLayout main = new LinearLayout(context);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.parseColor("#000000"));
        main.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        main.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView share = new TextView(context);
        share.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        share.setHeight(80);
        share.setText("分享");
        share.setId(R.id.share);
        share.setOnClickListener(this);
        main.addView(share);
        TextView reply = new TextView(context);
        reply.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        reply.setHeight(80);
        reply.setText("回复");
        reply.setId(R.id.reply);
        reply.setOnClickListener(this);
        main.addView(reply);
        TextView report = new TextView(context);
        report.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        report.setHeight(80);
        report.setText("举报");
        report.setId(R.id.report);
        report.setOnClickListener(this);
        main.addView(report);
        setContentView(main);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.share:
            {

            }break;
            case R.id.report:
            {
                ToReportDialog toReportDialog = new ToReportDialog(context, toUsername);
                toReportDialog.show();
            }break;
            case R.id.reply:
            {
                if(UserLogin.IS_LOGIN)
                {
                    String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                    ToReplyDialog toReplyDialog = new ToReplyDialog(context, 1, type, username != null
                            ? username : "", toUsername, id);
                    toReplyDialog.setTitle("回复");
                    toReplyDialog.show();
                }
                else
                {
                    UserLogin.requestLogin(context);
                }
            }break;
        }
    }
}
