package com.xxhhxhh.mainthing.addmessage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.Factory.MakeUUID;
import com.example.titan.R;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.addmessage.adapter.ShowLabelsAdapter;
import com.xxhhxhh.mainthing.addmessage.dialog.AddHrefDialog;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class AddArticleFragment  extends Fragment
    implements View.OnClickListener
{

    private View rootView;
    public final int type = 1;//标识
    public WebView webView;//网页视图
    //获取数据
    public Map<String, File> data;
    public Map<Integer, Bitmap> addList;//对应添加字典
    public List<String> allPutPhotos = new ArrayList<>();//所有保存到html中的图片文件
    public int number = 0;//总数量
    public int getType() { return type; }
    public Map<Integer, Bitmap> getAddList() { return addList; }
    public void setAddList(Map<Integer, Bitmap> addList) { this.addList = addList; }
    public void setData(Map<String, File> data) { this.data = data; }
    public Map<String, File> getData() { return data; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public WebView getWebView(){return this.webView;}
    //静态标识加载状态
    private static int nowLoadType = 0;
    //文件存储路径
    private String storagePath;
    //webclient
    private WebClient webClient;
    //管理
    private FragmentManager manager;
    private FragmentTransaction transaction;
    //加载标识，0为首次，1为非首次
    public int loadType;
    //html存储路径
    public String thePath;
    //预览标记
    private int previewMode = 0;
    private Button webViewBack;
    private Button toPreView;
    private TextView tipText;
    //标签存储
    public Map<String, String> chosedLabel = new HashMap<>();
    public void setChosedLabel(Map<String, String> chosedLabel){this.chosedLabel = chosedLabel;}
    public int nowSelect = 0;
    private EditText title;
    //修改模式数据获取
    private ArticleDataBean articleDataBean;
    //网页加载进度条
    private ProgressBar progressBar;

    public AddArticleFragment()
    {
        setAddList(new HashMap<>());
        loadType = 0;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        storagePath = getContext().getDataDir() + "/article";
        firstInit();//首次初始化
        thePath = storagePath + "/main.html";
        //写入富文本本体html
        writeRichEditor("richEditor.html", thePath);

        ((AddArticleActivity)getActivity()).setNowItem(0);
        super.onCreate(savedInstanceState);
        AddArticleActivity addArticleActivity = (AddArticleActivity)getActivity();

        if(addArticleActivity.getArticleDataBean() != null)
        {
            articleDataBean = addArticleActivity.getArticleDataBean();
            if(articleDataBean != null)
            {
                try
                {
                    JSONArray jsonArray = new JSONArray(articleDataBean.getLabels());

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
        }

    }

    //固定文件写入
    private void writeRichEditor(String fileName,String thePath)
    {
        //删除冗余文件
        File file = new File(thePath);
        if(file.exists())
        {
            file.delete();
        }
        try
        {
            InputStream inputStream = getResources().getAssets().open(fileName);
            byte[] bytes = new byte[1024];
            FileOutputStream outputStream = new FileOutputStream(new File(thePath));
            int bytesReads = 0;
            while((bytesReads = inputStream.read(bytes)) != -1)
            {
                outputStream.write(bytes, 0 , bytesReads);
            }
            outputStream.close();
            inputStream.close();
        }
        catch (Exception e)
        {
            Log.d("wowoo", e.toString());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.add_article, null);
        progressBar = rootView.findViewById(R.id.progressNow);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        init();
        initLabels();
        return rootView;
    }


    //文件夹初始化
    private void firstInit()
    {
        if(!new File(storagePath).exists())
        {
            new File(storagePath).mkdir();
        }
    }

    //初始化
    public void init()
    {
        //返回
        Button goBack = rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        //预览按钮
        toPreView = rootView.findViewById(R.id.toPreView);
        toPreView.setOnClickListener(this);
        //webview后退
        webViewBack = rootView.findViewById(R.id.webViewBack);
        webViewBack.setOnClickListener(this);
        //标题
        title = rootView.findViewById(R.id.editTitle);
        if(articleDataBean != null)
        {
            title.setText(articleDataBean.getArticle_title());
        }
        //提示信息
        tipText = rootView.findViewById(R.id.previewText);
        //确认
        Button confirm = rootView.findViewById(R.id.ok_add);
        confirm.setOnClickListener(this);
        //添加图片
        ImageButton addPhoto = rootView.findViewById(R.id.add_picture);
        addPhoto.setOnClickListener(this);
        //添加标签
        ImageButton addLabel = rootView.findViewById(R.id.add_topic);
        addLabel.setOnClickListener(this);
        //添加链接
        ImageButton addHref = rootView.findViewById(R.id.addHref);
        addHref.setOnClickListener(this);
        //网页编辑区
        webView = rootView.findViewById(R.id.makeArticle);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webClient = new WebClient();
        webView.setWebViewClient(webClient);
        //允许操作文件
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        if(articleDataBean != null && nowLoadType == 0)
        {
            nowLoadType = 1;
            webView.loadUrl("http://10.0.2.2:8080/" + articleDataBean.getUrl());
        }
        else
        {
            webView.loadUrl(Uri.fromFile(new File(thePath)).toString());
        }

        try
        {
            webView.loadUrl("javascript:setEditAble(true)");
        }
        catch (Exception e)
        {
            webView.loadUrl(Uri.fromFile(new File(thePath)).toString());
            e.printStackTrace();
        }

    }


    //初始化标签栏
    private void initLabels()
    {
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ShowLabelsAdapter showLabelsAdapter = new ShowLabelsAdapter(1, this);
        showLabelsAdapter.setItemCounts((chosedLabel == null || chosedLabel.isEmpty())?0:chosedLabel.size());
        recyclerView.setAdapter(showLabelsAdapter);
        ((LinearLayout)rootView.findViewById(R.id.showLabels)).addView(recyclerView);
    }

    //网页监控
    public class WebClient extends WebViewClient
    {

        public String startUrl = thePath;//记录一开始的url
        private int pageNumber = 0;//页面计数
        private WebLoad  webLoad = new WebLoad(20000, 200);

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            webLoad.start();
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            webLoad.onFinish();
            super.onPageFinished(view, url);
            onCreateViewUpdate();
            //不为空，更新数据
            if(addList.size() > 0 && number <= 30)
            {
                for (Bitmap value : addList.values())
                {
                    String photo = saveThePhotoWithBitmap(value);
                    webView.loadUrl(addTo(photo, 0));
                }
                nowSelect = addList.size();
            }
            //更新数量
            getPhotoNumberAndSave();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            if(!request.getUrl().equals(startUrl) && pageNumber != 0)
            {
                pageNumber = 0;
                view.loadUrl(startUrl);
                return true;
            }
            else
            {
                pageNumber = 1;
                return super.shouldOverrideUrlLoading(view, request);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
        {
            super.onReceivedHttpError(view, request, errorResponse);
            view.loadUrl(Uri.fromFile(new File(thePath)).toString());
        }

    }

    //进度条更新
    class WebLoad extends CountDownTimer
    {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public WebLoad(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            progressBar.setProgress(progressBar.getProgress() + 2);
        }

        @Override
        public void onFinish()
        {
            progressBar.setProgress(100);
            progressBar.setVisibility(View.GONE);
        }
    }


    //数据更新
    public void onCreateViewUpdate()
    {
        //上限30
        if (getData() != null)
        {
            if(getNumber() <= 30)
            {
                //保存拍摄图片
                if (getData().containsKey("photo"))
                {
                    String photo = saveThePhotoWithPath(getData().get("photo").getPath());
                    webView.loadUrl(addTo(photo, 0));
                    getData().remove("photo");
                }
            }
            else
            {
                Toast.makeText(getContext(), "上传文件数量达到上限", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //保存图片得到二进制流
    private String saveThePhotoWithPath(String filePath)
    {
        try
        {
            //文件重写入
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return saveThePhotoWithBitmap(bitmap);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }

    //保存图片，bitmap保存二进制流到html中
    private String saveThePhotoWithBitmap(Bitmap bitmap)
    {

        StringBuilder builder = new StringBuilder("data:image/png;base64,");
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

    //添加图片
    public String addTo(String path, int type)
    {
        if(type == 0)
        {
            //写入视图
            return "javascript:addPhoto('" + path + "', '" + number + "')" ;
        }
        else
        {
            return null;
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                getActivity().finish();
            }break;
            case R.id.add_picture:
            {

                getPhotoNumberAndSave();
                PictureLookFrgment pictureLookFrgment = new PictureLookFrgment();
                pictureLookFrgment.setType(1);
                addList.clear();
                transaction.add(R.id.addMessagePage, pictureLookFrgment);

                transaction.replace(R.id.addMessagePage, pictureLookFrgment).addToBackStack("addarticle").commit();
            }break;
            //添加标签
            case R.id.add_topic:
            {
                getPhotoNumberAndSave();
                AddLabelFragment addLabelFragment = new AddLabelFragment();
                addLabelFragment.setType(1);
                if(chosedLabel != null && !chosedLabel.isEmpty())
                {
                    addLabelFragment.chosedLabels = chosedLabel;
                }
                else
                {
                    addLabelFragment.chosedLabels = new HashMap<>();
                }
                addList.clear();
                transaction.replace(R.id.addMessagePage, addLabelFragment).addToBackStack("addarticle").commit();
            }break;
            case R.id.addHref:
            {
                new AddHrefDialog(getContext(), this).show();
            }break;
            case R.id.ok_add:
            {
                //设为不可编辑
                webView.loadUrl("javascript:setEditAble(false)");
                //需求内容判断
                if(title.getText() != null && !title.getText().toString().equals(""))
                {
                    if(chosedLabel != null && chosedLabel.size() > 0)
                    {

                        saveEnd();
                        Intent intent = getActivity().getIntent();
                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "请至少选择一个标签", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "标题不可为空", Toast.LENGTH_SHORT).show();
                }
            }break;
            //预览
            case R.id.toPreView:
             {
                 if(previewMode == 0)
                 {
                     previewMode = 1;
                     toPreView.setText("退出预览");
                     webViewBack.setVisibility(View.VISIBLE);
                     tipText.setText( "点击预览退出进入预览模式");
                     //设为不可编辑
                     webView.loadUrl("javascript:setEditAble(false)");

                 }
                 else if(previewMode == 1)
                 {
                     previewMode = 0;
                     toPreView.setText("预览");
                     webViewBack.setVisibility(View.INVISIBLE);
                     tipText.setText("点击预览进入预览模式");
                     webView.loadUrl("javascript:setEditAble(true)");
                 }
                 getPhotoNumberAndSave();
            }break;
            case R.id.webViewBack:
            {
                webView.goBack();
            }break;
        }
    }

    //图片数量
    public void getPhotoNumberAndSave()
    {
        webView.evaluateJavascript("javascript:getPhotoNumber();"
                , new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value)
                    {
                        try
                        {
                            if(value != null && !value.equals(""))
                            {
                                setNumber(Integer.parseInt(value));
                            }
                        }
                        catch (Exception e)
                        {
                            setNumber(0);
                            e.printStackTrace();
                        }
                    }
                });
        saveHtml();
    }

    //保存html内容
    public void saveHtml()
    {
        webView.evaluateJavascript("javascript:wholeHtml();"
                , new ValueCallback<String>()
                {
                    @Override
                    public void onReceiveValue(String value)
                    {
                        try
                        {
                            value = value.substring(1, value.length() - 1);
                            value = value.replaceAll("(\\\\u003C)", "<");
                            value = value.replaceAll("\\\\n", "");
                            value = value.replaceAll("\\\\", "");
                            FileWriter writer = new FileWriter(thePath, false);
                            BufferedWriter writer1 = new BufferedWriter(writer);
                            writer1.write(value);
                            writer1.close();
                            writer.close();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.toString());
                        }
                    }
                });
    }

    //保存文章
    public void saveEnd()
    {
        StringBuilder wholeHtml = new StringBuilder();
        //标题
        String title = this.title.getText().toString();
        //获取html
        webView.evaluateJavascript("javascript:wholeHtml()", new ValueCallback<String>()
        {
            @Override
            public void onReceiveValue(String value)
            {
                wholeHtml.append(value);
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        FormBody.Builder formBody = new FormBody.Builder();
                        //添加html
                        formBody.add("mainMessage", wholeHtml.toString());
                        if(chosedLabel != null && chosedLabel.size() > 0)
                        {
                            //标签
                            JSONArray jsonArray = new JSONArray(chosedLabel.values());
                            System.out.println(jsonArray);
                            formBody.add("labels", jsonArray.toString());
                        }
                        //添加标题
                        formBody.add("title", title);
                        //位置
                        formBody.add("location", MainActivity.nowLocation != null ? MainActivity.nowLocation : "暂无");
                        //用户名
                        formBody.add("username", ((UserInfoApplocation)getActivity().getApplication()).getUsername());
                        //模式
                        if(articleDataBean != null)
                        {
                            formBody.add("mode", "1");
                            formBody.add("article_id", String.valueOf(articleDataBean.getArticle_id()));
                        }
                        else
                        {
                            formBody.add("mode", "0");
                        }

                        Request request = new Request.Builder().url(TheUrls.SAVE_ARTICLE).post(formBody.build()).build();
                        Call call = okHttpClient.newCall(request);
                        try
                        {
                            Response response = call.execute();
                            String aa = response.body().string();
                            JSONObject jsonObject1 = new JSONObject(aa);
                            System.out.println(jsonObject1.get("isSuccess"));
                            response.close();
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.toString());
                        }
                        catch (JSONException ex)
                        {
                            System.out.println(ex.toString());
                        }

                    }
                }.start();
            }
        });

    }

}
