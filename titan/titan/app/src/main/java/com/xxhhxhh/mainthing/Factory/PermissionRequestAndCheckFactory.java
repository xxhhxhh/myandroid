package com.xxhhxhh.mainthing.Factory;

import android.app.Activity;
import android.content.pm.PackageManager;

public class PermissionRequestAndCheckFactory {

    public static boolean checkPermission(String[] permission, Activity activity)
    {
        for(String one : permission)
        {
            if(activity.checkCallingOrSelfPermission(one) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(String[] permission, Activity activity)
    {
        activity.requestPermissions(permission, 1);
    }

}
