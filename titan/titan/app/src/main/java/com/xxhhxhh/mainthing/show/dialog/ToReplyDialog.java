package com.xxhhxhh.mainthing.show.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.titan.R;
import com.xxhhxhh.transdata.util.CommentAndReplyAndResetDoUtil;

import java.util.HashMap;
import java.util.Map;

public class ToReplyDialog extends AlertDialog
    implements View.OnClickListener
{
    private EditText editText;

    /** 用户发送评论内容和回复内容
     * @param commentType /*0代表评论，1回复，2回复的回复
     * @param context /*上下文
     * @param username /*回复者
     * @param toUsername /*被回复者
     * */
    private int type;
    private int commentType;
    private String username;
    private String toUsername;
    private int id;

    public ToReplyDialog(Context context, int commentType, int type, String username, String toUsername, int id)
    {
        super(context);
        this.commentType = commentType;
        this.toUsername = toUsername;
        this.id = id;
        this.username = username;
        this.type = type;
        LinearLayout main = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.to_reply, null);
        setView(main);
        editText = main.findViewById(R.id.editReplyOrComment);

        Button button = main.findViewById(R.id.ok_add);
        button.setOnClickListener(this);
        Button button1 = main.findViewById(R.id.cancel);
        button1.setOnClickListener(this);
        //按类型设置文本
        switch (commentType)
        {
            case 0:
            {
                editText.setHint("评论");
            }break;
            case 1:
            {
                editText.setHint("回复" + toUsername + ":");
            }break;
            case 2:
                {
                editText.setHint(username + "回复" + toUsername + ":");
            }break;
        }

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.ok_add)
        {
            if(username != null && !username.equals(""))
            {
                //确定文本
                String text = null;
                if(commentType != 0)
                {
                    text = editText.getHint() + editText.getText().toString();
                }
                else
                {
                    text = editText.getText().toString();
                }
                String finalText = text;
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        Map<String, String> data = new HashMap<>();
                        data.put("username", username);
                        data.put("to_reply_username", toUsername);
                        data.put("message", finalText);
                        data.put("type", String.valueOf(commentType));
                        data.put("id", "" + id);
                        CommentAndReplyAndResetDoUtil.SaveCommentOrReply(commentType, data, type);
                    }
                }.start();
                this.dismiss();
            }
        }
        else if(v.getId() == R.id.cancel)
        {
            this.dismiss();
        }
    }


}
