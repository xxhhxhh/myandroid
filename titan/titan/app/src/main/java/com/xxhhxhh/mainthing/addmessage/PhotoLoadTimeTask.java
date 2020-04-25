package com.xxhhxhh.mainthing.addmessage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import com.xxhhxhh.mainthing.addmessage.fragment.PictureLookFrgment;

import java.util.TimerTask;

public class PhotoLoadTimeTask extends TimerTask {

    private Cursor cursor;
    private Context context;

   public PhotoLoadTimeTask(Cursor cursor, Context context)
   {
       this.cursor = cursor;
       this.context = context;
   }

    @Override
    public void run()
    {
        //每次10个
        for(int j = 0; j < 10; j++)
        {
            if(cursor != null && cursor.moveToNext())
            {
                PictureLookFrgment.photosMap.put(PictureLookFrgment.allPhotos, getOnePhoto(cursor));
                PictureLookFrgment.allPhotos = PictureLookFrgment.photosMap.size();
            }
        }
    }

    //获取一个图片
    private Bitmap getOnePhoto(Cursor cursor)
    {
        //获取图片保存位置数据
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        Uri base = Uri.parse("content://media/external/images/media");
        Uri nowUri = Uri.withAppendedPath(base, "" + id);

        try {
            ParcelFileDescriptor parcelFileDescriptor =  context.getContentResolver().openFileDescriptor(nowUri, "r");
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 100, true);
            return bitmap;
        }
        catch (Exception e)
        {

        }
        return null;
    }
}
