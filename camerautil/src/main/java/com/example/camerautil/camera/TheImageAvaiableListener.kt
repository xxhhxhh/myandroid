package com.example.camerautil.camera

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import com.example.camerautil.ErrorMessage
import com.example.camerautil.databean.SettingBeanForCapture
import com.example.camerautil.databean.UtilBeanForCapture
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class TheImageAvaiableListener(val settingBeanForCapture: SettingBeanForCapture, val utilBeanForCapture: UtilBeanForCapture) : ImageReader.OnImageAvailableListener{



    override fun onImageAvailable(reader: ImageReader?) {

        when(utilBeanForCapture.getType()){
            //最新
            1-> getAndSaveImage(utilBeanForCapture.getImageReader().acquireLatestImage())
            //下一个
            else-> getAndSaveImage(utilBeanForCapture.getImageReader().acquireNextImage())
        }

    }

    //获取图片
    private fun getAndSaveImage(image:Image){

        if(utilBeanForCapture.getImageReader().imageFormat != ImageFormat.PRIVATE){
            var planes:Array<Image.Plane> = image.planes
            if(settingBeanForCapture.getImageFile() != null){
                saveImage(planes[0].buffer, settingBeanForCapture.getImageFile())
                image.close()
            }else{
                image.close()
                throw ErrorMessage.getFileErrorMessage()
            }
        }else{
            image.close()
            throw ErrorMessage.getImageErrorMessage()
        }

    }

    /**文件存储
     * @param buffer //数据
     * @param file  //存储到文件
     * */
    private fun saveImage(buffer:ByteBuffer, file: File){
        try{
            var outputStream:BufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
            var byteArray:ByteArray = ByteArray(buffer.remaining())
            buffer.get(byteArray)
            outputStream.write(byteArray)
            outputStream.flush()
            outputStream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }


}
