package com.xxhhxhh.mainthing.show.commentsandreply.holder;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiCommentDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.CommentMenuPopUpWindow;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.ShowOneWholeCommentPopUpWindow;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.ToReplyPopUpWindow;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.mainthing.show.util.GiveGood;
import com.xxhhxhh.whenstart.UserLogin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OneCommentViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{
    View itemView;
    ImageView headImage;//头像
    TextView username;//用户名
    Button good;//关注
    Button reply;//回复
    TextView comment;//评论文本
    TextView replyShow;//回复数量
    private int goodChose = 0;//是否点赞
    public void setItemView(View itemView) { this.itemView = itemView; }
    public void setHeadImage(ImageView headImage){ this.headImage = headImage; }
    public void setUsername(TextView username){this.username = username;}
    public void setGood(Button good){ this.good = good; }
    public void setReplyShow(TextView replyShow){ this.replyShow = replyShow; }
    public TextView getReplyShow() { return replyShow; }
    public void setComment(TextView comment){ this.comment = comment; }
    public Button getGood() { return good; }
    public ImageView getHeadImage() { return headImage; }
    public TextView getComment() { return comment; }
    public TextView getUsername() { return username; }
    public View getItemView() { return itemView; }
    public Button getReply() { return reply; }
    public void setReply(Button reply) { this.reply = reply; }

    //线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private int type;//类型：0随记，1文章
    private SuiJiCommentDataBean suiJiCommentDataBean;
    private ArticleCommentDataBean articleCommentDataBean;
    private Fragment fragment;
    private GoodWillUpdate goodWillUpdate = new GoodWillUpdate(20000, 2000);
    private int nowChose = 0;//单击次数检测，超过一次不再开启计时

    public OneCommentViewHolder(@NonNull View itemView, int type, Fragment fragment)
    {
        super(itemView);
        setItemView(itemView);
        this.type = type;
        this.fragment = fragment;
        setComment(itemView.findViewById(R.id.comment));
        setReply(itemView.findViewById(R.id.reply));
        setUsername(itemView.findViewById(R.id.messageShowUserName));
        setGood(itemView.findViewById(R.id.good));
        setHeadImage(itemView.findViewById(R.id.messageShowHeadImage));
        setReplyShow(itemView.findViewById(R.id.toShowWholeComment));
        getItemView().setOnClickListener(this);
        getComment().setOnClickListener(this);
        getReplyShow().setOnClickListener(this);
        getReply().setOnClickListener(this);
        getGood().setOnClickListener(this);
        getGood().setBackgroundColor(Color.parseColor("#000000"));
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.oneComment:
            case R.id.comment:
            {
                switch (type)
                {
                    case 0:
                    {
                        suiJiCommentDataBean = (SuiJiCommentDataBean) getItemView().getTag();
                        CommentMenuPopUpWindow popUpWindow = new CommentMenuPopUpWindow(itemView.getContext(),
                                type, fragment, suiJiCommentDataBean.getComment_username(), suiJiCommentDataBean.getSuiji_id());

                        popUpWindow.showAtLocation(itemView, Gravity.END, 0, 0);
                    }break;
                    case 1:
                    {
                        articleCommentDataBean = (ArticleCommentDataBean) getItemView().getTag();
                        CommentMenuPopUpWindow popUpWindow = new CommentMenuPopUpWindow(itemView.getContext(),
                                type, fragment, articleCommentDataBean.getComment_username(), articleCommentDataBean.getArticle_id());

                        popUpWindow.showAtLocation(itemView, Gravity.END, 0, 0);
                    }break;
                }
            }break;
            case R.id.reply:
            {
                if(UserLogin.IS_LOGIN)
                {
                    String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                    if(username != null && !username.equals(""))
                    {
                        switch (type)
                        {
                            case 0:
                            {
                                if(suiJiCommentDataBean != null)
                                {
                                    suiJiCommentDataBean = (SuiJiCommentDataBean) getItemView().getTag();
                                    ToReplyDialog toReplyDialog = new ToReplyDialog(itemView.getContext(), 1, 0,
                                            username, "", suiJiCommentDataBean.getSuiji_comment_id());
                                    toReplyDialog.setTitle("评论");
                                    toReplyDialog.show();
                                }
                            }break;
                            case 1:
                            {
                                if(articleCommentDataBean != null)
                                {
                                    articleCommentDataBean = (ArticleCommentDataBean) getItemView().getTag();
                                    ToReplyPopUpWindow popUpWindow = new ToReplyPopUpWindow(itemView.getContext(),
                                            "回复" + articleCommentDataBean.getComment_username(),
                                            username, articleCommentDataBean.getComment_username()
                                            , articleCommentDataBean.getArticle_comment_id(), 0);
                                    popUpWindow.showAtLocation(itemView, Gravity.BOTTOM, 0, 0);
                                }
                            }break;
                        }
                    }

                }
                else
                {
                    UserLogin.requestLogin(itemView.getContext());
                }
            }break;
            case R.id.toShowWholeComment:
            {
                switch (type)
                {
                    case 0:
                    {
                        suiJiCommentDataBean = (SuiJiCommentDataBean) getItemView().getTag();
                    }break;
                    case 1:
                    {
                        articleCommentDataBean = (ArticleCommentDataBean) getItemView().getTag();
                    }break;
                }
                ShowOneWholeCommentPopUpWindow popUpWindow = new ShowOneWholeCommentPopUpWindow(itemView.getContext(),
                        fragment, type, suiJiCommentDataBean, articleCommentDataBean);
                popUpWindow.showAtLocation(itemView, Gravity.END, 0, 0);
            }break;
            case R.id.good:
            {
                if(goodChose % 2 == 0)
                {
                    String a = getGood().getText().toString().replace("赞", "");
                    int x = !a.equals("") ? Integer.parseInt(a) + 1 : 1;
                    getGood().setText("赞" + x);
                    getGood().setBackgroundColor(Color.parseColor("#456123"));
                    goodChose = 1;
                }
                else
                {
                    String a = getGood().getText().toString().replace("赞", "");
                    int x = !a.equals("") ? (Integer.parseInt(a) > 0 ? Integer.parseInt(a) - 1 : 0) : 0;
                    getGood().setText("赞" + x);
                    getGood().setBackgroundColor(Color.parseColor("#000000"));
                    goodChose = 0;
                }

                if(nowChose == 0)
                {
                    nowChose = 1;
                    goodWillUpdate.start();
                }
            }break;
        }
    }

    //点赞更新器
    class GoodWillUpdate extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public GoodWillUpdate(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {

        }

        @Override
        public void onFinish()
        {
            //触发点赞存储
            if(goodChose % 2 == 0)
            {
                Runnable runnable = () ->
                {
                    switch (type)
                    {
                        case 0:
                        {
                            GiveGood.giveGood(suiJiCommentDataBean.getSuiji_comment_id(),
                                    2);
                        }break;
                        case 1:
                        {
                            GiveGood.giveGood(articleCommentDataBean.getArticle_comment_id(),
                                    3);
                        }break;
                    }
                };
                executorService.execute(runnable);
            }
        }
    }

    //信息更新

}
