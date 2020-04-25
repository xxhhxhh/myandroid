package com.xxhhxhh.mainthing.index.holder;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;

import java.io.File;

public class VideoHolder extends RecyclerView.ViewHolder
    implements SurfaceHolder.Callback
{
    //录像
    public File videoFile;
    private MediaPlayer player;//视频播放器
    private SurfaceView surfaceView;//播放位置
    private int position;//播放进度
    private Button playVideo;

    public VideoHolder(@NonNull View itemView)
    {
        super(itemView);
        //文件不为空触发单击
        if(videoFile != null && videoFile.exists())
        {
            surfaceView = itemView.findViewById(R.id.showVideo);
            surfaceView.getHolder().setKeepScreenOn(true);
            surfaceView.getHolder().addCallback(this);
            playVideo = itemView.findViewById(R.id.playVideo);
            playVideo.setOnClickListener(v ->
            {
                if(player.isPlaying())
                {
                    playVideo.setText("开始");
                    player.pause();
                    position = player.getCurrentPosition();
                }
                else
                {
                    playVideo.setText("暂停");
                    if(position <= 0)
                    {
                        playVideo();
                    }
                    else
                    {
                        player.seekTo(position);
                        player.start();
                    }
                }
            });
        }
    }

    //播放视频
    private void playVideo()
    {
        player.reset();
        playVideo.setText("暂停");
        AudioAttributes audioAttributes = new AudioAttributes.Builder().
                setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        player.setAudioAttributes(audioAttributes);
        try
        {
            //要播放视频
            player.setDataSource(videoFile.getPath());
            player.setDisplay(surfaceView.getHolder());//推到界面
            player.prepare();
        }
        catch ( Exception e)
        {

        }

        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if(position > 0)
        {
            playVideo();
            player.seekTo(position);
            position = 0;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }
}
