package com.xxhhxhh.mainthing.index.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.index.adpter.ArticleAdapter;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.index.dataload.ArticleDataLoad;
import com.xxhhxhh.mainthing.index.handler.ArticleLoadHandler;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OneLabelForArticleFragment extends Fragment
    implements View.OnTouchListener
{
    public static String label_item = "";//标签
    private LinearLayout main;
    private float theY;//松开位置
    //适配器
    public ArticleAdapter articleAdapter;
    //顶部刷新
    private LinearLayout topRefresh;
    //底部加载条
    private LinearLayout bottomTip;
    private RecyclerView views;
    public ArticleLoadHandler handler;
    //内容获取统计
    public ExecutorService executePools = Executors.newSingleThreadExecutor();
    public String username;
    public LoadTimer loadTimer;

    public OneLabelForArticleFragment(int label_item)
    {
        OneLabelForArticleFragment.label_item = ArticleFragment.labels.get(
                Math.min(label_item - 2 < 0 ? 0 : label_item, SuiJiFragment.labels.size() - 1));
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
        views = new RecyclerView(getContext());
        articleAdapter = new ArticleAdapter(getContext(), views, this);
        articleAdapter.setHasStableIds(true);
        executePools.execute(new ArticleDataLoad(articleAdapter, username, 3, 0));
        loadTimer = new LoadTimer(5000, 1000, articleAdapter);
        loadTimer.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        main = new LinearLayout(getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        topRefresh = (LinearLayout) inflater.inflate(R.layout.top_refresh, null);
        main.addView(topRefresh);
        handler = new ArticleLoadHandler();
        handler.oneLabelForArticleFragment = this;
        initArticle();
        return main;
    }


    //初始化文章
    private void initArticle()
    {
        views.setOnTouchListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        views.setId(R.id.views);
        views.setLayoutManager(manager);
        views.setAdapter(articleAdapter);
        main.addView(views);
        views.setItemViewCacheSize(0);
        views.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    int nowSize = articleAdapter.getSize();
                    System.out.println(nowSize);
                    if(nowSize < 6)
                    {

                        if(6 - nowSize < 3)
                        {
                            handler.setLoadNumber(6 - nowSize + 1);
                        }
                        else
                        {
                            handler.setLoadNumber(3);
                        }
                        handler.setStart(nowSize);
                        handler.post(handler);
                    }
                    else if(nowSize  == 6)
                    {
                        handler.setStart(nowSize);
                        handler.post(handler);
                        articleAdapter.isFinally = true;
                        articleAdapter.notifyItemInserted(nowSize );
                        articleAdapter.notifyItemChanged(nowSize );
                    }
                }
            }
        });
    }



    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_MOVE:
                {
                    if(event.getY() <= 600 && event.getY() > 350 && views.getScrollY() < 5)
                    {
                        topRefresh.setVisibility(View.VISIBLE);
                        topRefresh.setMinimumHeight((int) event.getY() - 350);
                        theY = event.getY();
                        v.performClick();
                        return true;
                    }
                }
                case MotionEvent.ACTION_UP:
                {
                    topRefresh.setVisibility(View.GONE);
                    if(Math.abs(event.getY() - theY) <= 300 && event.getY() > 350)
                    {
                        views.scrollToPosition(0);
                        articleAdapter.notifyItemRangeRemoved(0, articleAdapter.getItemCount());
                        articleAdapter.isFinally = false;
                        articleAdapter.dataBeans.clear();
                        executePools.execute(new ArticleDataLoad(articleAdapter, username, 3, 0));
                        loadTimer = new LoadTimer(5000, 1000, articleAdapter);
                        loadTimer.start();
                        handler.setI(0);
                        v.performClick();
                        return true;
                    }

                }
            }
        v.performClick();
        return false;
    }


    public static class LoadTimer extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        private ArticleAdapter articleAdapter;

        public LoadTimer(long millisInFuture, long countDownInterval, ArticleAdapter articleAdapter)
        {
            super(millisInFuture, countDownInterval);
            this.articleAdapter = articleAdapter;
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            articleAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish()
        {

        }
    }
}
