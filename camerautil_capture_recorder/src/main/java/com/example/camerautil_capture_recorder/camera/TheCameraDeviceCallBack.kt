package com.example.camerautil_capture_recorder.camera

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.MediaRecorder
import android.view.Surface
import com.example.camerautil_capture_recorder.databean.SettingBean
import com.example.camerautil_capture_recorder.databean.UtilBean

class TheCameraDeviceCallBack(val utilBean: UtilBean, val settingBean: SettingBean, val  type:Int) : CameraDevice.StateCallback() {

    var surfaces:ArrayList<Surface> = ArrayList()

    override fun onOpened(camera: CameraDevice) {

        when(type){
            0->addSurfaceForTextureView()
            1->addSurfaceForSurfaceView()
        }


        utilBean.setCameraDevice(camera)
//        var surface:Surface = Surface(utilBean.getTextureView().surfaceTexture)
        var captureRequest:CaptureRequest.Builder = utilBean.getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        for(surface:Surface in surfaces){
            captureRequest.addTarget(surface)
        }
        utilBean.setPreviewRequest(captureRequest)
        var captureSessionCallBack: TheCameraCaptureCallBack =
            TheCameraCaptureCallBack(utilBean)
        camera.createCaptureSession(surfaces, captureSessionCallBack, utilBean.getHandler())

    }

    override fun onDisconnected(camera: CameraDevice) {
        camera.close()
        utilBean.setCameraDevice(null)
    }

    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        utilBean.setCameraDevice(null)
    }

    //surafce添加
    fun addSurfaceForSurfaceView(){
        if(settingBean.getVideoSource() == MediaRecorder.VideoSource.SURFACE){
            surfaces.add(utilBean.getMediaRecorder().surface)
            surfaces.add(utilBean.getSurfaceView().holder.surface)
            surfaces.add(utilBean.getImageReader().surface)
        }else{
            surfaces.clear()
            surfaces.add(utilBean.getSurfaceView().holder.surface)
            surfaces.add(utilBean.getImageReader().surface)
        }
    }
    fun addSurfaceForTextureView(){
        val surface:Surface = Surface(utilBean.getTextureView().surfaceTexture)
        if(settingBean.getVideoSource() == MediaRecorder.VideoSource.SURFACE){
            surfaces.add(utilBean.getMediaRecorder().surface)
            surfaces.add(surface)
            surfaces.add(utilBean.getImageReader().surface)
        }else{
            surfaces.clear()
            surfaces.add(surface)
            surfaces.add(utilBean.getImageReader().surface)
        }
    }

}
