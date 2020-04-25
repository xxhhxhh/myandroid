package com.xxhhxhh.mainthing.addmessage.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.xxhhxhh.mainthing.Factory.MakeUUID;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.example.titan.R;
import com.xxhhxhh.mainthing.camera.CameraStateCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CameraPreviewFragment extends Fragment
        implements View.OnClickListener,
        TextureView.SurfaceTextureListener,
        View.OnLongClickListener
{

    private View rootView;//根视图
    private TextureView show;
    private CameraStateCallBack cameraStateCallBack;
    public int type;//标识
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    private Button reCamera;//重拍
    private Button confirm;//确定
    //预览位置的高度和宽度
    private int width;
    private int height;
    //摄像
    private String mode = "0";//摄像头当前模式,拍照、预览...
    //录像
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;//是否在录制
    //需要权限
    private String[] permissions = {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET
    };
    //为图片还是录像
    private int photoOrMp4 = 0;
    private File videoFile;//录像文件

    //拍摄按钮
    private Button cameraUse;

    //管理
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AddSuiJiFragment addSuiJiFragment;
    private AddArticleFragment addArticleFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera_preview, null);

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        initMode();
        init();
        return rootView;
    }

    //初始化模式：随记还是文章
    private void initMode()
    {
        //随记
        if(getType() == 0)
        {
            addSuiJiFragment = (AddSuiJiFragment) manager.findFragmentByTag("addSuiJi");
            ((AddSuiJiActivity)getActivity()).setNowItem(2);
        }
        //文章
        else if(getType() == 1)
        {
            addArticleFragment = (AddArticleFragment)manager.findFragmentByTag("addArticle");
            ((AddArticleActivity)getActivity()).setNowItem(2);
        }
    }


    private void init() {
        show = (TextureView) rootView.findViewById(R.id.cameraPreview);
        show.setSurfaceTextureListener(this);

        //返回
        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        //确定
        confirm = (Button)rootView.findViewById(R.id.ok_add);
        confirm.setOnClickListener(this);
        //翻转摄像头
        Button over = (Button)rootView.findViewById(R.id.over);
        over.setOnClickListener(this);
        //照相
        cameraUse = (Button)rootView.findViewById(R.id.cameraUse);
        cameraUse.setOnClickListener(this);
        cameraUse.setOnLongClickListener(this);
        //重拍
        reCamera = (Button)rootView.findViewById(R.id.cancel);
        reCamera.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //返回
            case R.id.goBack:
            {
                manager.popBackStack();
            }
            break;
            //拍摄
            case R.id.cameraUse:
             {

                 if(isRecording)
                 {
                     mediaRecorder.stop();
                     mediaRecorder.release();
                     mediaRecorder = null;
                     photoOrMp4 = 1;
                     isRecording = false;
                     reCamera.setVisibility(View.VISIBLE);
                     confirm.setVisibility(View.VISIBLE);
                     cameraUse.setText("点击拍照/长按后松开录像");
                 }
                 else
                 {
                     try
                     {
                         photoOrMp4 = 0;
                         cameraStateCallBack.captureStillPicture();
                         reCamera.setVisibility(View.VISIBLE);
                         confirm.setVisibility(View.VISIBLE);
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
             }break;
            //翻转摄像头
            case R.id.over:
            {
                reCamera.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                //后置改前置
                if(mode.equals("0"))
                {
                  mode = "1";
                }
                //前置改后置
                else if(mode.equals("1"))
                {
                   mode = "0";
                }
                CameraStateCallBack.closeThis();
                openCamera(mode);
            }break;
            //重拍
            case R.id.cancel:
            {
                reCamera.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                openCamera(mode);
            }break;
            //确定
            case R.id.ok_add:
            {
                transData(type);
            }break;
        }

    }

    //数据传送
    public void transData(int type)
    {
        //传送图片
        if(photoOrMp4 == 0)
        {
            if(type == 0)
            {
                addSuiJiFragment.setData(cameraStateCallBack.outPut());
                manager.popBackStack();
                manager.popBackStack();
            }
            else if(type == 1)
            {
                addArticleFragment.setData(cameraStateCallBack.outPut());
                manager.popBackStack();
                manager.popBackStack();
            }

        }
        //传送视频
        else if(photoOrMp4 == 1)
        {
            if(type == 0)
            {
                if(videoFile != null)
                {
                    Map<String, File> map = new HashMap<>();
                    map.put("video", videoFile);
                    addSuiJiFragment.setData(map);
                    manager.popBackStack();
                    manager.popBackStack();
                }
            }
            else if(type == 1)
            {
                if(videoFile != null)
                {
                    Toast.makeText(getContext(), "文章暂不支持视频", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        this.width = width;
        this.height = height;
        openCamera(mode);
    }


    //打开摄像头
    public void openCamera(String mode)
    {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try
        {
            CameraStateCallBack.width = width;
            CameraStateCallBack.height = height;
            cameraStateCallBack = new CameraStateCallBack(show, getActivity(), mode);
        }
       catch (Exception e)
       {

       }

        if(PermissionRequestAndCheckFactory.checkPermission(permissions, getActivity()))
        {
            try
            {
                manager.openCamera(mode, cameraStateCallBack, null);
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            PermissionRequestAndCheckFactory.requestPermission(permissions, getActivity());
            openCamera(mode);
        }

    }

    //录像
    public void audioRecord(String mode)
    {
        videoFile = new File(getActivity().getDataDir().toString(),
                MakeUUID.makeUUID("video") + ".mp4");
        //录像
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();//重置
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//麦克风收集音频
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//摄像头采集图像
        //视频文件输出格式,必须在设置声音格式、图像编码格式之前
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置声音编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//视频编码格式
        //输出文件
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());

        //预览
        mediaRecorder.setPreviewDisplay(new Surface(show.getSurfaceTexture()));

        //准备拍摄
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cameraUse:
            {
                if(getType() == 0)
                {
                    audioRecord(mode);
                    cameraUse.setText("停止录像");
                }
                else if(getType() == 1)
                {
                    Toast.makeText(getContext(), "文章暂不支持视频", Toast.LENGTH_SHORT).show();
                }
            }return true;
        }
        return false;
    }
}
