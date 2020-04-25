package com.example.camerautil

//错误信息表
object ErrorMessage {

    //图片获取错误信息
    private  val  getImageError:String = "无法有效存储图片->TheImageAvaiableListener.getImage(image:Image)"
    fun getImageErrorMessage():Throwable{return Throwable(getImageError) }

    //文件错误
    private val getFileError:String = "文件存储错误,未正确设置文件"
    fun getFileErrorMessage():Throwable{return Throwable(getFileError) }

    //sdk版本错误
    private val getSDKError:String = "SDK版本错误->"
    fun getSDKErrorMessage(message:String):Throwable{return Throwable(getSDKError + message)}

    //权限错误
    private val getPermissionError:String = "权限检查错误,存在权限未获取"
    fun getPermissionErrorMessage():Throwable{return Throwable(getPermissionError)
    }

    //打开方式错误
    private val getOpenTypeError:String = "打开方式，未正确设置openType"
    fun getOpenTypeErrorMessage():Throwable{return Throwable(getOpenTypeError)}
}
