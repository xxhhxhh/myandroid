package com.example.camerautil_capture_recorder.camera

import android.hardware.camera2.*
import com.example.camerautil_capture_recorder.databean.UtilBean

//拍照回调
class TheCaptureCallBack(val utilBean: UtilBean, val runAfterListener: RunAfterListener?) :
    CameraCaptureSession.CaptureCallback() {

    val runCaptureCaptureDefault: RunCaptureDefaultListener =
        RunCaptureDefaultListener(utilBean)

    override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
        super.onCaptureCompleted(session, request, result)
        if(runAfterListener != null){
            runAfterListener!!.onCaptureCompleted(session, request, result)
        }else{
            runCaptureCaptureDefault.onCaptureCompleted(session, request, result)
        }
    }

    override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
        super.onCaptureFailed(session, request, failure)
        if(runAfterListener != null){
            runAfterListener!!.onCaptureFailed(session, request, failure)
        }else{
            runCaptureCaptureDefault.onCaptureFailed(session, request, failure)
        }

    }

    override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
        super.onCaptureProgressed(session, request, partialResult)
        if(runAfterListener != null){
            runAfterListener!!.onCaptureProgressed(session, request, partialResult)
        }else{
            runCaptureCaptureDefault.onCaptureProgressed(session, request, partialResult)
        }
    }
}
