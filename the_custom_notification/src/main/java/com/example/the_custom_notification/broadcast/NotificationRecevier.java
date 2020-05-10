package com.example.the_custom_notification.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import com.example.the_custom_notification.NotificationHandler;
import com.example.the_custom_notification.NotificationIdentify;

import java.util.HashMap;
import java.util.Map;

/**广播发送通知，此广播动态注册
 * */
public class NotificationRecevier extends BroadcastReceiver {

    private final Map<Integer, Notification> notificationBeanMap = new HashMap<>();//通知map
    private final NotificationHandler notificationHandler;//通知消息处理
    private final NotificationManager notificationManager;
    public static final String THE_BORDCAST = "com.example.notification";//注册广播用action

    public NotificationRecevier(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationHandler = new NotificationHandler(notificationManager);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getParcelableExtra(NotificationHandler.BUNDLE_KEY_NOTIFICATION) != null){
            System.out.println(12345);
            int id = intent.getIntExtra(NotificationHandler.BUNDLE_KEY_ID, -1);
            Notification notification = (Notification)(intent.getParcelableExtra(NotificationHandler.BUNDLE_KEY_NOTIFICATION));
            String notificationIdentify = intent.getStringExtra(NotificationIdentify.NOTIFICATION_IDENTIFY);
            if(notificationIdentify == null){
                notificationIdentify = NotificationIdentify.NONE;
            }
            sendNotification(id, notification, notificationIdentify);
        }
    }

    //发送消息  发送通知
    public void sendNotification(int id, Notification notification, String notificationIdentify){
        if(notificationBeanMap.get(id) == null){
            System.out.println(11111);
            notificationBeanMap.put(id, notification);
        }//替换
        else if(notificationIdentify.equals(NotificationIdentify.REPLACE)){
            notificationManager.cancel(id);
            notificationBeanMap.put(id, notification);
        }
        sendNotification(id, notificationBeanMap.get(id));
    }
    //发送消息  发送通知
    public void sendNotification(int id, Notification notification){
        System.out.println(11111);
        notificationBeanMap.put(id, notification);
        Message message = new Message();
        message.what = NotificationHandler.NOTIFICATION_FORM_RECEVIER;
        Bundle bundle = new Bundle();
        bundle.putInt(NotificationHandler.BUNDLE_KEY_ID, id);
        bundle.putParcelable(NotificationHandler.BUNDLE_KEY_NOTIFICATION,
                notification);
        message.setData(bundle);
        notificationHandler.sendMessage(message);
    }

}
