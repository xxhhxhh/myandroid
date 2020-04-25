package com.example.camerautil.databean

import android.graphics.ImageFormat
import android.net.Uri
import androidx.core.net.toFile
import com.example.camerautil.CameraUtil
import com.example.camerautil.ErrorMessage
import java.io.File

/**存储配置信息
 * */
class SettingBeanForCapture : SettingBeanIml {

    /*******通用********/
    //宽
    private var width:Int = 1280
    override fun setWidth(width:Int){this.width = width}
    override fun getWidth():Int{return width}

    //高
    private var height:Int = 720
    override fun setHeight(height:Int){this.height = height}
    override fun getHeight():Int{return height}

    /**********适用imageReader********************/
    //最大图片
    private var maxImages:Int = 1
    fun setMaxImages(maxImages:Int){this.maxImages = maxImages}
    fun getMaxImages():Int{return maxImages}
    /**图片格式
     * @param format //图片格式，默认jpeg，请使用
     * {android.graphics.ImageFormat} 或者
     * {android.graphics.PixelFormat} 其中内容
     * */
    private var format:Int = ImageFormat.JPEG
    fun setFormat(format:Int){this.format = format}
    fun getFormat():Int{return format}
    /**文件存储地址
     * @param file //文件
     * @param path //文件路径
     * @param uri //路径uri
     * */
    private var imageFile:File? = null
    fun setImageFile(file: File){this.imageFile = file}
    fun setImageFile(path:String){ this.imageFile = File(path) }
    fun setImageFile(uri:Uri){this.imageFile = uri.toFile()}
    fun getImageFile():File{
        if(imageFile != null){
            return imageFile!!
        }
        throw ErrorMessage.getFileErrorMessage()
    }

    /*************摄像头******************/
    //摄像头id
    private var cameraId:String = CameraUtil.BACK_CAMERA
    fun setCameraId(cameraId:String){this.cameraId = cameraId}
    fun getCameraId():String{return cameraId}

    /*********************摄制******************************/

}
