package com.example.camerautil_capture_recorder.databean

import android.graphics.ImageFormat
import android.media.MediaRecorder
import android.net.Uri
import androidx.core.net.toFile
import com.example.camerautil_capture_recorder.CameraUtil
import com.example.camerautil_capture_recorder.ErrorMessage
import java.io.File

/**存储配置信息
 * */
class SettingBean {

    /*******通用********/
    //宽
    private var width:Int = 1280
     fun setWidth(width:Int){this.width = width}
    fun getWidth():Int{return width}

    //高
    private var height:Int = 720
    fun setHeight(height:Int){this.height = height}
    fun getHeight():Int{return height}

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
    //视频格式
    private val videoSource:Int = MediaRecorder.VideoSource.SURFACE
//    fun setVideoSource(videoSoruce:Int){this.videoSource = videoSource}
    fun getVideoSource():Int{return videoSource}
    private var videoEncoder:Int = MediaRecorder.VideoEncoder.MPEG_4_SP
    fun setVideoEncoder(videoEncoder:Int){this.videoEncoder = videoEncoder}
    fun getVideoEncoder():Int{return videoEncoder}
    //音频格式
    private var audioSource:Int = MediaRecorder.AudioSource.MIC
    fun setAudioSource(audioSource:Int){this.audioSource = audioSource}
    fun getAudioSource():Int{return audioSource}
    private var audioEncoder:Int = MediaRecorder.AudioEncoder.DEFAULT
    fun setAudioEncoder(audioEncoder:Int){this.audioEncoder = audioEncoder}
    fun getAudioEncoder():Int{return audioEncoder}
    //视频文件
    private var videoFile:File? = null
    fun setVideoFile(videoFile: File){this.videoFile = videoFile}
    fun getVideoFile():File?{return videoFile}
    private var videoPath:String? = null
    fun setVideoPath(videoPath:String){this.videoPath = videoPath}
    fun getVideoPath():String?{return videoPath}
    //输出格式
    private var outPutFormat:Int = MediaRecorder.OutputFormat.MPEG_4
    fun setOutPutFormat(outPutFormat:Int){this.outPutFormat = outPutFormat}
    fun getOutPutFormat():Int{return outPutFormat}
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
