package com.example.the_custom_notification.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.*;
import androidx.annotation.Nullable;
import com.example.the_custom_notification.NotificationBean;
import com.example.the_custom_notification.NotificationHandler;
import com.example.the_custom_notification.NotificationIdentify;

import java.util.HashMap;
import java.util.Map;

public class NotificationBinder extends Binder {

    private final Context context;//上下文
    private final Map<Integer, NotificationBean> notificationBeanMap = new HashMap<>();//通知map
    private final NotificationManager notificationManager;
    private final NotificationHandler notificationHandler;//通知消息处理

    public NotificationBinder(Context context){
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationHandler = new NotificationHandler(notificationManager);
        //8.0及以上
        if(Build.VERSION_CODES.O <= Build.VERSION.SDK_INT){
            NotificationChannel notificationChannel = new NotificationChannel(NotificationService.CHANNEL_ID, "channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //绑定服务发送消息
    //发送消息  发送通知
    public void sendNotification(int id, @Nullable String notificationIdentify){

        if(notificationBeanMap.get(id) == null){
            NotificationBean notificationBean = new NotificationBean(id, context, NotificationService.CHANNEL_ID);
            notificationBeanMap.put(id, notificationBean);
        }
        sendNotification(notificationBeanMap.get(id), notificationIdentify);
    }
    public void sendNotification(NotificationBean notificationBean, @Nullable String notificationIdentify){
        if(!notificationBeanMap.values().contains(notificationBean)){
            if(notificationBeanMap.get(notificationBean.getId()) == null){
                notificationBeanMap.put(notificationBean.getId(), notificationBean);
            }
        }
        //删除
        else if(notificationIdentify.equals(NotificationIdentify.REPLACE)){
            notificationManager.cancel(notificationBean.getId());
            notificationBeanMap.put(notificationBean.getId(), notificationBean);
        }

        Message message = new Message();
        message.what = NotificationHandler.NOTIFICATION_FORM_BINDER;
        Bundle bundle = new Bundle();
        bundle.putInt(NotificationHandler.BUNDLE_KEY_ID, notificationBean.getId());
        bundle.putParcelable(NotificationHandler.BUNDLE_KEY_NOTIFICATION,
                notificationBeanMap
                .get(notificationBean.getId()).getNotification());
        message.setData(bundle);
        notificationHandler.sendMessage(message);
    }



    //获取通知bean
    public NotificationBean getNotificationBean(int id){
        return notificationBeanMap.get(id);
    }
}
