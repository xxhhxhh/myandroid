package com.xxhhxhh.mainthing.camera;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Size;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GetCameraCharacteristics {

    //返回最适合尺寸
    public static Size largest(CameraManager cameraManager, String mode) throws Exception{
        return Collections.max(Arrays.asList(
               cameraManager.getCameraCharacteristics(mode).
                       get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).
                       getOutputSizes(ImageFormat.JPEG)), new CompareSize());
    }



    //内部比较类
    static class CompareSize implements Comparator<Size>
    {

        @Override
        public int compare(Size o1, Size o2) {
            return 0;
        }
    }
}
