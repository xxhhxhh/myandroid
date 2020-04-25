package com.example.camerautil.camera

import android.hardware.camera2.*
import com.example.camerautil.databean.UtilBeanForCapture

//默认回调
class RunCaptureDefaultListener : RunAfterListener {

    private lateinit var utilBeanForCapture: UtilBeanForCapture

    //默认
    constructor(utilBeanForCapture: UtilBeanForCapture){
        this.utilBeanForCapture = utilBeanForCapture
    }

    //继承用
    constructor(){}

    override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
        if (utilBeanForCapture.getPreviewRequest() != null){
            session.setRepeatingRequest(utilBeanForCapture.getPreviewRequest().build(), null, utilBeanForCapture.getHandler())
        }
    }

    override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {

    }

    override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
    }


}
