package com.example.customdialogs.ok_cancel_dialog1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View

/**确定及取消对话框
 * */
class OkCancelDialog : AlertDialog{

    //监听
    public var listener:Listener? = null
    private var postiveButtonText:String = "确定"
    private var negativeButtonText:String = "取消"
    private var nowView:View? = null

    companion object{
        /**按钮规格
         * 1  确定按钮
         * 2  取消按钮
         * 3  两个按钮
         * */
        public val POSTIVE_BUTTON = 1
        public val NEGATIVE_BUTTON = 2
        public val BOTH_BUTTON = 3
    }

    /**@param cancelAble //是否可以取消
     * @param context //上下文
     * @param listener //监听
     * @param buttons
     * @see POSTIVE_BUTTON
     * @see NEGATIVE_BUTTON
     * @see BOTH_BUTTON
     * @param view 视图
     * */
    constructor(context: Context, listener: Listener?, buttons:Int, view:View?):super(context, true, listener){
        init( buttons, view)
    }
    /**@param cancelAble //是否可以取消
     * @param context //上下文
     * @param listener //监听
     * @param buttons
     * @see POSTIVE_BUTTON
     * @see NEGATIVE_BUTTON
     * @see BOTH_BUTTON
     * @param resourceId 视图id
     * */
    constructor(context: Context,listener: Listener?, buttons:Int, resourceId:Int):super(context, true, listener){
        init( buttons, LayoutInflater.from(context).inflate(resourceId, null))
    }
    /**@param cancelAble //是否可以取消
     * @param context //上下文
     * @param listener //监听
     * @param view //视图
     * */
    constructor(context: Context, listener: Listener?, view:View?):super(context, true, listener){
        init( 2, view)
    }
    /**@param cancelAble //是否可以取消
     * @param context //上下文
     * @param listener //监听
     * @param resourceId //视图id
     * */
    constructor(context: Context,listener: Listener?, resourceId: Int):super(context, true, listener){
        init( 2, LayoutInflater.from(context).inflate(resourceId, null))
    }
    private fun init( buttons: Int, view: View?){

        //是否可以取消
        if (listener != null){
            this.listener = listener
        }else{
            this.listener = DefaultListener()
        }
        setOnCancelListener(listener)
        setOnDismissListener(listener)
        setOnKeyListener(listener)
        setOnShowListener(listener)
        //按钮设置
        when(buttons){
            1->{
                setButton(DialogInterface.BUTTON_POSITIVE, postiveButtonText, this.listener)
            }
            2->{
                setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonText, this.listener)
            }
            3->{
                setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonText, this.listener)
                setButton(DialogInterface.BUTTON_POSITIVE, postiveButtonText, this.listener)
            }
        }

        if(view != null){
            setView(view)
            nowView = view
        }
    }


    //得到view
    public fun getView():View?{
        return this.nowView
    }
}
