package com.example.camerautil.databean

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import com.example.camerautil.ErrorMessage
import com.example.camerautil.PermissionDo
import com.example.camerautil.camera.TheCaptureCallBack
import com.example.camerautil.camera.TheTextureListener

//通用信息工具类
class UtilBeanForCapture {

    companion object{
        private var utilBeanForCapture: UtilBeanForCapture? = null

        //默认
        fun getInstance(): UtilBeanForCapture {

            if(utilBeanForCapture == null){
                utilBeanForCapture =
                    UtilBeanForCapture()
                val handlerThread:HandlerThread = HandlerThread("Camera2Util")
                handlerThread.start()
                utilBeanForCapture!!.setHandler(Handler(handlerThread.looper))
            }

            return utilBeanForCapture!!

        }

        //带参
        /**@param handler //拍照用handler
         * */
        fun getInstance(handler: Handler): UtilBeanForCapture {
            if(utilBeanForCapture == null){
                utilBeanForCapture =
                    UtilBeanForCapture()
                utilBeanForCapture!!.setHandler(handler)
            }

            return utilBeanForCapture!!
        }
    }

    //handler
    private lateinit var handler:Handler
    fun setHandler(handler: Handler){this.handler = handler}
    fun getHandler():Handler{return handler}
    //settingbean
    private var settingBeanForCapture: SettingBeanForCapture? = null
    fun getSettingBean(): SettingBeanForCapture {return settingBeanForCapture!!}
    fun setSettingBean(settingBeanForCapture: SettingBeanForCapture){this.settingBeanForCapture = settingBeanForCapture}
    private var isEnd:Boolean = false //摧毁标志
    fun setIsEnd(isEnd:Boolean){this.isEnd = isEnd}
    fun getIsEnd():Boolean{return isEnd}
    //用于修改图片监听内部type
    private var type:Int = 0
    fun setType(type:Int){this.type = type}
    fun getType():Int{return type}
    //打开模式 0textureview 1surfaceview
    private var openType:Int = 0
    fun getOpenType(): Int {return openType}
    fun setOpenType(openType:Int){this.openType = openType}
    //textureviewlistener
    private var textureListenrer:TextureView.SurfaceTextureListener? = null
    fun setTextureListener(textureListener: TheTextureListener){this.textureListenrer = textureListener}
    fun gettTextureListener():TextureView.SurfaceTextureListener{return textureListenrer!!}
    //cameraManager
    private lateinit var cameraManager: CameraManager
    fun getCameraManager(): CameraManager {return cameraManager}
    fun setCameraManager(cameraManager: CameraManager){this.cameraManager = cameraManager}
    //cameraDevice
    private var cameraDevice: CameraDevice? = null
    fun setCameraDevice(cameraDevice: CameraDevice?){this.cameraDevice = cameraDevice}
    fun getCameraDevice(): CameraDevice {return cameraDevice!!}
    //textureView
    lateinit private var textureView: TextureView
    fun getTextureView(): TextureView {return textureView}
    fun setTextureView(textureView: TextureView){this.textureView = textureView}
    //surfaceview
    private lateinit var surfaceView: SurfaceView
    fun getSurfaceView(): SurfaceView {return surfaceView}
    fun setSurfaceView(surfaceView: SurfaceView){this.surfaceView = surfaceView}
    //surface
    private lateinit var surface: Surface
    fun getSurface():Surface{return surface}
    fun setSurface(surface:Surface){this.surface = surface}
    //captureSession
    private var captureSession: CameraCaptureSession? = null
    fun getCameraCaptureSession(): CameraCaptureSession {return captureSession!!}
    fun setCameraCaptureSession(captureSession: CameraCaptureSession){this.captureSession = captureSession}
    //CameraDevice.StateCallback()
    private var cameraDeviceStateCallBack:CameraDevice.StateCallback? = null
    fun getCameraDeviceStateCallBack():CameraDevice.StateCallback{return cameraDeviceStateCallBack!!}
    fun setCameraDeviceStateCallBack(cameraDeviceCallBack: CameraDevice.StateCallback){this.cameraDeviceStateCallBack = cameraDeviceCallBack}
    //imageReader
    private var imageReader:ImageReader? = null
    fun getImageReader():ImageReader{return imageReader!!}
    fun setImageReader(imageReader: ImageReader){this.imageReader = imageReader}
    //capturerequest预览
    private var previewRequest:CaptureRequest.Builder? = null
    fun getPreviewRequest(): CaptureRequest.Builder { return previewRequest!! }
    fun setPreviewRequest(previewRequest: CaptureRequest.Builder){this.previewRequest = previewRequest}
    //capturerequest捕捉
    private var captureRequest:CaptureRequest.Builder? = null
    fun getCaptureRequest(): CaptureRequest.Builder { return captureRequest!! }
    fun setCaptureRequest(captureRequest: CaptureRequest.Builder){this.captureRequest = captureRequest}

    //处理方法
    //打开相机
    /**@param context //上下文
     * @param type //0textureview  1surfaceview
     * */
    @SuppressLint("MissingPermission")
    fun openCamera(context:Context){
        //权限检查
        if (PermissionDo.permissionCheck(
                context,
                PermissionDo.permissions
            )
        ) {
            when(getOpenType()){
                0->{
                    if(textureView.isAvailable && settingBeanForCapture != null){
                        getCameraManager().openCamera(settingBeanForCapture!!.getCameraId(), getCameraDeviceStateCallBack(), getHandler())
                    }
                }
                1->{
                    getCameraManager().openCamera(settingBeanForCapture!!.getCameraId(), getCameraDeviceStateCallBack(), getHandler())
                }
                else->{
                    throw ErrorMessage.getOpenTypeErrorMessage()
                }
            }
        }else{
            throw ErrorMessage.getPermissionErrorMessage()
        }
    }
    //关闭摄像头
    fun closeCamera(){
        getCameraCaptureSession().close()
        getCameraDevice().close()
        if(imageReader != null)
        imageReader!!.close()
    }


    //摄制
    fun takePicture(){

        getCameraDevice().apply {
            var cameraCaptureSession: CaptureRequest.Builder = getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            cameraCaptureSession.addTarget(imageReader!!.surface)
            setCaptureRequest(cameraCaptureSession)
            getCameraCaptureSession()
                .capture(cameraCaptureSession.build(),
                    TheCaptureCallBack(
                        getInstance(),
                        null
                    ), getHandler())
        }

    }

}
