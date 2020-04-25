package com.xxhhxhh.mainthing.addmessage.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.*;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amap.api.location.AMapLocationClient;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.xxhhxhh.mainthing.Factory.MakeUUID;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.adapter.ShowLabelsAdapter;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanSimple;
import com.xxhhxhh.mainthing.location.GetNowLocation;
import com.xxhhxhh.mainthing.adapter.PictureAdapter;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.util.*;

public class AddSuiJiFragment extends Fragment
    implements View.OnClickListener,
        SurfaceHolder.Callback
{
    public Map<String, File> data; //获取数据
    private PictureAdapter pictureAdapter;
    public Map<Integer, Integer> willDelete = new HashMap<>();//将要删除的key集合
    public static Map<Integer, Bitmap> addList = new HashMap<>();//对应添加字典
    public int nowPhotoNumber;
    //标签存储
    public Map<String, String> chosedLabel = new HashMap<>();
    public void setChosedLabel(Map<String, String> chosedLabel) { this.chosedLabel = chosedLabel; }
    public Map<String, File> getData() { return data; }
    public void setData(Map<String, File> data) { this.data = data; }
    public Map<Integer, Bitmap> getAddList() { return addList; }
    public void setAddList(Map<Integer, Bitmap> addList) { this.addList = addList; }
    public int getWillDeleteSize(){return willDelete.size();}//即将删除列表长度
    public void setWillDelete(){willDelete = new HashMap<>();}
    public Map<Integer, Integer> getWillDelete(){return willDelete;}
    public int getNowPhotoNumber(){return nowPhotoNumber;}
    public void setNowPhotoNumber(int nowPhotoNumber) { this.nowPhotoNumber = nowPhotoNumber; }
    private EditText mainMessage;
    public SpannableStringBuilder mainMessageAdd = new SpannableStringBuilder();//增加文本

    //录像
    private File videoFile;
    private MediaPlayer player;//视频播放器
    private SurfaceView surfaceView;//播放位置
    private int position;//播放进度
    private Button playVideo;
    //模式。
    private int mode;
    private View rootView;
    private RecyclerView recyclerView;
    private LinearLayout showAddPhoto;
    //需要权限
    private static final String[] permissions =
            {
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission_group.STORAGE

            };

    private FragmentTransaction transaction;

    //是否显示位置信息
    private boolean isShowLocation = false;
    //是否公开
    private boolean isPrivacy = true;
    private GetNowLocation getNowLocation;//监听器
    //初始化数据
    private SuiJiDataBeanSimple suiJiDataBeanSimple;
    //构造
    public AddSuiJiFragment()
    {
        //设置
        setWillDelete();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainMessageAdd = new SpannableStringBuilder();
        //初始化数据
        AddSuiJiActivity addSuiJiActivity = (AddSuiJiActivity)getActivity();
        if(addSuiJiActivity.getType() == 1)
        {
            if(addSuiJiActivity.getBitmapMap() != null)
            {
                setAddList(addSuiJiActivity.getBitmapMap());
            }

            if(addSuiJiActivity.getSuiJiDataBeanSimple() != null)
            {
                suiJiDataBeanSimple = addSuiJiActivity.getSuiJiDataBeanSimple();
                mainMessageAdd.append(suiJiDataBeanSimple.getMain_message());
                if(suiJiDataBeanSimple != null)
                {
                    try
                    {
                        JSONArray jsonArray = new JSONArray(suiJiDataBeanSimple.getLabels().toString());

                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            chosedLabel.put(jsonArray.getString(i), jsonArray.getString(i));
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                if(addSuiJiActivity.getBitmapMap() != null)
                {
                    setAddList(addSuiJiActivity.getBitmapMap());
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.add_suiji, null);
        //定位
        //定位
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        AMapLocationClient client = new AMapLocationClient(getContext());
        getNowLocation = new GetNowLocation(locationManager, client);
        client.startLocation();
        //管理设置
        //fragment管理
        FragmentManager manager = getFragmentManager();
        transaction = manager.beginTransaction();
        //设置整体回退标识
        ((AddSuiJiActivity)getActivity()).setNowItem(0);
        //请求权限
        if(!PermissionRequestAndCheckFactory.checkPermission(permissions, getActivity()))
        {
            PermissionRequestAndCheckFactory.requestPermission(permissions, getActivity());
        }
        System.out.println(addList.size());
        mode = onCreateViewUpdate();
        init(mode);
        initLabels();
        return rootView;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        pictureAdapter.itemCountUpdate();
        pictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mainMessage.setText(mainMessageAdd);
    }



    //初始化
    private void init(int mode)
    {

        //返回
        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        mainMessage = (EditText)rootView.findViewById(R.id.share);
        //确定
        Button ok_add = (Button)rootView.findViewById(R.id.ok_add);
        ok_add.setOnClickListener(this);
        TextView showText = rootView.findViewById(R.id.tipText);
        //显示图片
        showAddPhoto = (LinearLayout) rootView.findViewById(R.id.showMedia);
        if(mode == 0)
        {
            showAddPhoto.removeAllViews();
            showAddPhoto.addView(initPhotos());
            showText.setText("长按图片点击删除即可删除图片");
        }
        else if(mode == 1)
        {
            showAddPhoto.removeAllViews();
            showAddPhoto.addView(initVideo());
            showText.setText("点击删除即可删除视频");
        }
        //位置
        Button showLocation = rootView.findViewById(R.id.location);
        showLocation.setOnClickListener(this);
        showLocation.setText(getNowLocation());
        //刷新位置
        Button reSetLocation = rootView.findViewById(R.id.reSetLocation);
        reSetLocation.setOnClickListener(this);
        //公开隐私
        Button privacy = rootView.findViewById(R.id.open);
        privacy.setOnClickListener(this);
        //添加图片
        ImageButton add_picture = (ImageButton) rootView.findViewById(R.id.add_picture);
        add_picture.setOnClickListener(this);
        //添加标签
        ImageButton add_topic = (ImageButton)rootView.findViewById(R.id.add_topic);
        add_topic.setOnClickListener(this);
        //添加联系人
        ImageButton at_one = (ImageButton)rootView.findViewById(R.id.at_one);
        at_one.setOnClickListener(this);
        //删除
        Button delete = (Button)rootView.findViewById(R.id.deleteThese);
        delete.setOnClickListener(this);
    }

    //初始化显示图片
    public View initPhotos()
    {
        recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        pictureAdapter = new PictureAdapter(this, 0);
        System.out.println("当前" + getAddList().size());
        recyclerView.setAdapter(pictureAdapter);
        return recyclerView;
    }

    //初始化显示视频
    public View initVideo()
    {
        FrameLayout videoView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.video_look, null);
        player = new MediaPlayer();
        //播放完成监听
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playVideo.setText("开始");
            }
        });
        surfaceView = videoView.findViewById(R.id.showVideo);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(this);
        playVideo = videoView.findViewById(R.id.playVideo);
        playVideo.setOnClickListener(this);
        return videoView;
    }

    //初始化标签栏
    private void initLabels()
    {
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ShowLabelsAdapter showLabelsAdapter = new ShowLabelsAdapter(0, this);
        showLabelsAdapter.setItemCounts((chosedLabel == null || chosedLabel.isEmpty())?0:chosedLabel.size());
        recyclerView.setAdapter(showLabelsAdapter);
        ((LinearLayout)rootView.findViewById(R.id.showLabels)).addView(recyclerView);
    }


    //数据更新
    public int onCreateViewUpdate()
    {
        if(data != null)
        {
            if(data.containsKey("photo"))
            {
                Bitmap photo = (Bitmap) BitmapFactory.decodeFile(data.get("photo").getPath());
                //组成字典对象
                if (addList.size() <= 9)
                {
                    addList.put(addList.size(), photo);
                    setNowPhotoNumber(addList.size());
                }
                pictureAdapter.notifyDataSetChanged();
                data.remove("photo");
                return 0;
            }
            else if(data.containsKey("video"))
            {
                videoFile = new File(data.get("video").getPath());

                data.remove("video");
                return 1;
            }
        }
        return 0;
    }


    //保存视频
    private String saveVideo()
    {
        if(videoFile != null)
        {
            try
            {
                FileInputStream inputStream = new FileInputStream(videoFile);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                return Base64.encodeToString(bytes, 0);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());

            }
        }
        return null;
    }

    @Override
    public void onClick(View v)
    {
        mainMessageAdd.clear();
        mainMessageAdd.append(mainMessage.getText());
        switch (v.getId())
        {
            //返回
            case R.id.goBack:
            {
                addList.clear();
                getActivity().finish();
            }break;
            //添加图片
            case R.id.add_picture:
            {
                if(addList.size() >= 9)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).
                            setTitle("超限").
                            setMessage("您添加的图片数量已达上限（9个）");
                    builder.setPositiveButton("确定", null);
                    builder.show();

                }
                else
                {
                    PictureLookFrgment pictureLookFrgment = new PictureLookFrgment();
                    pictureLookFrgment.setType(0);
                    pictureLookFrgment.selectPhotos = new HashMap<>();
                    transaction.replace(R.id.addMessagePage, pictureLookFrgment).addToBackStack("addsuiji").commit();
                }
            }break;
            //确定发表
            case R.id.ok_add:
            {
                if(UserLogin.IS_LOGIN)
                {
                    if(!mainMessage.getText().toString().equals(""))
                    {
                        if(chosedLabel != null && !chosedLabel.isEmpty())
                        {
                            if(mainMessage.getText().toString().length() < 160)
                            {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext()).setTitle("正在上传").
                                        setView(LayoutInflater.from(getContext()).inflate(R.layout.yanzheng_dialog, null));
                                AlertDialog alertDialog1 = alertDialog.create();
                                alertDialog1.show();
                                finallySave();
                                alertDialog1.dismiss();
                                Intent intent = getActivity().getIntent();
                                getActivity().setResult(0, intent);
                                getActivity().finish();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "文本超限", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "至少选择一个标签", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    UserLogin.requestLogin(getContext());
                }

            }break;
            //添加标签
            case R.id.add_topic:
            {
                AddLabelFragment addLabelFragment = new AddLabelFragment();
                addLabelFragment.setType(0);
                //设置值
                if(chosedLabel != null && !chosedLabel.isEmpty())
                {
                    addLabelFragment.chosedLabels = chosedLabel;
                }
                else
                {
                    addLabelFragment.chosedLabels = new HashMap<>();
                }
                transaction.replace(R.id.addMessagePage, addLabelFragment).addToBackStack("addsuiji").commit();
            }break;
            //添加联系人
            case R.id.at_one:
            {
                AddPeopleFragment addPeopleFragment = new AddPeopleFragment();
                transaction.replace(R.id.addMessagePage, addPeopleFragment).addToBackStack("addsuiji").commit();
            }break;
            //公开位置
            case R.id.location:
            {
                if(isShowLocation)
                {
                    String nowLocation = getNowLocation();
                    ((Button)rootView.findViewById(R.id.location)).setText(nowLocation);
                    isShowLocation = false;
                }
                //不公开
                else
                {
                    ((Button)rootView.findViewById(R.id.location)).setText("不公开位置");
                    isShowLocation = true;
                }
            }break;
            //公开
            case R.id.open:
            {
                //不公开
                if(isPrivacy)
                {
                    ((Button)rootView.findViewById(R.id.open)).setText("隐私");
                    isPrivacy = false;
                }
                //公开
                else
                {
                    ((Button)rootView.findViewById(R.id.open)).setText("公开");
                    isPrivacy = true;
                }

            }break;
            case R.id.deleteThese:
            {
                if(mode == 0)
                {
                    if(willDelete.size() > 0)
                    {
                        //移除数据
                        for(Integer value : willDelete.values())
                        {
                            addList.remove(value);
                        }
                        //重置位置
                        Map<Integer, Bitmap> map = new HashMap<>();
                        int i = 0;
                        for(Integer key : addList.keySet())
                        {
                            map.put(i, addList.get(key));
                            i++;
                        }
                        addList.clear();
                        addList.putAll(map);
                        willDelete.clear();

                    }
                    pictureAdapter = new PictureAdapter(this, 0);
                    recyclerView.setAdapter(pictureAdapter);
                }
                else if(mode == 1)
                {
                    showAddPhoto.removeAllViews();
                }
            }break;
            case R.id.playVideo:
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
            }break;
            case R.id.reSetLocation:
            {
                String nowLocation = getNowLocation();
                ((Button)rootView.findViewById(R.id.location)).setText(nowLocation);
                isShowLocation = false;
            }break;
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

    //获取当前位置
    private String getNowLocation()
    {
        String location = getNowLocation.getNowLocation();
        return location == null? "暂无" : location;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(position > 0)
        {
            playVideo();
            player.seekTo(position);
            position = 0;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //存储最终数据
    private void finallySave()
    {
        //标签
        List<String> labels = new ArrayList<>();
        labels.addAll(chosedLabel.values());
        SaveThread saveThread = new SaveThread();
        saveThread.start();
        //存储文本
        try
        {
            saveThread.labels = new JSONArray(labels);
            saveThread.location = getNowLocation();
            saveThread.mainMessage = mainMessage.getText().toString();
            //视频存储
            if(videoFile != null)
            {
                saveThread.fileStrings = new JSONArray(new String[] {saveVideo()});
                saveThread.fileType = "1";
            }
            //图片存储
            else if(addList != null && addList.size() > 0)
            {
                List<String> photoStrings = new ArrayList<>();
                for(Bitmap bitmap : addList.values())
                {
                    photoStrings.add(saveThePhotoWithBitmap(bitmap));
                }
                saveThread.fileStrings = new JSONArray(photoStrings);
                saveThread.fileType = "0";
                addList.clear();
            }
        }
        catch (Exception e)
        {
            Log.d("数据写入异常", e.toString());
        }

        //发送数据
        try
        {
            saveThread.toHander.sendEmptyMessage(123);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }

    //保存图片，bitmap保存二进制流到字符串
    private String saveThePhotoWithBitmap(Bitmap bitmap)
    {

        StringBuilder builder = new StringBuilder();
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap = Bitmap.createScaledBitmap(bitmap, 220, 150, true);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            builder.append(Base64.encodeToString(bytes, Base64.NO_WRAP));
            return builder.toString();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

    public class SaveThread extends Thread
    {
        public ToHander toHander;
        public String mainMessage;//内容
        public JSONArray fileStrings;//文件数据
        public JSONArray labels;//标签数据
        public String location;//位置
        public String fileType = "0";

        @Override
        public void run()
        {
            Looper.prepare();
            toHander = new ToHander();
            Looper.loop();
        }

        //消息处理
        public class ToHander extends Handler
        {

            @Override
            public void handleMessage(@NonNull Message msg)
            {
                //存储数据
                if(msg.what == 123)
                {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //post体
                    FormBody.Builder formbody = new FormBody.Builder();
                    formbody.add("fileStrings", fileStrings.toString());
                    formbody.add("mainMessage", mainMessage);
                    formbody.add("labels", labels.toString());
                    formbody.add("location", location);
                    formbody.add("fileType", fileType);
                    formbody.add("username" ,((UserInfoApplocation)getActivity().getApplication()).getUsername());
                    //模式
                    if(suiJiDataBeanSimple != null)
                    {
                        formbody.add("mode", "1");
                        formbody.add("suiji_id", String.valueOf(suiJiDataBeanSimple.getSuiji_id()));
                    }
                    else
                    {
                        formbody.add("mode", "0");
                    }

                    //
                    try
                    {
                        Request request = new Request.Builder().url(TheUrls.SAVE_SUIJI).post(formbody.build()).build();
                        Call call = okHttpClient.newCall(request);
                        Response response = call.execute();
                        System.out.println(response.body().string());
                        response.close();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }
            }
        }
    }
}
