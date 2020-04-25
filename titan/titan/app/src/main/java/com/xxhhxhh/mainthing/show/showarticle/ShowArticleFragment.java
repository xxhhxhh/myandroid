package com.xxhhxhh.mainthing.show.showarticle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.ShowArticleActivity;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.ToReplyPopUpWindow;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.mainthing.show.handler.LoadArticleHandler;
import com.xxhhxhh.mainthing.show.showsuiji.ShowSuiJiFragment;
import com.xxhhxhh.mainthing.show.util.GiveGood;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;

import java.util.HashMap;
import java.util.Map;

public class ShowArticleFragment extends Fragment
    implements View.OnClickListener
{

    private Button webBack;//网页视图返回
    private WebView webView;//网页视图
    private EditText editReplyOrComment;//编辑评论或回复
    private LinearLayout main;//根视图
    private String url = "http://10.0.2.2:8080";
    public ArticleDataBean articleDataBean;
    private String username;
    private int goodChose;//点赞标识
    private Button good;//点赞按钮
    private int nowChose = 0;//单击次数检测，超过一次不再开启计时
    private GoodWillUpdate goodWillUpdate = new GoodWillUpdate(20000, 2000);
    private int type = 0;//0直接显示，1查询显示

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        type = intent.getIntExtra("type", 0);
        if(type == 0)
        {
            articleDataBean = ((ShowArticleActivity)getActivity()).getArticleDataBean();
            url += articleDataBean != null ? articleDataBean.getUrl() : "";

        }
        else if(type == 1)
        {
            int article_id = intent.getIntExtra("id", 0);
            LoadArticleHandler loadArticleHandler = new LoadArticleHandler(article_id, this);
            loadArticleHandler.sendEmptyMessage(123);
        }

        new Thread()
        {
            @Override
            public void run()
            {
                if(articleDataBean.getLabels() != null)
                {
                    saveLabelLook();
                }
                saveLook();
            }
        }.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
        main = new LinearLayout(getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        if(type == 0)
        {
           init();
        }
        return main;
    }

    //视图初始化
    public void init()
    {
        //标题
        TextView title = new TextView(getContext());
        title.setTextSize(20);
        title.setText(articleDataBean != null && articleDataBean.getArticle_title() != null ? articleDataBean.getArticle_title() : "");
        LinearLayout userLine = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.article_user_line, null);
        //用户名
        if(articleDataBean != null)
        {
            if(articleDataBean.getNickName().equals(""))
            {
                ((TextView)userLine.findViewById(R.id.showUsername)).setText(articleDataBean.getUsername());
            }
            else
            {
                ((TextView)userLine.findViewById(R.id.showUsername)).setText(articleDataBean.getNickName());
            }
        }
        //标签
        if(articleDataBean != null)
        {
            ((TextView)userLine.findViewById(R.id.showLabel)).setText(articleDataBean.getLabels());
        }
        LinearLayout backLine =  new LinearLayout(getContext());
        backLine.setOrientation(LinearLayout.HORIZONTAL);
        backLine.setMinimumHeight(70);
        main.addView(userLine);
        main.addView(title);
        webBack = new Button(getContext());
        webBack.setHeight(70);
        webBack.setWidth(100);
        webBack.setText("网页回退");
        backLine.addView(webBack);
        webView = new WebView(getContext());
        webView.loadUrl(url);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        main.addView(backLine);
        main.addView(webView);
        editReplyOrComment = getActivity().findViewById(R.id.editReplyOrComment);
        editReplyOrComment.setHint("已有" + (articleDataBean != null ? articleDataBean.getComment_number() : "0") +"条评论");
        editReplyOrComment.setFocusable(false);
        ((Button)getActivity().findViewById(R.id.giveGood)).setText("赞" +(articleDataBean != null ? articleDataBean.getGoods() : "0") );
        good = ((Button)getActivity().findViewById(R.id.giveGood));
        good.setOnClickListener(this);
        initListener();
    }

    //初始化监听
    private void initListener()
    {
        getActivity().findViewById(R.id.goBack).setOnClickListener(this);
        editReplyOrComment.setOnClickListener(this);
        webBack.setOnClickListener(this);
    }

    //随记查看存储
    public void saveLook()
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_LOOK);
        Map<String, String> data = new HashMap<>();
        if(articleDataBean != null)
        {
            data.put("id", String.valueOf(articleDataBean.getArticle_id()));
            data.put("type", String.valueOf(1));
            httpsUtil.setSendData(data);

            try
            {
                httpsUtil.sendPost();
                httpsUtil.getResponse().close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //标签观看存储
    public void saveLabelLook()
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_LABEL_LOOK);
        Map<String, String> data = new HashMap<>();
        data.put("data", articleDataBean.getLabels().toString());
        String username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();

        if(username != null && !username.equals(""))
        {
            data.put("username", ((UserInfoApplocation)getActivity().getApplication()).getUsername());
        }
        else
        {
            data.put("username", "");
        }

        data.put("type", "1");
        data.put("type2", "0");

        httpsUtil.setSendData(data);

        try
        {
            httpsUtil.sendPost();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            httpsUtil.getResponse().close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                getActivity().finish();
            }break;
            case R.id.editReplyOrComment:
            {
                editReplyOrComment.setFocusable(true);
                if(UserLogin.IS_LOGIN)
                {
                    if(username != null && !username.equals(""))
                    {
                        if(articleDataBean != null && articleDataBean.getUsername() != null && !articleDataBean.getUsername().equals(""))
                        {
                            ToReplyPopUpWindow popUpWindow = new ToReplyPopUpWindow(getContext(), "评论", username, articleDataBean.getUsername()
                                    , articleDataBean.getArticle_id(), 0);
                            popUpWindow.showAtLocation(editReplyOrComment, Gravity.BOTTOM, 0, 0);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "被用户名错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "用户名错误", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }

            }break;
            case R.id.giveGood:
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

                    GiveGood.giveGood(articleDataBean.getArticle_id(),
                            2);
                };
                runnable.run();
            }
        }
    }
}
