package com.example.camerautil.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import com.example.camerautil.CameraUtil
import com.example.camerautil.PermissionDo
import com.example.camerautil.R
import com.example.camerautil.databean.SettingBeanForCapture

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.for_textureview)
        //权限申请
        PermissionDo.permissionRequestDefault(this)
        var textureView:TextureView = findViewById(R.id.text1)//textureview
//        var surfaceView:SurfaceView = findViewById(R.id.text1)//surfaceview
        var settingBeanForCapture:SettingBeanForCapture = SettingBeanForCapture()
        settingBeanForCapture.setImageFile(filesDir.toString() + "/camera.jpeg")//必设置项，文件存储地址
        var cameraUtil:CameraUtil = CameraUtil(this, textureView, settingBeanForCapture)//textureview
//        var cameraUtil:CameraUtil = CameraUtil(this, surfaceview, settingBeanForCapture)//surfaceview
        var isStart:Boolean = false
        findViewById<Button>(R.id.button1).setOnClickListener {
            //拍照
            cameraUtil.takePicture()
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            //切换摄像头
            cameraUtil.switchCamera()
        }
    }
}
