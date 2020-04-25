# 本示例用于拍照功能展示
&nbsp;&nbsp;基于kotlin开发,使用Camera2API
## 基本功能
### 拍照预览
&nbsp;&nbsp;包含TextureView和SurfaceView(SDK>=29)
### 摄像头切换
&nbsp;&nbsp;默认为前后置摄像头切换，同时可通过switchCamera(cameraId)自由切换
### 拍摄
&nbsp;&nbsp;设置type属性控制拍摄监听imageReader内容选取
## 基本使用示例请查看com.example.camerautil.test.MainActivity
## 适用：SDK>=21 && SDK <= $最新版本$
## build.gradle依赖:implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
