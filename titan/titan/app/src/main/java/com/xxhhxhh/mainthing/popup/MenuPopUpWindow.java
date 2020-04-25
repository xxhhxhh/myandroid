package com.xxhhxhh.mainthing.popup;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.titan.R;
import com.xxhhxhh.mainthing.dialog.ToReportDialog;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class MenuPopUpWindow extends PopupWindow
    implements View.OnClickListener
{
    /**此弹窗处理用户的举报和不喜欢理由请求
     * @param canshu /*内容包含 被操作用户、标签
     * @param username /*操作用户
     * */

    private Context context;
    private Map<String, String> canshu;
    private String username;//要进行操作的用户
    private ReportThread reportThread;

    public MenuPopUpWindow(Context context, Map<String, String> canshu, String username)
    {
        this.canshu = canshu;
        this.context = context;
        this.username = username;
        reportThread = new ReportThread();
        reportThread.start();
        this.setContentView(makeView());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(context.getDrawable(R.drawable.pop_window_back));
    }

    public View makeView()
    {
        LinearLayout main = new LinearLayout(context);
        main.setMinimumHeight(500);
        main.setOrientation(LinearLayout.VERTICAL);
        //不感兴趣标题
        TextView title2 = new TextView(context);
        title2.setWidth(270);
        title2.setGravity(Gravity.CENTER);
        title2.setText(context.getResources().getString(R.string.reason));
        title2.setTextSize(17);
        main.addView(title2);
        //对用户不感兴趣
        Button deusername = new Button(context);
        deusername.setOnClickListener(this);
        if(canshu != null)
        {
            deusername.setText(canshu.get("username"));
        }
        else
        {
            deusername.setText("对用户不感兴趣");
        }
        deusername.setHeight(80);
        deusername.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        deusername.setTextSize(15);
        deusername.setId(R.id.deusername);
        main.addView(deusername);
        //对文章标签不感兴趣
        Button deLabel = new Button(context);
        deLabel.setOnClickListener(this);
        if(canshu != null)
        {
            deLabel.setText(canshu.get("label"));
        }
        else
        {
            deLabel.setText("对标签不感兴趣");
        }
        deLabel.setHeight(80);
        deLabel.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        deLabel.setTextSize(15);
        deLabel.setId(R.id.delabel);
        main.addView(deLabel);
        //举报按钮
        Button report = new Button(context);
        report.setId(R.id.toReportThis);
        report.setHeight(80);
        report.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        report.setText(context.getResources().getString(R.string.no));
        report.setTextSize(15);
        report.setOnClickListener(this);
        main.addView(report);
        return main;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.toReportThis:
            {
                if(canshu != null)
                {
                    this.dismiss();
                    ToReportDialog dialog = new ToReportDialog(context, canshu.get("username"));
                    dialog.show();
                }
            }break;
            case R.id.delabel:
            {
                if(canshu != null)
                reportThread.reportHandler.sendEmptyMessage(123);
                this.dismiss();
            }break;
            case R.id.deusername:
            {
                if(canshu != null)
                reportThread.reportHandler.sendEmptyMessage(456);
                this.dismiss();
            }break;
        }
    }

    class ReportThread extends Thread
    {
        public ReportHandler reportHandler;

        @Override
        public void run()
        {
            Looper.prepare();
            reportHandler = new ReportHandler();
            Looper.loop();
        }

        class ReportHandler extends Handler
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                if(msg.what == 123)
                {
                    reportLabel();
                }
                else if(msg.what == 456)
                {
                    reportUser();
                }
            }

            //标签不感兴趣
            public void reportLabel()
            {
                HttpsUtil httpsUtil = new HttpsUtil(TheUrls.TO_REPORT);
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("message", canshu.get("label"));
                data.put("type", String.valueOf(1));

                httpsUtil.setSendData(data);

                try
                {
                    httpsUtil.sendPost();
                    System.out.println(httpsUtil.getResponse().body().string());
                    httpsUtil.getResponse().close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            //用户不感兴趣
            public void reportUser()
            {
                HttpsUtil httpsUtil = new HttpsUtil(TheUrls.TO_REPORT);
                Map<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("message", canshu.get("username"));
                data.put("type", String.valueOf(0));
                System.out.println(username);
                System.out.println(canshu.get("username"));

                httpsUtil.setSendData(data);

                try
                {
                    httpsUtil.sendPost();
                    System.out.println(httpsUtil.getResponse().body().string());
                    httpsUtil.getResponse().close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


}
