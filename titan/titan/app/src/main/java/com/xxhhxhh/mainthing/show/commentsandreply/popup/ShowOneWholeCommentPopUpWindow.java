package com.xxhhxhh.mainthing.show.commentsandreply.popup;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.ArticleReplyBean;
import com.xxhhxhh.mainthing.databean.SuiJiCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiReplyBean;
import com.xxhhxhh.mainthing.show.commentsandreply.adapter.OneWholeCommentAdapter;
import com.xxhhxhh.mainthing.show.commentsandreply.holder.OneCommentViewHolder;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.mainthing.show.util.GiveGood;
import com.xxhhxhh.transdata.util.CommentAndReplyAndResetDoUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowOneWholeCommentPopUpWindow extends PopupWindow
    implements View.OnClickListener
{

    private LinearLayout main;
    private Context context;
    private Fragment fragment;
    private int type;
    private OneWholeCommentAdapter oneWholeCommentAdapter;
    private SuiJiCommentDataBean suiJiCommentDataBean;
    private ArticleCommentDataBean articleCommentDataBean;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Button good;
    private Button reply;
    private GoodWillUpdate goodWillUpdate = new GoodWillUpdate(20000, 2000);
    private int nowChose = 0;//单击次数检测，超过一次不再开启计时
    private int goodChose = 0;

    public void setArticleCommentDataBean(ArticleCommentDataBean articleCommentDataBean) { this.articleCommentDataBean = articleCommentDataBean; }
    public void setSuiJiCommentDataBean(SuiJiCommentDataBean suiJiCommentDataBean) { this.suiJiCommentDataBean = suiJiCommentDataBean; }

    public ShowOneWholeCommentPopUpWindow(Context context, Fragment fragment, int type, SuiJiCommentDataBean suiJiCommentDataBean
    , ArticleCommentDataBean articleCommentDataBean)
    {
        this.context = context;
        this.fragment =fragment;
        this.type = type;
        setArticleCommentDataBean(articleCommentDataBean);
        setSuiJiCommentDataBean(suiJiCommentDataBean);
        init();
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    //初始化
    private void init()
    {
        //返回
        main = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.whole_comment, null);
        main.setBackgroundColor(Color.parseColor("#000fff"));
        good = main.findViewById(R.id.good);
        good.setOnClickListener(this);
        reply = main.findViewById(R.id.reply);
        reply.setOnClickListener(this);
        Button goBack = main.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        oneWholeCommentAdapter = new OneWholeCommentAdapter(fragment, type);
        getData();
        //该评论所有回复
        RecyclerView recyclerView = main.findViewById(R.id.replyView);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        oneWholeCommentAdapter.setHasStableIds(true);
        recyclerView.setAdapter(oneWholeCommentAdapter);
        recyclerView.setLayoutManager(manager);
        setContentView(main);
        initData();
    }

    //初始化数据
    private void initData()
    {

        switch (type)
        {
            case 0:
            {
                if(suiJiCommentDataBean != null)
                {
                    good.setText("赞" + suiJiCommentDataBean.getGoods());
                    ((TextView)main.findViewById(R.id.commentText)).setText(suiJiCommentDataBean.getComment_message());
                    if(suiJiCommentDataBean.getNickName() != null)
                    {
                        ((TextView)main.findViewById(R.id.messageShowUserName)).setText(suiJiCommentDataBean.getNickName());
                    }
                    else
                    {
                        ((TextView)main.findViewById(R.id.messageShowUserName)).setText(suiJiCommentDataBean.getComment_username());
                    }

                    if(suiJiCommentDataBean.getUserHead() != null)
                    {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                Base64.decode(suiJiCommentDataBean.getUserHead(), Base64.NO_WRAP));
                        ((ImageView)main.findViewById(R.id.messageShowHeadImage)).setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                    }
                    reply.setText("回复" + suiJiCommentDataBean.getReply_number());
                }
            }break;
            case 1:
            {
                 if(articleCommentDataBean != null)
                 {
                     good.setText("赞" + articleCommentDataBean.getGoods());
                     ((TextView)main.findViewById(R.id.commentText)).setText(articleCommentDataBean.getComment_message());
                     if(articleCommentDataBean.getNickName() != null)
                     {
                         ((TextView)main.findViewById(R.id.messageShowUserName)).setText(articleCommentDataBean.getNickName());
                     }
                     else
                     {
                         ((TextView)main.findViewById(R.id.messageShowUserName)).setText(articleCommentDataBean.getComment_username());
                     }

                     if(articleCommentDataBean.getUserHead() != null)
                     {
                         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                 Base64.decode(articleCommentDataBean.getUserHead(), Base64.NO_WRAP));
                         ((ImageView)main.findViewById(R.id.messageShowUserName)).setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                     }

                     reply.setText("回复" + articleCommentDataBean.getReply_number());
                 }
            }break;
        }
    }

    //获取数据
   private void getData()
   {
       switch (type)
       {
           case 0:
           {
               if (suiJiCommentDataBean != null) {
                   Runnable runnable = () ->
                   {
                       Map<Integer, JSONObject> data = CommentAndReplyAndResetDoUtil.
                               getComment(suiJiCommentDataBean.getSuiji_comment_id(),
                               2);

                       for (Integer key : data.keySet()) {
                           SuiJiReplyBean suiJiReplyBean = new SuiJiReplyBean();
                           try {
                               JSONObject jsonObject = new JSONObject(data.get(key).toString());
                               suiJiReplyBean.setReply_id(jsonObject.getInt("id"));
                               suiJiReplyBean.setReply_message(jsonObject.getString("reply_message"));
                               suiJiReplyBean.setReply_username(jsonObject.getString("username"));
                               suiJiReplyBean.setTo_username(jsonObject.getString("to_username"));
                               suiJiReplyBean.setNickName(jsonObject.getString("nickName"));
                               suiJiReplyBean.setToNickName(jsonObject.getString("to_nickName"));
//                                suiJiReplyBean.setSuiji_comment_id(jsonObject.getInt("suiji_comment_id"));
                               oneWholeCommentAdapter.suiJiReplyBeanMap.put(key, suiJiReplyBean);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }

                   };
                   executorService.execute(runnable);
                   new CountDownTimer(20000, 2500)
                   {

                       @Override
                       public void onTick(long millisUntilFinished)
                       {
                           oneWholeCommentAdapter.notifyDataSetChanged();
                       }

                       @Override
                       public void onFinish()
                       {

                       }
                   }.start();
               }
           }break;
           case 1:
           {
               if(articleCommentDataBean != null)
               {
                   Runnable runnable = () ->
                   {
                       Map<Integer, JSONObject> data = CommentAndReplyAndResetDoUtil.getComment(articleCommentDataBean.getArticle_comment_id(),
                               3);

                       for(Integer key : data.keySet())
                       {
                           ArticleReplyBean articleReplyBean = new ArticleReplyBean();
                           try
                           {
                               JSONObject jsonObject = new JSONObject(data.get(key).toString());
                               System.out.println(jsonObject.toString());
                               articleReplyBean.setReply_id(jsonObject.getInt("id"));
                               articleReplyBean.setReply_message(jsonObject.getString("reply_message"));
                               articleReplyBean.setReply_username(jsonObject.getString("username"));
                               articleReplyBean.setTo_username(jsonObject.getString("to_username"));
                               articleReplyBean.setNickName(jsonObject.getString("nickName"));
                               articleReplyBean.setToNickName(jsonObject.getString("to_nickName"));
//                               articleReplyBean.setArticle_comment_id(jsonObject.getInt("article_comment_id"));
                               oneWholeCommentAdapter.articleReplyBeanMap.put(key, articleReplyBean);
                           }
                           catch (Exception e)
                           {
                               e.printStackTrace();
                           }
                       }

                   };
                   executorService.execute(runnable);

                   new CountDownTimer(20000, 2500)
                   {

                       @Override
                       public void onTick(long millisUntilFinished)
                       {
                           oneWholeCommentAdapter.notifyDataSetChanged();
                       }

                       @Override
                       public void onFinish()
                       {

                       }
                   }.start();
               }

           }break;
       }
   }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                dismiss();
            }break;
            case R.id.reply:
            {
                String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                switch (type)
                {
                    case 0:
                    {
                        if(suiJiCommentDataBean != null)
                        {
                            if(suiJiCommentDataBean.getNickName() != null)
                            {
                                ToReplyDialog toReplyDialog = new ToReplyDialog(context, 2, 0,username,
                                        suiJiCommentDataBean.getNickName(), suiJiCommentDataBean.getSuiji_comment_id());
                                toReplyDialog.show();
                            }
                            else
                            {
                                ToReplyDialog toReplyDialog = new ToReplyDialog(context, 2, 0,username,
                                        suiJiCommentDataBean.getComment_username(), suiJiCommentDataBean.getSuiji_comment_id());
                                toReplyDialog.show();
                            }
                        }
                    }break;
                    case 1:
                    {
                        if(articleCommentDataBean != null)
                        {
                            if(articleCommentDataBean.getNickName() != null)
                            {
                                ToReplyDialog toReplyDialog = new ToReplyDialog(context, 2, 0,username,
                                        articleCommentDataBean.getNickName(), articleCommentDataBean.getArticle_comment_id());
                                toReplyDialog.show();
                            }
                            else
                            {
                                ToReplyDialog toReplyDialog = new ToReplyDialog(context, 2, 0,username,
                                        articleCommentDataBean.getComment_username(), articleCommentDataBean.getArticle_comment_id());
                                toReplyDialog.show();
                            }
                        }
                    }break;
                }
            }break;
            case R.id.good:
            {
                if(goodChose % 2 == 0)
                {
                    String a = good.getText().toString().replace("赞", "");
                    int x = !a.equals("") ? Integer.parseInt(a) + 1 : 1;
                    good.setText("赞" + x);
                    good.setBackgroundColor(Color.parseColor("#456123"));
                    goodChose = 1;
                }
                else
                {
                    String a = good.getText().toString().replace("赞", "");
                    int x = !a.equals("") ? (Integer.parseInt(a) > 0 ? Integer.parseInt(a) - 1 : 0) : 0;
                    good.setText("赞" + x);
                    good.setBackgroundColor(Color.parseColor("#000000"));
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
}
