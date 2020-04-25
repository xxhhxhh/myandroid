package com.example.camerautil.camera

import android.hardware.camera2.CameraCaptureSession
import com.example.camerautil.databean.UtilBeanForCapture

class TheCameraCaptureCallBack(val utilBeanForCapture: UtilBeanForCapture):CameraCaptureSession.StateCallback(){


    override fun onConfigureFailed(session: CameraCaptureSession) {
        session.close()
    }

    override fun onConfigured(session: CameraCaptureSession) {
        utilBeanForCapture.setCameraCaptureSession(session)
        if(utilBeanForCapture.getCameraCaptureSession() != null){

            utilBeanForCapture.getCameraCaptureSession().setRepeatingRequest(utilBeanForCapture.getPreviewRequest().build(), null, utilBeanForCapture.getHandler())
        }
    }

}
