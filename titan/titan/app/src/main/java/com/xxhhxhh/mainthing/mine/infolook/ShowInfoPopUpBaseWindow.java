package com.xxhhxhh.mainthing.mine.infolook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.databean.RelatedBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanSimple;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.mainthing.mine.adapter.*;
import com.xxhhxhh.mainthing.show.util.FocusAndToBlacklist;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowInfoPopUpBaseWindow extends PopupWindow
    implements View.OnClickListener
{
    private Context context;
    public LinearLayout rootView;
    public RecyclerView recyclerView;//数据显示视图
    private int type;
    private SuiJiShowAdapter suiJiShowAdapter;
    private ArticleShowAdapter articleShowAdapter;
    private BlackListAdapter blackListAdapter;
    private FocusAdapter focusAdapter;
    private String username;
    private LoadThread loadThread;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private int nowLocation;//当前已查询个数

    public ShowInfoPopUpBaseWindow(Context context, int type, String username)
    {
        nowLocation = 0;
        this.context = context;
        this.type = type;
        this.username = username;
        //数据显示试图配置
        recyclerView = new RecyclerView(context);
        rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout backLine = new LinearLayout(context);
        backLine.setOrientation(LinearLayout.HORIZONTAL);
        Button button = new Button(context);
        button.setOnClickListener(this);
        button.setWidth(80);
        button.setHeight(80);
        button.setText("返回");
        button.setId(R.id.goBack);
        backLine.addView(button);
        rootView.addView(backLine);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        rootView.addView(recyclerView);
        rootView.setBackgroundColor(Color.parseColor("#000095"));
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(rootView);
        initAdapter();
        loadThread = new LoadThread();
        loadThread.start();
    }

    //设置adapter
    private void initAdapter()
    {
        switch (type)
        {
            //随记
            case 1:
            {
                suiJiShowAdapter = new SuiJiShowAdapter();
                new LoadCountDownTimer(100000, 2500).start();
                recyclerView.setAdapter(suiJiShowAdapter);
            }break;
            //文章
            case 2:
            {
                articleShowAdapter = new ArticleShowAdapter();
                new LoadCountDownTimer(100000, 2500).start();
                recyclerView.setAdapter(articleShowAdapter);
            }break;
            //关注
            case 3:
            {
                focusAdapter = new FocusAdapter(type);
                new LoadCountDownTimer(100000, 2500).start();
                recyclerView.setAdapter(focusAdapter);
            }break;
            //黑名单
            case 4:
            {
                blackListAdapter = new BlackListAdapter(type);
                new LoadCountDownTimer(100000, 2500).start();
                recyclerView.setAdapter(blackListAdapter);
            }break;
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.goBack)
        {
            dismiss();
        }
    }


    class LoadCountDownTimer extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public LoadCountDownTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            switch (type)
            {
                //随记
                case 1:
                {
                    suiJiShowAdapter.notifyDataSetChanged();
                }break;
                //文章
                case 2:
                {
                    articleShowAdapter.notifyDataSetChanged();
                }break;
                //关注
                case 3:
                {
                    focusAdapter.notifyDataSetChanged();
                }break;
                //黑名单
                case 4:
                {
                    blackListAdapter.notifyDataSetChanged();
                }break;
            }
        }

        @Override
        public void onFinish()
        {

        }
    }

    public class LoadThread extends Thread
    {
        private LoadHandler loadHandler;


        @Override
        public void run() {
            Looper.prepare();
            loadHandler = new LoadHandler();
            switch (type)
            {
                //随记
                case 1:
                {
                    loadThread.loadHandler.sendEmptyMessage(789);
                }break;
                //文章
                case 2:
                {
                    loadThread.loadHandler.sendEmptyMessage(101112);

                }break;
                //关注
                case 3:
                {
                    loadThread.loadHandler.sendEmptyMessage(456);
                }break;
                //黑名单
                case 4:
                {
                    loadThread.loadHandler.sendEmptyMessage(123);
                }break;
            }
            Looper.loop();
        }

        class LoadHandler extends Handler
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                if(msg.what == 123)
                {
                    getBlackList();
                }
                else if(msg.what == 456)
                {
                    getFoucs();
                }
                else if(msg.what == 789)
                {

                    Runnable runnable = this::getSUiJiForUser;
                    executorService.execute(runnable);

                }
                else if(msg.what == 101112)
                {
                    Runnable runnable = this::getArticleForUser;
                    executorService.execute(runnable);

                }
            }

            //获取黑名单
            private void getBlackList()
            {

                Map<String, String> data = FocusAndToBlacklist.getUserRelated(1, username, 0, "");

                int i = 0;
                for(String key : data.keySet())
                {
                    RelatedBean relatedBean = new RelatedBean();
                    relatedBean.setToUsername(key);
                    relatedBean.setType(1);
                    relatedBean.setUsername(username);
                    blackListAdapter.getBeanMap().put(i, relatedBean);
                    i++;
                }

            }

            //获取关注
            private void getFoucs()
            {
                Map<String, String> data = FocusAndToBlacklist.getUserRelated(0, username, 0, "");

                int i = 0;
                for(String key : data.keySet())
                {
                    RelatedBean relatedBean = new RelatedBean();
                    relatedBean.setToUsername(key);
                    relatedBean.setType(0);
                    relatedBean.setUsername(username);
                    focusAdapter.getBeanMap().put(i, relatedBean);
                    i++;
                }
            }

            //获取文章
            private void getArticleForUser()
            {
                for(int i = 0; i < 10; i++)
                {

                        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_ARTICLE);
                    //数据
                    Map<String, String> data = new HashMap<>();
                    data.put("the_label", "");
                    data.put("username", username != null ?
                            username : "");
                    data.put("type", "4");
                    data.put("start", String.valueOf(i));
                    httpsUtil.setSendData(data);
                    //发送
                    httpsUtil.sendPost();

                    try
                    {
                        String result = httpsUtil.getResponse().body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        System.out.println(jsonObject.toString());
                        if (!jsonObject.get("theUrl").equals("null"))
                        {

                            ArticleDataBean articleDataBean = new ArticleDataBean();
                            articleDataBean.setArticle_id(jsonObject.getInt("the_id"));
                            articleDataBean.setArticle_title(jsonObject.getString("articleTitle"));
                            articleDataBean.setComment_number(jsonObject.getInt("commentNumber"));
                            articleDataBean.setGoods(jsonObject.getInt("goods"));
                            articleDataBean.setLocation(jsonObject.getString("location"));
                            articleDataBean.setLooks_number(jsonObject.getInt("looksNumber"));
                            articleDataBean.setThe_date(jsonObject.getString("theDate").substring(0, 10));
                            articleDataBean.setUrl(jsonObject.getString("theUrl"));
                            articleDataBean.setLabels(jsonObject.getString("label").equals("[]") ?
                                    new JSONArray() : new JSONArray(jsonObject.getString("label")));

                            //判断并添加数据
                            if (articleDataBean.getArticle_title().equals("") && articleDataBean.getArticle_title().equals("null"))
                            {
                                articleDataBean.setArticle_title("");
                            }

                            if (articleDataBean.getThe_date().equals("") && articleDataBean.getThe_date().equals("null"))
                            {
                                articleDataBean.setThe_date("");
                            }

                            httpsUtil.getResponse().close();
                            articleShowAdapter.dataBeans.put(i, articleDataBean);
                        }
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }}
            }

            //获取随记
            private void getSUiJiForUser()
            {
                for(int i = 0; i < 10; i++)
                {

                    HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_SUIJI_MAIN);
                    //数据
                    Map<String, String> data = new HashMap<>();
                    data.put("the_label", OneLabelForSuiJiFragment.label_item != null ? OneLabelForSuiJiFragment.label_item : "");
                    data.put("username", username != null ?
                            username : "");
                    data.put("type", "4");
                    data.put("start", String.valueOf(i));
                    httpsUtil.setSendData(data);
                    //发送
                    httpsUtil.sendPost();

                    try
                    {
                        String result = httpsUtil.getResponse().body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        System.out.println(result);
                        //数据获取
                        if (!jsonObject.get("mainMessage").equals("null"))
                        {
                                SuiJiDataBeanSimple suiJiDataBean = new SuiJiDataBeanSimple();
                                suiJiDataBean.setComment_number(jsonObject.getInt("commentNumber"));
                                suiJiDataBean.setGoods(jsonObject.getInt("goods"));
                                suiJiDataBean.setHave_file(jsonObject.getInt("haveFile"));
                                suiJiDataBean.setMain_message(jsonObject.getString("mainMessage"));
                                suiJiDataBean.setLocation(jsonObject.getString("location"));
                                suiJiDataBean.setThe_date(jsonObject.getString("the_date").substring(0, 10));
                                suiJiDataBean.setSuiji_id(jsonObject.getInt("suiji_id"));
                                suiJiDataBean.setLooks_number(jsonObject.getInt("looks_number"));
                                suiJiDataBean.setUsername(jsonObject.getString("username"));
                                suiJiDataBean.setLabels(jsonObject.getString("labels").equals("[]") ?
                                        (new JSONArray()).toString() : new JSONArray(jsonObject.getString("labels")).toString());
                                suiJiDataBean.setFileType(jsonObject.getInt("fileType"));

                                if (suiJiDataBean.getUsername().equals("") || suiJiDataBean.getUsername().equals("null"))
                                {
                                    suiJiDataBean.setUsername("");
                                }

                                if (suiJiDataBean.getThe_date().equals("") && suiJiDataBean.getThe_date().equals("null"))
                                {
                                    suiJiDataBean.setThe_date("");
                                }


                                if (suiJiDataBean.getMain_message().equals("") && suiJiDataBean.getMain_message().equals("null"))
                                {
                                    suiJiDataBean.setMain_message("");
                                }

                                httpsUtil.getResponse().close();
                                suiJiShowAdapter.dataBeans.put(i, suiJiDataBean);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }}
            }
        }
    }
