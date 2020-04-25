package com.example.camerautil.camera

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import com.example.camerautil.databean.UtilBeanForCapture

class TheCameraDeviceCallBack(val utilBeanForCapture: UtilBeanForCapture) : CameraDevice.StateCallback() {



    override fun onOpened(camera: CameraDevice) {
        utilBeanForCapture.setCameraDevice(camera)
//        var surface:Surface = Surface(utilBean.getTextureView().surfaceTexture)
        var captureRequest:CaptureRequest.Builder = utilBeanForCapture.getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequest.addTarget(utilBeanForCapture.getSurface())
        utilBeanForCapture.setPreviewRequest(captureRequest)
        var captureSessionCallBack: TheCameraCaptureCallBack =
            TheCameraCaptureCallBack(utilBeanForCapture)
        utilBeanForCapture.getCameraDevice().createCaptureSession(arrayListOf(utilBeanForCapture.getSurface(), utilBeanForCapture.getImageReader().surface), captureSessionCallBack, utilBeanForCapture.getHandler())

    }

    override fun onDisconnected(camera: CameraDevice) {
        camera.close()
        utilBeanForCapture.setCameraDevice(null)
    }

    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        utilBeanForCapture.setCameraDevice(null)
    }
}
