package com.xxhhxhh.mainthing.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.xxhhxhh.mainthing.Factory.MakeUUID;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;


import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class CameraStateCallBack extends CameraDevice.StateCallback
{

    private CameraCaptureSession cameraCaptureSession;
    private static CameraDevice camera;
    private Surface surface;
    private ImageReader imageReader;
    private CaptureRequest captureRequest;
    private CameraManager cameraManager;
    private Activity activity;
    private Size previewSize;//预览尺寸
    //预览视图尺寸
    public static int width;
    public static int height;
    //返回用数据
    private Map<String, File> returnData = new HashMap<String, File>();

    public CameraStateCallBack(TextureView textureView, Activity activity, String mode) throws Exception{

        this.activity = activity;
        //预览设置
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surface = new Surface(surfaceTexture);
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        //摄像头特性
        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mode);
        //配置属性
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        //摄像头支持最大尺寸
        Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSize());
        //预览尺寸
         previewSize = choseOptimalSize(map.getOutputSizes(SurfaceTexture.class), largest);
         //长宽比调整
         if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
         {
             textureView.setMinimumHeight(previewSize.getHeight());
             textureView.setMinimumWidth(previewSize.getWidth());
         }
         else
         {
             textureView.setMinimumHeight(previewSize.getWidth());
             textureView.setMinimumWidth(previewSize.getHeight());
         }
        imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);

    }

    //断开
    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        camera.close();
    }

    //错误
    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        camera.close();

    }

    //外部调用，用于切换摄像头
    public static void closeThis()
    {
        camera.close();
    }


    public void captureStillPicture() throws Exception
    {
        final CaptureRequest.Builder builder;
        //拍照
        builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        builder.addTarget(imageReader.getSurface());//目标
        //停止连续取景
        cameraCaptureSession.stopRepeating();
        //自动对焦
        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        //自动曝光
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        //捕获静态图像
        cameraCaptureSession.capture(builder.build(), new CameraCaptureSession.CaptureCallback()
        {
            //拍照完成
            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                           @NonNull CaptureRequest request,
                                           @NonNull TotalCaptureResult result)
            {
                super.onCaptureCompleted(session, request, result);
                try
                {
                    //自动对焦
                    builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    //自动曝光
                    builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                    cameraCaptureSession.setRepeatingRequest(request, null, null);
                }
                catch (CameraAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    public void capturePreview() throws Exception
    {
        //预览
        final CaptureRequest.Builder captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);
        //请求处理
        camera.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                //开始预览
                cameraCaptureSession = session;
                //设置
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                //显示预览
                captureRequest = captureRequestBuilder.build();

                try {
                    cameraCaptureSession.setRepeatingRequest(captureRequest, null, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                System.out.println(1234);
            }
        }, null);

    }

    //输出图片
    public Map<String, File> outPut()
    {
        //获取照片数据
        Image image = imageReader.acquireLatestImage();
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        File file = new File(activity.getDataDir().toString(),
                MakeUUID.makeUUID("i") + ".jpg");

        try( FileOutputStream outputStream = new FileOutputStream(file))
        {
            outputStream.write(bytes);
            Toast.makeText(activity.getBaseContext(), "w1", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            image.close();//关闭
            camera.close();
        }


        returnData.put("photo", file);
        return returnData;
    }

    //打开
    @Override
    public void onOpened(@NonNull CameraDevice camera)
    {
        CameraStateCallBack.camera = camera;
        try
        {
            capturePreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //选取最佳尺寸
    private static Size choseOptimalSize(Size[] choices, Size largest)
    {
        //手机摄像头大于预览视图的分表率
        List<Size> bigs = new ArrayList<>();
        int w = largest.getWidth();
        int h = largest.getHeight();
        for(Size option : choices)
        {
            if(option.getWidth() == option.getHeight() * h / w && option.getWidth() >= width
                && option.getHeight() >= height)
            {
                bigs.add(option);
            }
        }

        //取最小尺寸
        if(bigs.size() > 0)
        {
            return Collections.min(bigs, new CompareSize());
        }
        else
        {
            return choices[0];//找不到最佳尺寸
        }

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
