package com.example.the_custom_notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import com.example.the_custom_notification.service.NotificationService;

public class NotificationBean {

    private Notification.Builder builder1 = null;//8.0以后
    private RemoteViews remoteView = null;
    private int smallIcon = R.drawable.default_icon;//小图标
    private String contentTitle = null;//内容标题
    private String contentText = null;//内容
    private int id;
    private PendingIntent pendingIntent = null;
    private boolean autoCancel = true;

    public boolean isAutoCancel() { return autoCancel; }
    public void setAutoCancel(boolean autoCancel) { this.autoCancel = autoCancel; }
    public int getId() { return id; }
    public PendingIntent getPendingIntent() { return pendingIntent; }
    public RemoteViews getRemoteView() { return remoteView; }
    public void setRemoteView(RemoteViews remoteView) { this.remoteView = remoteView; }
    public void setPendingIntent(PendingIntent pendingIntent) { this.pendingIntent = pendingIntent; }
    public Notification.Builder getBuilder1() { return builder1; }
    public String getContentText() { return contentText; }
    public int getSmallIcon() { return smallIcon; }
    public String getContentTitle() { return contentTitle; }
    public void setContentText(String contentText) { this.contentText = contentText; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    public void setSmallIcon(int smallIcon) { this.smallIcon = smallIcon; }

    /**@param channelId //面板id
     * @param context  //上下文
     * */
    public NotificationBean(int id, Context context, String channelId){
        this.id = id;
        this.remoteView = new RemoteViews(context.getPackageName(), R.layout.default_view);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            System.out.println("awdawdawd");
            builder1 = new Notification.Builder(context, channelId);
        }else {
            builder1 = new Notification.Builder(context);
        }
    }
    /**@param channelId //面板id
     * @param context  //上下文
     * @param remoteViews //自定义视图
     * */
    public NotificationBean(int id, Context context, String channelId, RemoteViews remoteViews){
        this.id = id;
        this.remoteView = remoteViews;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder1 = new Notification.Builder(context, channelId);
        }else {
            builder1 = new Notification.Builder(context);
        }
    }
    /**@param channelId //面板id
     * @param context  //上下文
     * @param resourceId //自定义视图id
     * */
    public NotificationBean(int id, Context context, String channelId, int resourceId){
        this.id = id;
        this.remoteView = new RemoteViews(context.getPackageName(), resourceId);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder1 = new Notification.Builder(context, channelId);
        }else {
            builder1 = new Notification.Builder(context);
        }
    }
    //获取通知
    public Notification getNotification(){

        if(pendingIntent != null){
            System.out.println(12344);
            builder1.setContentIntent(pendingIntent);
        }
        return builder1.setSmallIcon(smallIcon)
                .setCustomContentView(remoteView)
                .setContentText(contentText)
                .setAutoCancel(autoCancel)
                .setChannelId(NotificationService.CHANNEL_ID)
                .setContentTitle(contentTitle).build();
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if((obj instanceof NotificationBean)){
            return ((NotificationBean)obj).getId() == getId();
        }else {
            return false;
        }
    }
}
