package com.example.camerautil.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.example.camerautil.databean.SettingBeanForCapture
import com.example.camerautil.databean.UtilBeanForCapture

class TheTextureListener(val settingBeanForCapture: SettingBeanForCapture, val utilBeanForCapture: UtilBeanForCapture, val context: Context) : TextureView.SurfaceTextureListener {


    //图片监听
    private val avaiableListener: TheImageAvaiableListener =
        TheImageAvaiableListener(settingBeanForCapture, utilBeanForCapture)

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        Log.d("textureviewListener", "destroy")
        surface!!.release()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.d("textureviewListener", "avaiable")
        if(surface != null){
            utilBeanForCapture.setSurface(Surface(surface))
            surface!!.setDefaultBufferSize(settingBeanForCapture.getWidth(), settingBeanForCapture.getHeight())//默认视图大小
            utilBeanForCapture.getImageReader().setOnImageAvailableListener(avaiableListener, null)
            utilBeanForCapture.openCamera(context)
        }

    }


}
