package com.example.camerautil_capture_recorder.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.example.camerautil_capture_recorder.databean.SettingBean
import com.example.camerautil_capture_recorder.databean.UtilBean

class TheTextureListener(val settingBean: SettingBean, val utilBean: UtilBean, val context: Context) : TextureView.SurfaceTextureListener {


    //图片监听
    private val avaiableListener: TheImageAvaiableListener =
        TheImageAvaiableListener(settingBean, utilBean)

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
            utilBean.setSurface(Surface(surface))
            surface!!.setDefaultBufferSize(settingBean.getWidth(), settingBean.getHeight())//默认视图大小
            utilBean.getImageReader().setOnImageAvailableListener(avaiableListener, null)
            utilBean.initRecorder(0)
            utilBean.openCamera(context, 0)
        }

    }


}
