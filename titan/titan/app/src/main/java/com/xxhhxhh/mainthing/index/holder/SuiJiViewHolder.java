package com.xxhhxhh.mainthing.index.holder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.ShowSuiJiActivity;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanParceable;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.mainthing.popup.MenuPopUpWindow;
import com.xxhhxhh.mainthing.show.commentsandreply.popup.ShowOneWholeCommentPopUpWindow;
import com.xxhhxhh.mainthing.show.util.GiveGood;
import com.xxhhxhh.transdata.requestcode.RequestCode;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuiJiViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, View.OnLongClickListener
{

    public View itemView;
    ImageView headImage;//头像
    TextView username;//用户名
    TextView userLabel;//用户标签
    Button focus;//关注
    Button report;//举报
    TextView messageSuiJi;//随记文本
    TextView label;//随记标签
    TextView date;//发表日期
    Button share;//分享
    Button comment;//评论
    Button good;//点赞
    private View tablePicture;
    private Fragment fragment;
    private GoodWillUpdate goodWillUpdate = new GoodWillUpdate(20000, 2000);
    private int nowChose = 0;//单击次数检测，超过一次不再开启计时
    private int goodChose = 0;


    @SuppressLint("WrongViewCast")
    public SuiJiViewHolder(@NonNull View itemView, int viewType, Fragment fragment)
    {
        super(itemView);
        setItemView(itemView);
        if(viewType == 0)
        {
            this.fragment = fragment;
            setHeadImage((ImageView) itemView.findViewById(R.id.messageShowHeadImage));
            setUsername((TextView) itemView.findViewById(R.id.messageShowUserName));
            setUserLabel((TextView) itemView.findViewById(R.id.messageShowUserLabel));
            setFocus((Button) itemView.findViewById(R.id.focus));
            setReport((Button) itemView.findViewById(R.id.report));
            setMessageSuiJi((TextView) itemView.findViewById(R.id.messageSuiJi));
            setLabel((TextView) itemView.findViewById(R.id.label));
            setDate((TextView) itemView.findViewById(R.id.theDate));
            setShare((Button) itemView.findViewById(R.id.share));
            setComment((Button) itemView.findViewById(R.id.comment));
            setGood((Button) itemView.findViewById(R.id.giveGood));
            tablePicture = itemView.findViewById(R.id.pictureLook);

            getItemView().setOnClickListener(this);
            getMessageSuiJi().setOnLongClickListener(this);
            getComment().setOnClickListener(this);
            getGood().setOnClickListener(this);
            getShare().setOnClickListener(this);
            getReport().setOnClickListener(this);
            getItemView().setOnClickListener(this);
            getMessageSuiJi().setOnClickListener(this);

        }
        else if(viewType == 1)
        {

        }
        else if(viewType == 2)
        {
        }
    }



    public void setItemView(View itemView) { this.itemView = itemView; }
    public void setHeadImage(ImageView headImage){ this.headImage = headImage; }
    public void setUsername(TextView username){this.username = username;}
    public void setUserLabel(TextView userLabel){ this.userLabel = userLabel; }
    public void setFocus(Button focus){ this.focus = focus; }
    public void setReport(Button report){ this.report = report; }
    public void setMessageSuiJi(TextView messageSuiJi){ this.messageSuiJi = messageSuiJi; }
    public void setLabel(TextView label){ this.label = label; }
    public void setDate(TextView date){ this.date = date; }
    public void setShare(Button share){ this.share = share; }
    public void setComment(Button comment){ this.comment = comment; }
    public void setGood(Button good){ this.good = good; }
    public Button getFocus() { return focus; }
    public TextView getLabel() { return label; }
    public Button getComment() { return comment; }
    public Button getReport() { return report; }
    public Button getGood() { return good; }
    public Button getShare() { return share; }
    public ImageView getHeadImage() { return headImage; }
    public TextView getDate() { return date; }
    public TextView getMessageSuiJi() { return messageSuiJi; }
    public TextView getUserLabel() { return userLabel; }
    public TextView getUsername() { return username; }
    public View getItemView() { return itemView; }
    public View getTablePicture() { return tablePicture; }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.report:
            {
                SuiJiDataBean suiJiDataBean = (SuiJiDataBean) getItemView().getTag();
                if(suiJiDataBean != null)
                {
                    if(UserLogin.IS_LOGIN)
                    {
                        String username = ((UserInfoApplocation)fragment.getActivity().getApplication()).getUsername();
                        Map<String, String> data = new HashMap<>();
                        data.put("username", suiJiDataBean.getUsername());
                        data.put("label", suiJiDataBean.getLabels().toString());
                        MenuPopUpWindow popUpWindow = new MenuPopUpWindow(itemView.getContext(), data, username);
                        popUpWindow.showAtLocation(itemView, Gravity.BOTTOM, 0, 0);
                    }
                   else
                    {
                        UserLogin.requestLogin(getItemView().getContext());
                    }
                }
            }break;
            case R.id.theSuiJi:
            case R.id.messageSuiJi:
            case R.id.comment:
            {

                Intent intent = new Intent(itemView.getContext(), ShowSuiJiActivity.class);
                if(getItemView().getTag() != null)
                {
                    intent.putExtra("data", new SuiJiDataBeanParceable((SuiJiDataBean) getItemView().getTag()));
                    fragment.getActivity().startActivityForResult(intent, RequestCode.LOOK_WHOLE_SUIJI);
                }
            }break;
            case R.id.headImage:
            {

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
    public boolean onLongClick(View v)
    {

        switch (v.getId())
        {
            case R.id.messageSuiJi:
            case R.id.theSuiJi:
            {
                SuiJiDataBean suiJiDataBean = (SuiJiDataBean) getItemView().getTag();
                if(suiJiDataBean != null)
                {
                    if(UserLogin.IS_LOGIN) {
                        String username = ((UserInfoApplocation) fragment.getActivity().getApplication()).getUsername();
                        Map<String, String> data = new HashMap<>();
                        data.put("username", suiJiDataBean.getUsername());
                        data.put("label", suiJiDataBean.getLabels().toString());
                        MenuPopUpWindow popupWindow = new MenuPopUpWindow(itemView.getContext(), data, username);
                        popupWindow.showAtLocation(itemView, Gravity.BOTTOM, 0, 0);
                    }
                    else
                    {
                        UserLogin.requestLogin(getItemView().getContext());
                    }
                }
                return true;
            }

        }
        return false;
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
            SuiJiDataBean suiJiDataBean = (SuiJiDataBean) getItemView().getTag();
            //触发点赞存储
            if(goodChose % 2 == 0)
            {
                Runnable runnable = () ->
                {

                    GiveGood.giveGood(suiJiDataBean.getSuiji_id(),
                                    2);
                };
                runnable.run();
            }
        }
    }
}
