package com.xxhhxhh.mainthing.index.adpter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.index.holder.VideoHolder;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder>
{

    public int suiji_id;
    private Context context;

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.video_look, parent, false);
        context = parent.getContext();
        return new VideoHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position)
    {

        new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 123)
                {
                   File file =  getFile();
                   holder.videoFile = file;
                   notifyDataSetChanged();
                }
            }
        }.sendEmptyMessage(123);

    }

    @Override
    public int getItemCount()
    {
        return 1;
    }

    private File getFile()
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_MEDIA);
        Map<String, String> data = new HashMap<>();
        data.put("group_id", "0");
        data.put("suiji_id", String.valueOf(suiji_id));

        httpsUtil.setSendData(data);

        try
        {
            httpsUtil.sendPost();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        try
        {
            String result = httpsUtil.getResponse().body().string();
            JSONObject jsonObject = new JSONObject(result);

            String file = jsonObject.getString("result");
            System.out.println(file);
            byte[] bytes = Base64.decode(file, Base64.DEFAULT);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getDataDir() +
                    "/suiji-" + suiji_id));
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return new File(context.getDataDir() +
                "/suiji-" + suiji_id);
    }
}
