package com.example.the_custom_notification.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.example.the_custom_notification.service.NotificationBinder;

public class NotificationService  extends Service {

    public static final String CHANNEL_ID = "THE_NOTIFICATION_CHANNEL";//面板id

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        NotificationBinder notificationBinder = new NotificationBinder(this);
        return notificationBinder;
    }
}
