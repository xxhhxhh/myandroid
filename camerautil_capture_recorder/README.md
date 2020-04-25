# 本示例用于同时支持拍照及录像
## 主要功能
### 拍照及录像预览
&nbsp;&nbsp;支持surfaceview(SDK>=29)和textureview
### 拍照
&nbsp;&nbsp;通过设置UtilBean中的acquireType，确定拍照后文件获取方式
### 录像
&nbsp;&nbsp;可调用控制方法
CameraUtil.startRecorder()//开始录像<br>
CameraUtil.stopRecorder()//停止录像<br>
CameraUtil.release()//释放录像<br>
CameraUtil.resetRecorder()//重置录像资源（将会保存内容到最终资源文件（resultFile）中）
### 摄像头切换
&nbsp;&nbsp;默认前置和后置切换.
## 注意
运行要求SDK>= 21
基于kotlin开发
gradle依赖:implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
