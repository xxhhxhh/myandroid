package com.example.camerautil.camera

import android.content.Context
import android.view.SurfaceHolder
import com.example.camerautil.databean.SettingBeanForCapture
import com.example.camerautil.databean.UtilBeanForCapture

/**拍照回调
 * @param context //上下文
 * @param utilBeanForCapture //拍照bean
 * @param settingBeanForCapture //设置bean
 * */
class TheSurfaceViewCallBack(val context: Context,
                             val utilBeanForCapture: UtilBeanForCapture,
                             val settingBeanForCapture: SettingBeanForCapture
) : SurfaceHolder.Callback {


    //图片监听
    private val avaiableListener: TheImageAvaiableListener =
        TheImageAvaiableListener(settingBeanForCapture, utilBeanForCapture)


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        utilBeanForCapture.closeCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        holder!!.setFixedSize(settingBeanForCapture.getWidth(), settingBeanForCapture.getHeight())
        utilBeanForCapture.getImageReader().setOnImageAvailableListener(avaiableListener, null)
        utilBeanForCapture.openCamera(context)
    }
}
