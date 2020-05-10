package com.example.the_custom_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class NotificationHandler extends Handler {

    public static final int NOTIFICATION_FORM_BINDER = 1;
    public static final int NOTIFICATION_FORM_RECEVIER = 2;
    public static final String BUNDLE_KEY_NOTIFICATION = "notification_send";
    public static final String BUNDLE_KEY_ID = "notification_id";
    private final NotificationManager notificationManager;

    public NotificationHandler(NotificationManager notificationManager){
        this.notificationManager = notificationManager;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Bundle bundle = msg.getData();
        if (NOTIFICATION_FORM_BINDER == msg.what){
            if(bundle != null){
                sendNotification(bundle.getInt(BUNDLE_KEY_ID), (Notification) bundle.getParcelable(BUNDLE_KEY_NOTIFICATION));
            }
        }else if(msg.what == NOTIFICATION_FORM_RECEVIER){
            if(bundle != null){
                sendNotification(bundle.getInt(BUNDLE_KEY_ID), (Notification) bundle.getParcelable(BUNDLE_KEY_NOTIFICATION));
            }
        }
    }
    //发送通知
    private void sendNotification(int id, Notification notification){
        notificationManager.notify(id, notification);
    }

}
