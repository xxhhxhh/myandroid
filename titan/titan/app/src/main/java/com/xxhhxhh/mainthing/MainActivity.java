package com.xxhhxhh.mainthing;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.FileUtils;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.amap.api.location.AMapLocationClient;
import com.example.titan.R;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.xxhhxhh.mainthing.index.fragment.ArticleFragment;
import com.xxhhxhh.mainthing.location.GetNowLocation;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.mainthing.login_and_register.alertdialog.SetPasswordDialog;
import com.xxhhxhh.mainthing.login_and_register.alertdialog.WriteIdentifyDialog;
import com.xxhhxhh.mainthing.login_and_register.handler.LoginAndRegisterThread;
import com.xxhhxhh.mainthing.pagechanges.BottomPageChangerAdapter;
import com.xxhhxhh.mainthing.viewpager.MainViewPager;
import com.google.android.material.tabs.TabLayout;
import com.xxhhxhh.transdata.requestcode.RequestCode;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity
        implements View.OnClickListener
{
    private String[] requestPermissions =
        {
                Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE
        };

    //标签获取
    public static String[] suiJiLabels;
    public static String[] articleLabels;
    public static String databasePath;//本地数据库
    public static int newDataBaseVersion = 2;//最新数据库版本
    //登录模式
    public static int loginType = 5;
    //定位
    public static String nowLocation;
    private GetNowLocation getNowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new LinearLayout(this));
        if(!PermissionRequestAndCheckFactory.checkPermission(requestPermissions, this))
        {
            PermissionRequestAndCheckFactory.requestPermission(requestPermissions, this);
        }
        //数据库创建
        databasePath = this.getDataDir().toString() + "/databases/my.db";
        getLabels();
        //主界面
        LinearLayout whole = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        //主要界面显示
        TabLayout bottomPageLook = whole.findViewById(R.id.bottomPageLooker);
        //添加viewpager
        LinearLayout pagerShower = (LinearLayout)whole.findViewById(R.id.pagerShower);
        MainViewPager bottomPageShower = new MainViewPager(this);
        bottomPageShower.setMinimumHeight(ViewPager.LayoutParams.MATCH_PARENT);
        bottomPageShower.setMinimumWidth(ViewPager.LayoutParams.MATCH_PARENT);
        bottomPageShower.setId(R.id.bottomPageShower);
        pagerShower.addView(bottomPageShower);
        bottomPageShower.setOffscreenPageLimit(2);
        //viewpager不可滑动
        bottomPageShower.setScrollAble(false);
        bottomPageLook.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(bottomPageShower));
        bottomPageShower.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomPageLook));
        bottomPageShower.setAdapter(new BottomPageChangerAdapter(getSupportFragmentManager(), 1));
        setContentView(whole);
        //创建必须文件夹
        File addLinShi = new File(getDataDir().toString() + "/local/linshi");
        if(!addLinShi.exists())
        {
            addLinShi.mkdir();
        }

        //定位
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        AMapLocationClient client = new AMapLocationClient(this);
        getNowLocation = new GetNowLocation(locationManager, client);
        autoLogin();

        SQLiteDataBaseOpenHelper db = new SQLiteDataBaseOpenHelper(this, MainActivity.databasePath, null, MainActivity.newDataBaseVersion);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from ArticleLabels;", null);
        cursor.moveToFirst();
        ArticleFragment.labels.add(cursor.getString(0));
        while (cursor.moveToNext())
        {
            ArticleFragment.labels.add(cursor.getString(0));
        }
        db.close();
    }


    //获取标签
    public void getLabels()
    {
        //数据库对象
        SQLiteDataBaseOpenHelper db = new SQLiteDataBaseOpenHelper(this, databasePath, null, newDataBaseVersion);
        //接受标签
        new Thread()
        {
            @Override
            public void run()
            {
                HttpsUtil httpsUtil = new HttpsUtil(TheUrls.GET_LABELS);
                httpsUtil.sendPost();

                try
                {
                    String result = httpsUtil.getResponse().body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray suijiLabels = jsonObject.getJSONArray("suiJiLabels");
                    JSONArray articleLabels = jsonObject.getJSONArray("articleLabels");
                    //插入数据库
                    for(int i = 0; i < suijiLabels.length(); i++)
                    {
                        db.getWritableDatabase().execSQL("insert into SuiJiLabels values('" + suijiLabels.getString(i) + "')");
                    }
                    for(int j = 0; j < articleLabels.length(); j++)
                    {
                        db.getWritableDatabase().execSQL("insert into ArticleLabels values('" + articleLabels.getString(j) + "')");
                    }
                    httpsUtil.getResponse().close();
                    db.close();

                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }

            }
        }.start();
    }

    //自动登录
    protected void autoLogin()
    {
//        //数据库对象
//        SQLiteDataBaseOpenHelper db = new SQLiteDataBaseOpenHelper(this, databasePath, null, newDataBaseVersion);
//        //自动登录
//        Cursor cursor = db.getReadableDatabase().rawQuery("select username from UserInfo where isallow_login=1 limit 1", null);
        //自动登录模式二
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if(!username.equals(""))
        {
            LoginAndRegisterThread loginAndRegisterThread = new LoginAndRegisterThread(this, username);
            loginAndRegisterThread.start();
        }
        //存在可自动登录用户
//        if(cursor.moveToFirst() && !UserLogin.IS_LOGIN)
//        {
//            LoginAndRegisterThread loginAndRegisterThread = new LoginAndRegisterThread(this, cursor.getString(0));
//            loginAndRegisterThread.start();
//        }
//        db.close();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        nowLocation = getNowLocation.getNowLocation();
        if(loginType == 1)
        {
            loginType = 5;
            String username = ((UserInfoApplocation) getApplication()).getUsername();
            WriteIdentifyDialog writeIdentifyDialog = new WriteIdentifyDialog(this, username);
            AlertDialog alertDialog = writeIdentifyDialog.create();
            alertDialog.show();
        }

    }

    @Override
    public void onClick(View v) {

        //弹出设置菜单
        switch (v.getId())
        {
            case R.id.showSetting:
            {
                View settingView = this.getLayoutInflater().inflate(R.layout.setting_title, null);
                PopupWindow settingChose = new PopupWindow(settingView, 500, 500);
                settingChose.showAtLocation(findViewById(R.id.showSetting), Gravity.END, 0, 0);
            }break;

        }

    }

    //返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //摄像头拍照返回
        if(requestCode == RequestCode.OPEN_CAMERA)
        {
            try
            {
                File outFile = new File(getFilesDir() + "/aHead.png");
                FileInputStream inputStream = new FileInputStream(outFile);
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), 100, 100, true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String username = ((UserInfoApplocation)getApplication()).getUsername();
                final String data1 =   Base64.encodeToString(bytes, Base64.NO_WRAP);
                editor.putString("userHead", data1);
                editor.putString("username", username);
                editor.apply();
                if(username != null && !username.equals(""))
                {
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            GetUserHeadUtil.saveHead(username, data1);
                        }
                    }.start();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        //从相册获取
        else if(requestCode == RequestCode.GET_PICTURE)
        {
            try
            {
                if(data.getData() != null)
                {
                    Uri uri = data.getData();
                    Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), 100, 100, true);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String username = ((UserInfoApplocation)getApplication()).getUsername();
                    final String data1 =   Base64.encodeToString(bytes, Base64.NO_WRAP);
                    editor.putString("userHead", data1);
                    editor.putString("username", username);
                    editor.apply();
                    if(username != null && !username.equals(""))
                    {
                        new Thread()
                        {
                            @Override
                            public void run()
                            {
                                GetUserHeadUtil.saveHead(username, data1);
                            }
                        }.start();
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
