package com.xxhhxhh.mainthing.addmessage.fragment;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.PhotoLoadTimeTask;
import com.xxhhxhh.mainthing.addmessage.adapter.PictureLookAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class PictureLookFrgment extends Fragment
    implements View.OnClickListener
{

    private LinearLayout rootView;//根视图
    public static Map<Integer, Bitmap> photosMap = new HashMap<>();//所有找到的图片
    public  static Map<Integer, Bitmap> selectPhotos = new HashMap<>();//当前选中图片
    public static int allPhotos = 0;//所有找到的图片数量
    //确定按钮
    public Button confirm;
    //定时器
    private Timer timer;
    private PhotoLoadTimeTask timeTask;
    public int type;//标识
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    //需要权限
    private static final String[] permissions =
            {
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission_group.STORAGE

            };
    //管理
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AddSuiJiFragment addSuiJiFragment;
    private AddArticleFragment addArticleFragment;
    //设配器
    private PictureLookAdapter pictureLookAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (LinearLayout) inflater.inflate(R.layout.picture_look, null);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        initMode();
        //请求权限
        if(!PermissionRequestAndCheckFactory.checkPermission(permissions, getActivity()))
        {
            PermissionRequestAndCheckFactory.requestPermission(permissions, getActivity());
        }
        //初始化对象
        makeAll();
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
            ((AddSuiJiActivity)getActivity()).setNowItem(1);
        }
        //文章
        else if(getType() == 1)
        {
            addArticleFragment = (AddArticleFragment)manager.findFragmentByTag("addArticle");
            ((AddArticleActivity)getActivity()).setNowItem(1);
            selectPhotos.clear();
        }
    }

    //初始化
    private void init()
    {
        //请求权限
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 1);
        //返回
        Button goBack = (Button)rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        //确定
        confirm = (Button)rootView.findViewById(R.id.ok_add);
        confirm.setOnClickListener(this);
        //显示图片列表
        final RecyclerView showPictures = rootView.findViewById(R.id.showPhonePictures);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        showPictures.setLayoutManager(manager);
        //模式选择
        if(getType() == 0)
        {
            pictureLookAdapter = new PictureLookAdapter(confirm, getContext(), addSuiJiFragment,getType(), transaction);
        }
        else if(getType() == 1)
        {
            pictureLookAdapter = new PictureLookAdapter(confirm, getContext(), addArticleFragment,getType(), transaction);

        }
        showPictures.setAdapter(pictureLookAdapter);
        showPictures.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    if(manager.findLastCompletelyVisibleItemPosition() > pictureLookAdapter.getItemCount() - 2
                    && pictureLookAdapter.getItemCount() <= photosMap.size() / 3)
                    {
                        int a = pictureLookAdapter.itemCounts += 3;
                        pictureLookAdapter.notifyItemRangeChanged(pictureLookAdapter.getItemCount(), a);
                    }
                }
            }
        });
    }

    //所有图片
    private void makeAll()
    {
        Cursor cursor1 = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        photosMap.put(0, BitmapFactory.decodeResource(getResources(), R.mipmap.photo_icon));
        allPhotos = photosMap.size();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        if(cursor1.moveToFirst())
        photosMap.put(allPhotos, getOnePhoto(cursor1));
        allPhotos = photosMap.size();
        while (cursor1.moveToNext() && allPhotos <= 5)
        {
            photosMap.put(allPhotos, getOnePhoto(cursor1));
            allPhotos = photosMap.size();
        }
        timer = new Timer();
        timeTask = new PhotoLoadTimeTask(cursor1, getContext());
        timer.schedule(timeTask, 0, 100);
    }


    //获取图片
    private Bitmap getOnePhoto(Cursor cursor)
    {
        //获取图片保存位置数据
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        Uri base = Uri.parse("content://media/external/images/media");
        Uri nowUri = Uri.withAppendedPath(base, "" + id);

        try {
            ParcelFileDescriptor parcelFileDescriptor =  getContext().getContentResolver().openFileDescriptor(nowUri, "r");
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 100, true);
            return bitmap;
        }
        catch (Exception e)
        {

        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                //返回
                selectPhotos.clear();
                if(timer != null)
                {
                    timer.cancel();
                    timer = null;
                }
                if(timeTask != null)
                {
                    timeTask.cancel();
                    timeTask = null;
                }

                if(getType() == 0)
                {
                    manager.popBackStack();
                }
                else if(getType() == 1)
                {
                    addArticleFragment.setAddList(new HashMap<>());
                    manager.popBackStack();
                }
            }break;
            //确定
            case R.id.ok_add:
            {
                Map<Integer, Bitmap> map = new HashMap<>();

                //随记数据存储
                if(getType() == 0)
                {
                    for(Integer key : selectPhotos.keySet())
                    {
                        System.out.println(key);
                        AddSuiJiFragment.addList.put(AddSuiJiFragment.addList.size(), selectPhotos.get(key));
                    }
                    //设置数据
                    System.out.println(AddSuiJiFragment.addList.size());
                    addSuiJiFragment.setNowPhotoNumber(addSuiJiFragment.getAddList().size());
                    manager.popBackStack();
                }
                //文章数据存储
                else if(getType() == 1)
                {

                    for(Integer key : selectPhotos.keySet())
                    {
                       map.put(addArticleFragment.getNumber(), selectPhotos.get(key));
                    }
                    addArticleFragment.setAddList(new HashMap<>());
                    addArticleFragment.setAddList(map);
                    addArticleFragment.setNumber(addArticleFragment.getNumber() + addArticleFragment.getAddList().size());
                    manager.popBackStack();
                }
                selectPhotos.clear();
            }break;
        }
    }


}
