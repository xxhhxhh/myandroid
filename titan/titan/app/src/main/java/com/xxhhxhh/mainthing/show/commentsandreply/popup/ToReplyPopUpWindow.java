package com.xxhhxhh.mainthing.show.commentsandreply.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.example.titan.R;
import com.xxhhxhh.transdata.util.CommentAndReplyAndResetDoUtil;
import com.xxhhxhh.whenstart.UserLogin;

import java.util.HashMap;
import java.util.Map;

public class ToReplyPopUpWindow extends PopupWindow
    implements View.OnClickListener
{
    private Context context;
    private EditText editText;//编辑内容
    private String username;
    private String toUsername;
    private int id;
    private int comment_type;//评论类型 0评论1回复

    public ToReplyPopUpWindow(Context context, String hint, String username, String toUsername, int id, int comment_type)
    {
        this.id = id;
        this.username = username;
        this.toUsername = toUsername;
        this.context = context;
        this.comment_type = comment_type;
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        initView(hint);
        setFocusable(true);
    }

    //
    private void initView(String hint)
    {
        LinearLayout main = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.to_reply, null);
        main.setBackgroundColor(Color.parseColor("#000fff"));
        editText = main.findViewById(R.id.editReplyOrComment);
        editText.setHint(hint);
        Button button = main.findViewById(R.id.ok_add);
        button.setOnClickListener(this);
        Button button1 = main.findViewById(R.id.cancel);
        button1.setOnClickListener(this);
        setContentView(main);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.ok_add)
        {
            if(UserLogin.IS_LOGIN)
            {
                if(username != null && !username.equals(""))
                {
                    String text = null;
                    if(editText.getHint().equals("评论"))
                    {
                        text =  editText.getText().toString();
                    }
                    else
                    {
                        text = editText.getHint() + editText.getText().toString();
                    }
                    final String text1 = text;
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            Map<String, String> data = new HashMap<>();
                            data.put("username", username);
                            data.put("to_reply_username", toUsername);
                            data.put("message", text1);
                            data.put("type", String.valueOf(0));
                            data.put("id", "" + id);
                            switch (comment_type)
                            {
                                case 0:
                                {
                                    CommentAndReplyAndResetDoUtil.SaveCommentOrReply(0, data, 1);
                                }break;
                                case 1:
                                {
                                    CommentAndReplyAndResetDoUtil.SaveCommentOrReply(1, data, 1);
                                }break;
                            }
                        }
                    }.start();
                    this.dismiss();
                }
            }
            else
            {
                UserLogin.requestLogin(context);
            }

        }
        else if(v.getId() == R.id.cancel)
        {
            dismiss();
        }
    }
}
