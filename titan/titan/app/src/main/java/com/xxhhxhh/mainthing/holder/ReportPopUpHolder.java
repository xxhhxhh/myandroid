package com.xxhhxhh.mainthing.holder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class ReportPopUpHolder extends RecyclerView.ViewHolder
{
    public TextView textView;
    public TextView getTextView() { return textView; }
    public void setTextView(TextView textView) { this.textView = textView; }
    private String username;//被举报

    public ReportPopUpHolder(@NonNull View itemView, String username)
    {
        super(itemView);
        setTextView(itemView.findViewById(R.id.choseMessage));
        this.username = username;
        getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String text = (String) textView.getTag();
                if(text.equals("恶意内容"))
                {
                    System.out.println(text);
                    ReportThread reportThread = new ReportThread(0);
                    reportThread.start();
                }
                else if(text.equals("社会危害"))
                {
                    ReportThread reportThread = new ReportThread(1);
                    reportThread.start();
                }
                else if(text.equals("内容非法"))
                {
                    ReportThread reportThread = new ReportThread(2);
                    reportThread.start();
                }
            }
        });
    }

    class ReportThread extends Thread
    {
        private int messageType;

        public ReportThread(int messageType)
        {
            this.messageType = messageType;
        }

        @Override
        public void run()
        {
            toReport();
        }

        //举报
        public void toReport()
        {
            HttpsUtil httpsUtil = new HttpsUtil(TheUrls.TO_REPORT);
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("type", String.valueOf(3));
            data.put("messageType", String.valueOf(messageType));
            data.put("mesage", "");

            httpsUtil.setSendData(data);

            try
            {
                httpsUtil.sendPost();
                System.out.println(httpsUtil.getResponse());
                httpsUtil.getResponse().close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }




}
