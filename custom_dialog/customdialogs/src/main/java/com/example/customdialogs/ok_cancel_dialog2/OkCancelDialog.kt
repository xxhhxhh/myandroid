package com.example.customdialogs.ok_cancel_dialog2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.customdialogs.R

class OkCancelDialog : AlertDialog, View.OnClickListener {

    //监听
    public var listener: Listener? = null
    private var nowView: View? = null

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
     * @param resourceId 视图id
     * */
    constructor(context: Context, listener: Listener?, buttons:Int, text:String):super(context, true, listener){
        init(listener,buttons, text)
    }
    /**@param cancelAble //是否可以取消
     * @param context //上下文
     * @param listener //监听
     * @param view //视图
     * */
    constructor(context: Context, listener: Listener?, text: String):super(context, true, listener){
        init( listener,2, text)
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
    constructor(context: Context,  listener: Listener?, buttons:Int, resourceId:Int):super(context, true, listener){
        init(listener, buttons, context.resources.getString(resourceId))
    }
    /**
     * @param context //上下文
     * @param listener //监听
     * @param resourceId //视图id
     * */
    constructor(context: Context, listener: Listener?, resourceId: Int):super(context, true, listener){
        init(listener,2, context.resources.getString(resourceId))
    }
    private fun init( listener: Listener?,buttons: Int, text: String){

        //是否可以取消
        if (listener != null){
            this.listener = listener
        }else {
            this.listener = DefaultListener()
        }
        setOnCancelListener(listener)
        setOnDismissListener(listener)
        setOnShowListener(listener)
        var view:View = LayoutInflater.from(context).inflate(R.layout.ok_cancel_dialog, null)
        view.findViewById<TextView>(R.id.text).setText(text)
        setView(view)
        nowView = view
        //按钮设置
        if(this.listener != null){
            when(buttons){
                1->{
                    view.findViewById<TextView>(R.id.ok).setOnClickListener(this)
                    view.findViewById<TextView>(R.id.cancel).visibility = View.INVISIBLE
                }
                2->{
                    view.findViewById<TextView>(R.id.cancel).setOnClickListener(this)
                    view.findViewById<TextView>(R.id.ok).visibility = View.INVISIBLE
                }
                3->{
                    view.findViewById<TextView>(R.id.ok).setOnClickListener(this)
                    view.findViewById<TextView>(R.id.cancel).setOnClickListener(this)
                }
            }
        }

    }


    //得到view
    public fun getView(): View?{
        return this.nowView
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ok->listener!!.onPostiveClick(this)
            R.id.cancel->listener!!.onNegativeClick(this)
        }
    }

}
