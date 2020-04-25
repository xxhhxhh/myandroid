package com.example.custom_imageview;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    //命名空间
    public final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private final Paint paint = new Paint();//画笔
    private final Canvas theCanvas = new Canvas();
    private boolean isFirstMeasure = true;
    private boolean isNoFirstSetBack = false;
    private Drawable drawable;
    private VIEW_MODE viewMode = VIEW_MODE.RECT;
    private int centerX = 0;
    private int centerY = 0;
    //圆角矩形
    private int roundX = 0;//横向角度
    private int roundY = 0;//纵向角度
    public int getRoundX() { return roundX; }
    public int getRoundY() { return roundY; }
    public void setRoundX(int roundX) { this.roundX = roundX; }
    public void setRoundY(int roundY) { this.roundY = roundY; }
    //圆
    private int r = 0;//半径
    //图片资源
    private Bitmap theBitmap = null;
    //bitmap加载
    private BitmapFactory.Options options = new BitmapFactory.Options();
    //handler
    private final LoadHanlder loadHandler = new LoadHanlder(this);
    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViewMode(Integer.parseInt(attrs.getAttributeValue(NAMESPACE, "viewMode")));
        roundX = attrs.getAttributeIntValue(NAMESPACE, "roundX", 0);
        roundY = attrs.getAttributeIntValue(NAMESPACE, "roundY", 0);
        initFileMode(attrs.getAttributeIntValue(NAMESPACE, "fileMode", 0), attrs);
        init();
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewMode(Integer.parseInt(attrs.getAttributeValue(NAMESPACE, "viewMode")));
        roundX = attrs.getAttributeIntValue(NAMESPACE, "roundX", 0);
        roundY = attrs.getAttributeIntValue(NAMESPACE, "roundY", 0);
        initFileMode(attrs.getAttributeIntValue(NAMESPACE, "fileMode", 0), attrs);
        init();
    }

    //初始化viewMode
    /**@param viewMode
     * @see VIEW_MODE
     * */
    private void initViewMode(int viewMode){
        switch (viewMode){
            case 1:{
                this.viewMode = VIEW_MODE.CIRCLE_RECT;
            }break;
            case 2:{
                this.viewMode = VIEW_MODE.CIRCLE;
            }break;
            case 3:{
                this.viewMode = VIEW_MODE.RECT;
            }break;
        }
    }
    //初始化filemode
    /**@param fileMode
     * @see FILE_MODE //默认或者resource对于layout文件都将使用background属性
     * @param attributeSet  //参数集合
     * */
    private void initFileMode(int fileMode, AttributeSet attributeSet){
        switch (fileMode){
            case 2:{
                String uri = attributeSet.getAttributeValue(NAMESPACE, "theFile");
                Uri uri1 = Uri.parse(uri);
                theBitmap = BitmapFactory.decodeFile(uri1.getPath());
            }break;
            case 3:{
                theBitmap = BitmapFactory.decodeFile(attributeSet.getAttributeValue(NAMESPACE, "theFile"));
            }break;
        }
    }

    //初始化
    private void init(){
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一次测量
        if(isFirstMeasure){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            centerX = size / 2;
            centerY = size / 2;
            r = size / 2;
            isFirstMeasure = false;
            isNoFirstSetBack = true;
        }else {
            //其它次设置背景
            setBackground(getBackground());
            setMeasuredDimension(centerX * 2, centerY * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //bitmap非空绘制
        if(theBitmap != null){

            BitmapShader bitmapShader = new BitmapShader(theBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
            paint.setShader(bitmapShader);
            switch (viewMode){
                case CIRCLE_RECT:{
                    canvas.drawRoundRect(0,0,centerX * 2,centerY * 2,roundX,roundY, paint);
                }break;
                case CIRCLE:{
                    canvas.drawCircle(centerX, centerY, r, paint);
                }break;
                case RECT:{
                    canvas.drawRect(0,0,centerX * 2,centerY * 2,paint);
                }break;
            }

        }else {
            super.onDraw(canvas);
        }

    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        //第一次
        if(!isNoFirstSetBack){
            super.setBackgroundDrawable(background);
            drawable = getBackground();//获取背景
        }else {
            //非第一次，清空背景并绘图
            super.setBackgroundDrawable(null);
            theBitmap = Bitmap.createBitmap(centerX * 2, centerY * 2, Bitmap.Config.ARGB_8888);
            theCanvas.setBitmap(theBitmap);
            drawable.setBounds(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
            drawable.draw(theCanvas);
        }
    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(null);
        theBitmap = Bitmap.createBitmap(centerX, centerY, Bitmap.Config.ARGB_8888);
        theCanvas.setBitmap(theBitmap);
        drawable.setBounds(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
        drawable.draw(theCanvas);
    }

    //设置背景
    public void setTheBitmap(Bitmap bitmap){
        theBitmap = bitmap;
        loadHandler.sendEmptyMessage(LoadHanlder.URL_LOAD);
    }
    //文件加载
    /**@param sampleSize  //缩放倍数,如果小于等于1，自动校准
     * */
    public void setTheBitmap(File file, int sampleSize){
        setTheBitmap(file.getPath(), sampleSize);
    }
    public void setTheBitmap(String path, int sampleSize){
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if(sampleSize <= 1){
            sampleSize = calculateSampleSize(theBitmap);
            System.out.println(sampleSize);
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        theBitmap = BitmapFactory.decodeFile(path, options);
        loadHandler.sendEmptyMessage(LoadHanlder.URL_LOAD);
    }
    public void setTheBitmap(Uri uri, int sampleSize){
        setTheBitmap(uri.getPath(), sampleSize);
    }
    //源
    public void setTheBitmap(int id){
        theBitmap = BitmapFactory.decodeResource(getContext().getResources(), id);
        loadHandler.sendEmptyMessage(LoadHanlder.URL_LOAD);
    }
    //url加载图片,图片过大会造成影响
    /**@param sampleSize //缩放倍数，网络加载不会自动计算，可能造成图片显示不准，如果小于等于1，将直接Bitmap.createScaleBitmap缩放
     * @param executorService //运行在的线程池
     * @param url //网络url，统一使用httpUrlConnection加载
     * @see HttpURLConnection
     * @param requestMethod //请求方法
     * */
    public void setTheBitmap(String url, String requestMethod, int sampleSize, @Nullable ExecutorService executorService) throws IOException {
        setTheBitmap(new URL(url), requestMethod, sampleSize, executorService);
    }
    public void setTheBitmap(URL url, String requestMethod, int sampleSize, @Nullable ExecutorService executorService) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);

        if(executorService != null){
            //线程池执行
            executorService.execute(new UrlLoadThread(this, urlConnection, sampleSize));
        }else {
            //url加载bitmap
            new UrlLoadThread(this, urlConnection, sampleSize).start();
        }
    }

    //图片缩放计算
    private int calculateSampleSize(Bitmap theBitmap){
        return Math.max(options.outWidth, options.outHeight) / Math.min(getMeasuredWidth(), getMeasuredHeight());
    }

    //url加载
    private static class UrlLoadThread extends Thread{
        private WeakReference<CustomImageView> customImageView;
        private HttpURLConnection urlConnection;
        private int sampleSize;
        /**@param sampleSize //缩放倍数
         * @param customImageView  //视图
         * @param urlConnection //链接
         * */
        public UrlLoadThread(CustomImageView customImageView, HttpURLConnection urlConnection, int sampleSize){
            this.customImageView = new WeakReference<>(customImageView);
            this.urlConnection = urlConnection;
            this.sampleSize = sampleSize;
        }

        @Override
        public void run() {
            try {
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();
                //缩放
                if(sampleSize < 1){
                    customImageView.get().theBitmap = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeStream(stream),
                            customImageView.get().getMeasuredWidth(),
                            customImageView.get().getMeasuredHeight(),
                            true);
                }else{
                    customImageView.get().options.inSampleSize = sampleSize;
                    customImageView.get().theBitmap = BitmapFactory
                            .decodeStream(stream, null, customImageView.get().options);
                }
                stream.close();
                urlConnection.disconnect();
                urlConnection = null;
                customImageView.get().loadHandler.sendEmptyMessage(LoadHanlder.URL_LOAD);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadHanlder extends Handler {

        private final WeakReference<CustomImageView> customImageView;

        public static final int URL_LOAD = 1;

        public LoadHanlder(CustomImageView customImageView){
            this.customImageView = new WeakReference<>(customImageView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == URL_LOAD){
                customImageView.get().invalidate();
            }
        }
    }
}
