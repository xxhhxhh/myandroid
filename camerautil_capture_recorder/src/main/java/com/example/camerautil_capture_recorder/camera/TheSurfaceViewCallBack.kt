package com.example.camerautil_capture_recorder.camera

import android.content.Context
import android.view.SurfaceHolder
import com.example.camerautil_capture_recorder.databean.SettingBean
import com.example.camerautil_capture_recorder.databean.UtilBean

/**拍照回调
 * @param context //上下文
 * @param utilBeanForCapture //拍照bean
 * @param settingBeanForCapture //设置bean
 * */
class TheSurfaceViewCallBack(val context: Context, val utilBean: UtilBean, val settingBean: SettingBean) : SurfaceHolder.Callback {


    //图片监听
    private val avaiableListener: TheImageAvaiableListener =
        TheImageAvaiableListener(settingBean, utilBean)


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        utilBean.closeCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        holder!!.setFixedSize(settingBean.getWidth(), settingBean.getHeight())
        utilBean.getImageReader().setOnImageAvailableListener(avaiableListener, utilBean.getHandler())
        utilBean.initRecorder(1)
        utilBean.openCamera(context, 1)
    }
}
