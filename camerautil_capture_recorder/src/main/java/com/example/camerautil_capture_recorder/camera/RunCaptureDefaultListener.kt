package com.example.camerautil_capture_recorder.camera

import android.hardware.camera2.*
import com.example.camerautil_capture_recorder.databean.UtilBean

//默认回调
class RunCaptureDefaultListener : RunAfterListener {

    private lateinit var utilBean: UtilBean

    //默认
    constructor(utilBeanForCapture: UtilBean){
        this.utilBean = utilBeanForCapture
    }

    //继承用
    constructor(){}

    override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
        if (utilBean.getPreviewRequest() != null){
            session.setRepeatingRequest(utilBean.getPreviewRequest().build(), null, utilBean.getHandler())
        }
    }

    override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {

    }

    override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
    }


}
