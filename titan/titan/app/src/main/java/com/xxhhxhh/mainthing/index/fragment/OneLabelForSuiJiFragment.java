package com.xxhhxhh.mainthing.index.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.view.*;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.index.dataload.ArticleDataLoad;
import com.xxhhxhh.mainthing.index.dataload.SuiJiDataLoad;
import com.xxhhxhh.mainthing.index.handler.SuiJiLoadHandler;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OneLabelForSuiJiFragment extends Fragment
    implements View.OnTouchListener
{
    public static String label_item;//标签
    private LinearLayout main;
    private float theY;//松开位置
    //适配器
    public SuiJiAdapter suiJiAdapter;
    //顶部刷新
    private LinearLayout topRefresh;
    //底部加载条
    private LinearLayout bottomLoadTip;
    private RecyclerView views;
    //ui更新
    public SuiJiLoadHandler handler;
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    public String username;
    public LoadTimer loadTimer;

    public OneLabelForSuiJiFragment(int label_item)
    {
        OneLabelForSuiJiFragment.label_item = SuiJiFragment.labels.get(
                Math.min(label_item - 2 < 0 ? 0 : label_item, SuiJiFragment.labels.size() - 1));
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
        views = new RecyclerView(getContext());
        suiJiAdapter = new SuiJiAdapter(getContext(), views, this);
        executorService.execute(new SuiJiDataLoad(suiJiAdapter, username, 3, 0));
        loadTimer = new LoadTimer(5000, 1000, suiJiAdapter);
        loadTimer.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        handler = new SuiJiLoadHandler(suiJiAdapter, this);
        main = new LinearLayout(getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        topRefresh = (LinearLayout) inflater.inflate(R.layout.top_refresh, container, false);
        main.addView(topRefresh);
        initSuiJi();

        return main;
    }



    //初始化随记
    private void initSuiJi()
    {
//        views.setOnTouchListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        views.setLayoutManager(manager);
        views.setAdapter(suiJiAdapter);
        views.setId(R.id.views);
        main.addView(views);
        views.addOnScrollListener(new RecyclerView.OnScrollListener()
        {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {

                    int nowSize = suiJiAdapter.getSize();
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
                        handler.setStart(nowSize - 1);
                        handler.post(handler);
                    }
                    else if(nowSize == 6)
                    {
                        suiJiAdapter.isFinally = true;
                        suiJiAdapter.notifyItemInserted(nowSize);
                        suiJiAdapter.notifyItemChanged(nowSize);
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
                    if(event.getY() <= 800 && event.getY() > 350 && views.getScrollY() < 5)
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
                        suiJiAdapter.notifyItemRangeRemoved(0, suiJiAdapter.getItemCount());
                        suiJiAdapter.dataBeans.clear();
                        suiJiAdapter.isFinally = false;
                        executorService.execute(new SuiJiDataLoad(suiJiAdapter, username, 3, 0));
                        handler.setI(0);
                        loadTimer = new LoadTimer(5000, 1000, suiJiAdapter);
                        loadTimer.start();
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

        private SuiJiAdapter suiJiAdapter;

        public LoadTimer(long millisInFuture, long countDownInterval, SuiJiAdapter suiJiAdapter)
        {
            super(millisInFuture, countDownInterval);
            this.suiJiAdapter = suiJiAdapter;
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            suiJiAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish()
        {

        }
    }
}
