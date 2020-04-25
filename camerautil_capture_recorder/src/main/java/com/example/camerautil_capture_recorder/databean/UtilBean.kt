package com.example.camerautil_capture_recorder.databean

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.*
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import com.example.camerautil_capture_recorder.ErrorMessage
import com.example.camerautil_capture_recorder.PermissionDo
import com.example.camerautil_capture_recorder.camera.TheCameraDeviceCallBack
import com.example.camerautil_capture_recorder.camera.TheCaptureCallBack
import com.example.camerautil_capture_recorder.camera.TheTextureListener
import java.io.File

//通用信息工具类
class UtilBean {

    companion object{
        private var utilBean: UtilBean? = null

        //默认
        fun getInstance(): UtilBean {

            if(utilBean == null){
                utilBean =
                    UtilBean()
                val handlerThread:HandlerThread = HandlerThread("Camera2Util")
                handlerThread.start()
                utilBean!!.setHandler(Handler(handlerThread.looper))
            }

            return utilBean!!

        }

        //带参
        /**@param handler //拍照用handler
         * */
        fun getInstance(handler: Handler): UtilBean {
            if(utilBean == null){
                utilBean =
                    UtilBean()
                utilBean!!.setHandler(handler)
            }

            return utilBean!!
        }
    }
    //context
    private lateinit var context: Context
    fun setContext(context: Context){this.context = context}
    fun getContext():Context{return context}
    //mediarecorder
    private var mediaRecorder:MediaRecorder = MediaRecorder()
    fun setMediaRecorder(mediaRecorder: MediaRecorder){this.mediaRecorder = mediaRecorder}
    fun getMediaRecorder():MediaRecorder{return mediaRecorder}
    //handler
    private lateinit var handler:Handler
    fun setHandler(handler: Handler){this.handler = handler}
    fun getHandler():Handler{return handler}
    //settingbean
    private var settingBean: SettingBean? = null
    fun getSettingBean(): SettingBean {return settingBean!!}
    fun setSettingBean(settingBean: SettingBean){this.settingBean = settingBean}
    private var isEnd:Boolean = false //摧毁标志
    fun setIsEnd(isEnd:Boolean){this.isEnd = isEnd}
    fun getIsEnd():Boolean{return isEnd}
    //用于修改图片监听内部type
    private var acquireType:Int = 0
    fun setAcquireType(acquireType:Int){this.acquireType = acquireType}
    fun getAcquireType():Int{return acquireType}
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
    //最终结果
    private lateinit var resultFile:File
    fun setResultFile(resultFile: File){this.resultFile = resultFile}
    fun getResultFile():File{return resultFile}
    //处理方法
    //打开相机
    /**@param context //上下文
     * @param type //0textureview  1surfaceview
     * */
    @SuppressLint("MissingPermission")
    fun openCamera(context:Context, type:Int){
        //权限检查
        if (PermissionDo.permissionCheck(context, PermissionDo.permissions)) {
            val cameraDeviceCallBack: TheCameraDeviceCallBack = TheCameraDeviceCallBack(this, getSettingBean(), type)
            setCameraDeviceStateCallBack(cameraDeviceCallBack)
            when(getOpenType()){
                0->{
                    if(textureView.isAvailable && settingBean != null){
                        getCameraManager().openCamera(settingBean!!.getCameraId(), getCameraDeviceStateCallBack(), getHandler())
                    }
                }
                1->{
                    getCameraManager().openCamera(settingBean!!.getCameraId(), getCameraDeviceStateCallBack(), getHandler())
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

    /**************************录像*************************************/
    //初始化
    /**@param type //0textureview 1surfaceview
     * */
    fun initRecorder(type: Int){
        getMediaRecorder().reset()
        getMediaRecorder().setVideoSource(getSettingBean().getVideoSource())
        getMediaRecorder().setAudioSource(getSettingBean().getAudioSource())
        getMediaRecorder().setOutputFormat(getSettingBean().getOutPutFormat())
        getMediaRecorder().setVideoEncoder(getSettingBean().getVideoEncoder())
        getMediaRecorder().setAudioEncoder(getSettingBean().getAudioEncoder())
        getMediaRecorder().setVideoFrameRate(12)
        when(type){
            0->getMediaRecorder().setPreviewDisplay(getSurface())
            1->getMediaRecorder().setPreviewDisplay(getSurfaceView().holder.surface)
        }
        if(Build.VERSION.SDK_INT >= 26 && getSettingBean().getVideoFile() != null){
            getMediaRecorder().setOutputFile(getSettingBean().getVideoFile())
        }else if(getSettingBean().getVideoPath() != null){
            getMediaRecorder().setOutputFile(getSettingBean().getVideoPath())
        }else{
            throw ErrorMessage.getFileErrorMessage()
        }
        getMediaRecorder().prepare()
    }

    //开始
    fun startRecorder(){
        getMediaRecorder().start()
    }
    //结束
    fun stopRecorder(){
        getMediaRecorder().stop()
    }
    //释放
    fun release(){
        getMediaRecorder().release()
    }
    //重置
    fun reset(type: Int){
        if(Build.VERSION.SDK_INT >= 26 && getSettingBean().getVideoFile() != null){
            getSettingBean().getVideoFile()!!.renameTo(getResultFile())
        }else if(getSettingBean().getVideoPath() != null){
            File(getSettingBean().getVideoPath()).renameTo(getResultFile())
        }else{
            throw ErrorMessage.getFileErrorMessage()
        }
        initRecorder(type)
        openCamera(context, type)
    }
}
