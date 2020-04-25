package com.example.camerautil

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Build
import android.view.SurfaceView
import android.view.TextureView
import android.view.Surface
import com.example.camerautil.camera.*
import com.example.camerautil.databean.*


/**权限需求
 *  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.CAMERA"/>
 创建类实例将自动申请，也可通过 CameraUtil.permissionRequest申请
                    使用CameraUtil.permissionCheck检查权限
 * */
//相机工具类
class CameraUtil{

    //上下文
    private lateinit var context:Context
    private fun setContext(context: Context){ this.context = context }
    fun getContext():Context{ return context }
    //imagereader
    private lateinit var imageReader: ImageReader
    //相机管理器
    private lateinit var  cameraManager:CameraManager

    //bean
    private lateinit var settingBeanForCapture: SettingBeanForCapture
    private lateinit var utilBeanForCapture: UtilBeanForCapture//私有拍照通用信息

    companion object{
        //默认值
        val FONT_CAMERA:String = android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT.toString()//前置
        val BACK_CAMERA:String = android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK.toString()//后置
    }

    /**构造方法，适用sdk29以上
     * @param context //上下文
     * @param surfaceView  //视图显示
     * @param settingBeanForCapture //设置bean
     * @param mode  //0拍照 1录像
     * */
    constructor(context: Context, surfaceView: SurfaceView, settingBeanIml: SettingBeanIml){
        //sdk高于29才行
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
           settingBeanForCapture = settingBeanIml as SettingBeanForCapture
           initCaptureBean(surfaceView)
           initSettingForCapture(context, settingBeanForCapture)
           initSurfaceCallBack()
        }else{
            throw ErrorMessage.getSDKErrorMessage("CameraUtil.constructor(context: Context, surfaceView: SurfaceView, settingBean: SettingBean)")
        }
    }


    //初始化拍照bean
    private fun initCaptureBean(surfaceView: SurfaceView){
        utilBeanForCapture = UtilBeanForCapture.getInstance()
        utilBeanForCapture.setSettingBean(settingBeanForCapture)
        utilBeanForCapture.setSurfaceView(surfaceView)
        utilBeanForCapture.setOpenType(1)
        utilBeanForCapture.setSurface(surfaceView.holder.surface)
    }
    private fun initCaptureBean(textureView: TextureView){

    }

    //初始化surfaceviewcallback
    private fun initSurfaceCallBack() {
        var theSurfaceViewCallBack: TheSurfaceViewCallBack =
            TheSurfaceViewCallBack(
                context,
                utilBeanForCapture,
                settingBeanForCapture
            )
        utilBeanForCapture.getSurfaceView().holder.addCallback(theSurfaceViewCallBack)

    }

    /**构造方法，适用5.0，sdk21以上
     * @param context //上下文
     * @param textureView  //视图显示
     * @param settingBeanForCapture //设置bean
     * @param mode //0拍照 1录像
     * */
    constructor(context: Context, textureView: TextureView, settingBeanForCapture: SettingBeanForCapture){
        utilBeanForCapture = UtilBeanForCapture.getInstance()
        utilBeanForCapture.setSettingBean(settingBeanForCapture)
        utilBeanForCapture.setTextureView(textureView)
        initSettingForCapture(context, settingBeanForCapture)
        initTextureViewListener()
    }
    //初始化textureview监听
    private fun initTextureViewListener(){
        val theTextureListener: TheTextureListener =
            TheTextureListener(settingBeanForCapture, utilBeanForCapture, context)
        utilBeanForCapture.setTextureListener(theTextureListener)
        utilBeanForCapture.getTextureView().surfaceTextureListener = utilBeanForCapture.gettTextureListener()

    }

    //设置初始化
    private fun initSettingForCapture(context: Context, settingBeanForCapture: SettingBeanForCapture){
        this.context = context
        this.settingBeanForCapture = settingBeanForCapture
        makeImageReader()
        utilBeanForCapture.setImageReader(imageReader)
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        utilBeanForCapture.setCameraManager(cameraManager)
        val cameraDeviceCallBack: TheCameraDeviceCallBack = TheCameraDeviceCallBack(utilBeanForCapture)
        utilBeanForCapture.setCameraDeviceStateCallBack(cameraDeviceCallBack)
        //数据矫正
        if(settingBeanForCapture.getHeight() <= 0){
            settingBeanForCapture.setWidth(1280)
        }

        if(settingBeanForCapture.getWidth() <= 0){
            settingBeanForCapture.setHeight(720)
        }

        if(settingBeanForCapture.getMaxImages() <= 0){
            settingBeanForCapture.setMaxImages(1)
        }

        if(settingBeanForCapture.getFormat() <= 0){
            settingBeanForCapture.setFormat(ImageFormat.JPEG)
        }
    }

    /*******************************相机操控方法区**************************************/
    //关闭相机
    fun closeCamera(){
        utilBeanForCapture.closeCamera()
    }
    //切换相机
    /**@param cameraId  //cameraId
     * */
    fun switchCamera(cameraId: String){

        if(utilBeanForCapture.getCameraDevice() != null){
            utilBeanForCapture.getCameraDevice().close()
        }
        settingBeanForCapture.setCameraId(cameraId)
        utilBeanForCapture.openCamera(context)


    }
    //切换相机,默认切换前后
    fun switchCamera(){

        if(utilBeanForCapture.getCameraDevice() != null){
            utilBeanForCapture.getCameraDevice().close()
        }
        if(settingBeanForCapture.getCameraId().equals(FONT_CAMERA)){
            settingBeanForCapture.setCameraId(BACK_CAMERA)

        }else if(settingBeanForCapture.getCameraId().equals(BACK_CAMERA)){
            settingBeanForCapture.setCameraId(FONT_CAMERA)
        }
        utilBeanForCapture.openCamera(context)
    }

    //摄制
    fun takePicture(){

        utilBeanForCapture.takePicture()

    }

    //生成imagereader
    private fun makeImageReader(){
        imageReader = ImageReader.newInstance(settingBeanForCapture.getWidth(),
            settingBeanForCapture.getHeight(),
            settingBeanForCapture.getFormat(),
            settingBeanForCapture.getMaxImages())
    }

    /**图片监听type设置
     * @param type      /**模式
    *                   {详情至 TheImageAvaiableListener}*/
     * */
    fun setType(type:Int){utilBeanForCapture.setType(type)}
    fun getType():Int{return utilBeanForCapture.getType()}


}
