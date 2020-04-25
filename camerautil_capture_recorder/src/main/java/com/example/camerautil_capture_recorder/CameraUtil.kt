package com.example.camerautil_capture_recorder

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.view.SurfaceView
import android.view.TextureView
import androidx.core.net.toFile
import com.example.camerautil_capture_recorder.camera.TheCameraDeviceCallBack
import com.example.camerautil_capture_recorder.camera.TheSurfaceViewCallBack
import com.example.camerautil_capture_recorder.camera.TheTextureListener
import com.example.camerautil_capture_recorder.databean.SettingBean
import com.example.camerautil_capture_recorder.databean.UtilBean
import java.io.File


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
    private lateinit var settingBean: SettingBean
    private lateinit var utilBean: UtilBean//私有拍照通用信息

    //标志位
    private var type:Int = 0  //0textureview 1surfaceview
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
    constructor(context: Context, surfaceView: SurfaceView, settingBean:SettingBean){
        type = 1
        //sdk高于29才行
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
           this.settingBean = settingBean
           initBean(surfaceView)
           initSetting(context, settingBean)
           initSurfaceCallBack()
        }else{
            throw ErrorMessage.getSDKErrorMessage("CameraUtil.constructor(context: Context, surfaceView: SurfaceView, settingBean: SettingBean)")
        }
    }


    //初始化拍照bean
    private fun initBean(surfaceView: SurfaceView){
        utilBean = UtilBean.getInstance()
        utilBean.setSettingBean(settingBean)
        utilBean.setSurfaceView(surfaceView)
        utilBean.setOpenType(1)
        utilBean.setSurface(surfaceView.holder.surface)
    }
    private fun initBean(textureView: TextureView){

    }

    //初始化surfaceviewcallback
    private fun initSurfaceCallBack() {
        var theSurfaceViewCallBack: TheSurfaceViewCallBack =
            TheSurfaceViewCallBack(context, utilBean, settingBean)
        utilBean.getSurfaceView().holder.addCallback(theSurfaceViewCallBack)

    }

    /**构造方法，适用5.0，sdk21以上
     * @param context //上下文
     * @param textureView  //视图显示
     * @param settingBean //设置bean
     * @param mode //0拍照 1录像
     * */
    constructor(context: Context, textureView: TextureView, settingBean: SettingBean){
        type = 0
        utilBean = UtilBean.getInstance()
        utilBean.setSettingBean(settingBean)
        utilBean.setTextureView(textureView)
        initSetting(context, settingBean)
        initTextureViewListener()
    }
    //初始化textureview监听
    private fun initTextureViewListener(){
        val theTextureListener: TheTextureListener =
            TheTextureListener(settingBean, utilBean, context)
        utilBean.setTextureListener(theTextureListener)
        utilBean.getTextureView().surfaceTextureListener = utilBean.gettTextureListener()

    }

    //设置初始化
    private fun initSetting(context: Context, settingBean: SettingBean){
        this.context = context
        this.settingBean = settingBean
        makeImageReader()
        utilBean.setContext(context)
        utilBean.setImageReader(imageReader)
        utilBean.setResultFile(File(context.filesDir.toString() + "/cameraFinal.mp4"))
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        utilBean.setCameraManager(cameraManager)
        //数据矫正
        if(settingBean.getHeight() <= 0){
            settingBean.setWidth(1280)
        }

        if(settingBean.getWidth() <= 0){
            settingBean.setHeight(720)
        }

        if(settingBean.getMaxImages() <= 0){
            settingBean.setMaxImages(1)
        }

        if(settingBean.getFormat() <= 0){
            settingBean.setFormat(ImageFormat.JPEG)
        }
    }

    /*******************************相机操控方法区**************************************/
    //关闭相机
    fun closeCamera(){
        utilBean.closeCamera()
    }
    //切换相机
    /**@param cameraId  //cameraId
     * */
    fun switchCamera(cameraId: String){

        if(utilBean.getCameraDevice() != null){
            utilBean.getCameraDevice().close()
        }
        settingBean.setCameraId(cameraId)
        utilBean.openCamera(context, type)


    }
    //切换相机,默认切换前后
    fun switchCamera(){

        if(utilBean.getCameraDevice() != null){
            utilBean.getCameraDevice().close()
        }
        if(settingBean.getCameraId().equals(FONT_CAMERA)){
            settingBean.setCameraId(BACK_CAMERA)

        }else if(settingBean.getCameraId().equals(BACK_CAMERA)){
            settingBean.setCameraId(FONT_CAMERA)
        }
        utilBean.openCamera(context, type)
    }

    //摄制
    fun takePicture(){

        utilBean.takePicture()

    }

    //生成imagereader
    private fun makeImageReader(){
        imageReader = ImageReader.newInstance(settingBean.getWidth(),
            settingBean.getHeight(),
            settingBean.getFormat(),
            settingBean.getMaxImages())
    }

    /**图片监听type设置
     * @param acquireType      /**模式
    *                   {详情至 TheImageAvaiableListener}*/
     * */
    fun setAcquireType(acquireType:Int){utilBean.setAcquireType(acquireType)}
    fun getAcquireType():Int{return utilBean.getAcquireType()}

    /************************录像*******************************/
    //开始
    fun startRecorder(){
        utilBean.startRecorder()
    }
    //结束
    fun stopRecorder(){
        utilBean.stopRecorder()
    }
    //重置
    fun resetRecorder(){
        utilBean.reset(type)
    }
    //获取最终结果
    fun getFinallyRecorder():File{
        return utilBean.getResultFile()
    }
    //设置最终存储文件
    fun setFinallyFile(resultFile: File){
        utilBean.setResultFile(resultFile)
    }
    /**@param uri //uri
     * */
    fun setFinallyFile(uri:Uri){
        utilBean.setResultFile(uri.toFile())
    }
    fun setFinallyFile(path:String){
        utilBean.setResultFile(File(path))
    }
}
