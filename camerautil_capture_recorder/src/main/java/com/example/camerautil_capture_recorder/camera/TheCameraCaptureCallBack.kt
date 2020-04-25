package com.example.camerautil_capture_recorder.camera

import android.hardware.camera2.CameraCaptureSession
import com.example.camerautil_capture_recorder.databean.UtilBean

class TheCameraCaptureCallBack(val utilBean: UtilBean):CameraCaptureSession.StateCallback(){


    override fun onConfigureFailed(session: CameraCaptureSession) {
        session.close()
    }

    override fun onConfigured(session: CameraCaptureSession) {
        utilBean.setCameraCaptureSession(session)
        if(utilBean.getCameraCaptureSession() != null){

            utilBean.getCameraCaptureSession().setRepeatingRequest(utilBean.getPreviewRequest().build(), null, utilBean.getHandler())
        }
    }

}
