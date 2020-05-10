package com.example.the_custom_notification;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.example.the_custom_notification.broadcast.NotificationRecevier;
import com.example.the_custom_notification.service.NotificationBinder;

/**发送通知类
 * */
public class SendNotification {


    //广播发送通知
    public static void sendBroadCast(Context context, NotificationBean notificationBean,@Nullable String notificationIdentify){
        if(notificationBean != null){
            Intent intent = new Intent();
            intent.setAction(NotificationRecevier.THE_BORDCAST);
            intent.putExtra(NotificationIdentify.NOTIFICATION_IDENTIFY, notificationIdentify);
            intent.putExtra(NotificationHandler.BUNDLE_KEY_ID, notificationBean.getId());
            intent.putExtra(NotificationHandler.BUNDLE_KEY_NOTIFICATION, notificationBean.getNotification());
            context.sendBroadcast(intent);
        }
    }

    //发送通知，利用服务
    public static void sendWithService(NotificationBinder notificationBinder, NotificationBean notificationBean, @Nullable String notificationIdentify){
        notificationBinder.sendNotification(notificationBean, notificationIdentify);
    }
    public static void sendWithService(NotificationBinder notificationBinder, int id,@Nullable String notificationIdentify){
        notificationBinder.sendNotification(id, notificationIdentify);
    }
}
