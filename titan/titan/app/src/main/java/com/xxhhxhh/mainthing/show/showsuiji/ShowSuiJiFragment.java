package com.xxhhxhh.mainthing.show.showsuiji;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.*;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.ShowSuiJiActivity;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanParceable;
import com.xxhhxhh.mainthing.popup.MenuPopUpWindow;
import com.xxhhxhh.mainthing.show.commentsandreply.adapter.ShowCommentsAdapter;
import com.xxhhxhh.mainthing.show.commentsandreply.loaddata.SuiJiCommentLoad;
import com.xxhhxhh.mainthing.show.dialog.ToReplyDialog;
import com.xxhhxhh.mainthing.show.handler.LoadSuiJiHandler;
import com.xxhhxhh.mainthing.show.util.FocusAndToBlacklist;
import com.xxhhxhh.mainthing.show.util.GiveGood;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowSuiJiFragment extends Fragment
    implements View.OnClickListener
{

    //主视图
    private LinearLayout main;
    public SuiJiDataBeanParceable suiJiDataBean;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    public boolean  isFocus;//是否被关注
    public String username = "";
    private ShowCommentsAdapter showCommentsAdapter;
    private int goodChose;//点赞标识
    private Button good;//点赞按钮
    private int nowChose = 0;//单击次数检测，超过一次不再开启计时
    private GoodWillUpdate goodWillUpdate = new GoodWillUpdate(20000, 2000);
    private int type = 0;//加载模式，0单击加载 1查询加载
    private LoadSuiJiHandler loadSuiJiHandler;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        Intent intent = getActivity().getIntent();
        type = intent.getIntExtra("type", 0);
        username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
        if(username == null)
        {
            username = "";
        }

        if(type == 0)
        {
            suiJiDataBean = (SuiJiDataBeanParceable) intent.getParcelableExtra("data");
        }
        else if(type == 1)
        {
            loadSuiJiHandler = new LoadSuiJiHandler(intent.getIntExtra("id", 0), this);
            loadSuiJiHandler.sendEmptyMessage(123);
        }

        showCommentsAdapter = new ShowCommentsAdapter(0, this);
        showCommentsAdapter.setHasStableIds(true);

    }


    //查询关注信息
    public void getFocus()
    {
        if(username != null)
        {
            Runnable runnable = ()->
            {
                if(FocusAndToBlacklist.getUserRelated(0, username, 1, suiJiDataBean.getUsername()).size() > 0)
                {
                    isFocus = true;
                }

            };
            executorService.execute(runnable);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ScrollView scrollView = new ScrollView(getContext());
        main = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.show_suiji, container, false);
        new GoodWillUpdate(10000, 1000).start();
        LinearLayout showComments = main.findViewById(R.id.showComments);
        showComments.addView(initComments());
        ((ShowSuiJiActivity)getActivity()).setNowItem(0);
        //返回
        Button goBack = main.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);

        if(username == null)
        {
            username = "";
        }
        else if(!username.equals(""))
        {
            if(type == 0)
            {
                getFocus();
            }
        }

        if(type == 0)
        {
            init();
            getComments();
        }


        scrollView.addView(main);
        return scrollView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(type == 0)
        {
            Runnable runnable = () -> {
                saveLabelLook();
                saveLook();
            };
            executorService.execute(runnable);
        }
    }

    //随记查看存储
    public void saveLook()
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_LOOK);
        Map<String, String> data = new HashMap<>();
        if(suiJiDataBean != null)
        {
            data.put("id", String.valueOf(suiJiDataBean.getSuiji_id()));
            data.put("type", String.valueOf(0));
            httpsUtil.setSendData(data);

            try
            {
                Call call = httpsUtil.sendEnquene();
                call.enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                            response.close();
                    }
                });
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
        if(suiJiDataBean != null)
        {
            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_LABEL_LOOK);
            Map<String, String> data = new HashMap<>();
            data.put("data", suiJiDataBean.getLabels().toString());
            String username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();

            if(username != null && !username.equals(""))
            {
                data.put("username", ((UserInfoApplocation)getActivity().getApplication()).getUsername());
            }
            else
            {
                data.put("username", "");
            }

            data.put("type", "0");
            data.put("type2", "0");

            httpsUtil.setSendData(data);

            try
            {
                Call call = httpsUtil.sendEnquene();
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        response.close();
                    }
                });
                call.cancel();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //初始化
    public void init()
    {
        //关注
        Button focus= main.findViewById(R.id.focus);
        focus.setOnClickListener(this);
        focus.setVisibility(View.VISIBLE);
        //点赞
        good = main.findViewById(R.id.giveGood);
        good.setOnClickListener(this);
        //下拉框
        Button report = main.findViewById(R.id.report);
        report.setOnClickListener(this);
        //分享
        Button share = main.findViewById(R.id.share);
        share.setOnClickListener(this);
        //评论
        Button comment = main.findViewById(R.id.comment);
        comment.setOnClickListener(this);
        if(isFocus)
        {
            focus.setText("关注");
        }
        else
        {
            focus.setText("已关注");
        }

        if(suiJiDataBean != null)
        {
            comment.setText("评论" + suiJiDataBean.getComment_number());
            if(suiJiDataBean.getBitmapMap() != null && suiJiDataBean.getHave_file() > 0 && suiJiDataBean.getFileType() == 0)
            {
                main.findViewById(R.id.pictureLook).setVisibility(View.VISIBLE);
                initPhotos();
            }
        }


        //
        TextView mainMessaage = main.findViewById(R.id.messageSuiJi);
        mainMessaage.setMinHeight(200);
        mainMessaage.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mainMessaage.setText(suiJiDataBean.getMain_message());
        ((TextView)main.findViewById(R.id.theDate)).setText("发表日期:" + suiJiDataBean.getThe_date());

        if(suiJiDataBean != null)
        {
            if(suiJiDataBean.getNickName() != null && suiJiDataBean.getNickName().equals(""))
            {
                ((TextView)main.findViewById(R.id.messageShowUserName)).setText(suiJiDataBean.getUsername());
            }
            else
            {
                ((TextView)main.findViewById(R.id.messageShowUserName)).setText(suiJiDataBean.getNickName());
            }
            ((Button)main.findViewById(R.id.giveGood)).setText("赞" + suiJiDataBean.getGoods());
            if(suiJiDataBean.getUserHead() != null)
            {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(suiJiDataBean.getUserHead(), Base64.NO_WRAP));
                ((ImageView)main.findViewById(R.id.messageShowHeadImage)).
                        setImageBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
            }
        }

    }

    //评论初始化
    private RecyclerView initComments()
    {
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(showCommentsAdapter);
        recyclerView.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutManager(linearLayoutManager);

        return recyclerView;
    }

    //获取评论
    public void getComments()
    {
        executorService.execute(new SuiJiCommentLoad(showCommentsAdapter, suiJiDataBean.getSuiji_id()));
        new CountDownTimer(20000, 2500)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {
                showCommentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish()
            {

            }
        }.start();
    }

    //图片初始化
    public void initPhotos()
    {
        switch (suiJiDataBean.getBitmapMap().size())
        {

            case 1:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
            }break;
            case 2:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage2)).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
            }break;
            case 3:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
            }break;
            case 4:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
            }break;
            case 5:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
                ((ImageView)main.findViewById(R.id.mainImage5)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(4));
            }break;
            case 6:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
                ((ImageView)main.findViewById(R.id.mainImage5)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(4));
                ((ImageView)main.findViewById(R.id.mainImage6)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(5));
            }break;
            case 7:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row3).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
                ((ImageView)main.findViewById(R.id.mainImage5)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(4));
                ((ImageView)main.findViewById(R.id.mainImage6)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(5));
                ((ImageView)main.findViewById(R.id.mainImage7)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(6));
            }break;
            case 8:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row3).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
                ((ImageView)main.findViewById(R.id.mainImage5)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(4));
                ((ImageView)main.findViewById(R.id.mainImage6)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(5));
                ((ImageView)main.findViewById(R.id.mainImage7)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(6));
                ((ImageView)main.findViewById(R.id.mainImage8)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(7));

            }break;
            case 9:
            {
                main.findViewById(R.id.row1).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row2).setVisibility(View.VISIBLE);
                main.findViewById(R.id.row3).setVisibility(View.VISIBLE);
                ((ImageView)main.findViewById(R.id.mainImage1)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(0));
                ((ImageView)main.findViewById(R.id.mainImage2)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(1));
                ((ImageView)main.findViewById(R.id.mainImage3)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(2));
                ((ImageView)main.findViewById(R.id.mainImage4)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(3));
                ((ImageView)main.findViewById(R.id.mainImage5)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(4));
                ((ImageView)main.findViewById(R.id.mainImage6)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(5));
                ((ImageView)main.findViewById(R.id.mainImage7)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(6));
                ((ImageView)main.findViewById(R.id.mainImage8)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(7));
                ((ImageView)main.findViewById(R.id.mainImage9)).
                        setImageBitmap(suiJiDataBean.getBitmapMap().get(8));
            }break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //返回
            case R.id.goBack:
            {
                getActivity().finish();
            }break;
            case R.id.report:
            {

                Map<String, String> data = new HashMap<>();
                data.put("username", suiJiDataBean.getUsername());
                data.put("label", suiJiDataBean.getLabels().toString());
                MenuPopUpWindow popUpWindow = new MenuPopUpWindow(getContext(), data, username);
                popUpWindow.showAsDropDown(((Button)getActivity().findViewById(R.id.report)));


            }break;
            case R.id.focus:
            {
                if(UserLogin.IS_LOGIN)
                {

                    if(!suiJiDataBean.getUsername().equals(((UserInfoApplocation)getActivity().getApplication()).getUsername()))
                    {
                        if(isFocus)
                        {
                            Runnable runnable = () ->FocusAndToBlacklist.doFocusAndBlacklist(0,
                                    ((UserInfoApplocation)getActivity().getApplication()).getUsername(),
                                    suiJiDataBean.getUsername());
                            executorService.execute(runnable);
                            ((Button)main.findViewById(R.id.focus)).setText("已关注");
                        }
                        else
                        {
                            Runnable runnable = () ->FocusAndToBlacklist.doFocusAndBlacklist(2,
                                    ((UserInfoApplocation)getActivity().getApplication()).getUsername(),
                                    suiJiDataBean.getUsername());
                            executorService.execute(runnable);
                            ((Button)main.findViewById(R.id.focus)).setText("关注");
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "不能关注自己", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    UserLogin.getLogin(getContext());
                }
            }break;
            case R.id.share:
            {

            }break;
            case R.id.comment:
            {
                System.out.println(124);
                if(UserLogin.IS_LOGIN)
                {
                    String username = ((UserInfoApplocation)getActivity().getApplication()).getUsername();
                    if(username != null && !username.equals(""))
                    {
                        ToReplyDialog toReplyDialog = new ToReplyDialog(getContext(), 0, 0, username, "",
                                suiJiDataBean.getSuiji_id());
                        toReplyDialog.setTitle("评论");
                        toReplyDialog.show();
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


    @Override
    public void onDestroyView()
    {
        //触发点赞存储
        if(goodChose % 2 == 1)
        {
            Runnable runnable = () ->
            {

                GiveGood.giveGood(suiJiDataBean.getSuiji_id(),
                        0);

            };
            executorService.execute(runnable);
        }
        super.onDestroyView();
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
            if(goodChose % 2 == 1)
            {
                Runnable runnable = () ->
                {

                    GiveGood.giveGood(suiJiDataBean.getSuiji_id(),
                            0);

                };
                executorService.execute(runnable);
            }
        }
    }


}
