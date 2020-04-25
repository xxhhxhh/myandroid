package com.example.camerautil_capture_recorder.camera

import android.hardware.camera2.*

//拍照后监听
abstract class RunAfterListener : CameraCaptureSession.CaptureCallback() {

    abstract override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult)
    abstract override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure)
    abstract override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult)
}
