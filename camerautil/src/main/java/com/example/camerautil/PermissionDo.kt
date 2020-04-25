package com.example.camerautil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

//权限操作
object PermissionDo {
    //需求权限
    val permissions:Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO)

    //默认请求码
    private val REQUEST_CODE_DEFAULT:Int = 0x00

    //权限申请,6.0以上
    /**@param activity //接收返回的activity
     * @param permissions  //权限
     * @param requestCode //申请码，获取返回值时用到
     * */
    fun permissionRequest(activity: Activity, permissions:Array<String>, requestCode:Int){

        if(Build.VERSION_CODES.M <= Build.VERSION.SDK_INT){
            if(permissions.size > 0){
                activity.requestPermissions(permissions, requestCode)
            }else{
                activity.requestPermissions(this.permissions, requestCode)
            }
        }else{
            throw ErrorMessage.getSDKErrorMessage("CameraUtil.permissionCheck(activity:Activity, permissions:Array<String>, requestCode:Int)")
        }

    }

    //权限申请,6.0以上
    /**@param activity //接收返回的activity
     * @param permissions  //权限
     * @param requestCode //申请码，获取返回值时用到
     * */
    fun permissionRequestDefaultWithRequestCode(activity: Activity, requestCode:Int){

        if(Build.VERSION_CODES.M <= Build.VERSION.SDK_INT){

            activity.requestPermissions(this.permissions, requestCode)

        }else{
            throw ErrorMessage.getSDKErrorMessage("CameraUtil.permissionCheck(activity:Activity, permissions:Array<String>, requestCode:Int)")
        }

    }
    //权限申请,6.0以上
    /**@param activity //接收返回的activity
     * @param permissions  //权限
     * @param requestCode //申请码，获取返回值时用到
     * */
    fun permissionRequestDefault(activity: Activity){

        if(Build.VERSION_CODES.M <= Build.VERSION.SDK_INT){

            activity.requestPermissions(this.permissions, REQUEST_CODE_DEFAULT)

        }else{
            throw ErrorMessage.getSDKErrorMessage("CameraUtil.permissionCheck(activity:Activity, permissions:Array<String>, requestCode:Int)")
        }

    }

    /**权限检查
     * @param context //上下文
     * @param permission //权限
     * @return true检查成功，false失败
     * */
    fun permissionCheck(context: Context, permission:String):Boolean{
        if(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
            return  true
        }
        return false
    }

    /**多权限检查
     * @param context //上下文
     * @param permission //权限
     * @return true检查成功，false失败,只要一个失败就返回false
     * */
    fun permissionCheck(context: Context, permissions:Array<String>):Boolean{

        for (permission:String in permissions){
            if(!permissionCheck(context, permission)){
                return false
            }
        }
        return true
    }

}
