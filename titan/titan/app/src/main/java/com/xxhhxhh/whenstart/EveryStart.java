package com.xxhhxhh.whenstart;

import android.app.Activity;

import java.io.File;

public class EveryStart {

    public  static void mkdirsMain(Activity activity)
    {
        File file = new File(activity.getDataDir() + "/files/jpgs");
        File file1 = new File(activity.getDataDir()+ "files/videos");
        if(!file.exists())
        {
            file.mkdirs();
        }
        if(!file1.exists())
        {
            file1.mkdirs();
        }
    }


}
