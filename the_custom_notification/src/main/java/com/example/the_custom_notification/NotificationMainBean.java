package com.example.the_custom_notification;

import android.app.NotificationManager;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class NotificationMainBean {

    private final Map<Integer, NotificationBean> notificationBeanMap = new HashMap<>();//通知map
    private final NotificationHandler notificationHandler;//通知消息处理
    private final NotificationManager notificationManager;

    public NotificationManager getNotificationManager() { return notificationManager; }
    public Map<Integer, NotificationBean> getNotificationBeanMap() { return notificationBeanMap; }
    public NotificationHandler getNotificationHandler() { return notificationHandler; }

    /**@param context //上下文
     * */
    public NotificationMainBean(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationHandler = new NotificationHandler(notificationManager);
    }

}
