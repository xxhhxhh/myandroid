package com.example.theflowlayout.withkotlin

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.example.theflowlayout.MODE
import com.example.theflowlayout.SettingBean
import java.util.*

/**流式布局*/
class TheFlowLayout : ViewGroup {

    private var windowHeight:Int = 0//屏幕高度
    private var windowWidth:Int = 0//屏幕宽度
    private var maxHeight:Int = 0//最大高度
    private var maxWidth:Int = 0//最大宽度
    private var dpi:Int = 160 //屏幕密度
    private var settingBean: SettingBean = SettingBean()//设置信息
    private val views:LinkedList<View> = LinkedList()//view
    private val everyLineViewNumber:LinkedList<Int> = LinkedList()//每行个数
    private var lfspec:Int = 0
    private var tbspec:Int = 0
    /**测量模式
     * 0 默认
     * @see forDefault
     * 1 自定义，使用constructor(settingBean: SettingBean, context: Context):super(context)触发
     * 用于代码内创建
     * @see forCustom
     * 其它 使用super
     * */
    private var measureMode:Int = 0

    /**dp转px
     * @param dp
     * @return px
     * */
    fun transDpToPx(dp:Int):Int{
        return dp * (dpi / 160)
    }
    /**px转dp
     * @param px
     *@return dp
     * */
    fun transPxToDp(px:Int):Int{
        return dpi / (160 * px)
    }

    init {
        //屏幕信息初始化
        val windowManager:WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics:DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        dpi = displayMetrics.densityDpi
        windowHeight = displayMetrics.heightPixels
        windowWidth = displayMetrics.widthPixels
    }
    /** @param maxHeight //最大高度
    * @param maxWidth  //最大宽度
    * @param mode  ->
    * @see MODE
    * @param context //上下文
    * */
    constructor(settingBean: SettingBean, context: Context):super(context){
        this.settingBean = settingBean
        initSpec()
        when(settingBean.mode!!){
            MODE.MODE_DP->initMax(settingBean.maxWidth, settingBean.maxHeight)
            MODE.MODE_PX->initMax(transPxToDp(settingBean.maxWidth), transPxToDp(settingBean.maxHeight))
        }

    }

    //初始化最大宽高
    private fun initMax(width:Int, height:Int){
        if(width > 0 && height > 0){
            maxHeight = Math.min(height, windowHeight)
            maxWidth = Math.min(width, windowWidth)
            measureMode = 1
        }else{
            measureMode = 2
        }
    }

    constructor(context: Context):super(context){
        measureMode = 0
        initSpec()
    }
    constructor(context: Context, attributes: AttributeSet):super(context, attributes){
        measureMode = 0
        initSpec()
    }
    constructor(context: Context, attributes: AttributeSet, defStyleAttr:Int) :super(context, attributes, defStyleAttr){
        measureMode = 0
        initSpec()
    }
    constructor(context: Context, attributes: AttributeSet, defStyleAttr:Int, defStyleRes:Int):super(context, attributes, defStyleAttr,defStyleRes){
        measureMode = 0
        initSpec()
    }

    //初始化间隔
    private fun initSpec(){
        when(measureMode){
            0-> {
                lfspec = paddingLeft + paddingRight
                tbspec = paddingBottom + paddingTop
            }
            1,2->{
                lfspec = settingBean.leftSpec + settingBean.rightSpec
                tbspec = settingBean.topSpec + settingBean.bottomSpec
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when(measureMode){
            0->forDefault(widthMeasureSpec, heightMeasureSpec)
            1->forCustom(widthMeasureSpec, heightMeasureSpec)
            2->super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    //自定义
    private fun forCustom(widthMeasureSpec: Int, heightMeasureSpec: Int){
        var sizeWidth:Int = maxWidth
        var sizeHeight:Int = maxHeight
        var modeWidth:Int = MeasureSpec.getMode(widthMeasureSpec)
        var modeHeight:Int = MeasureSpec.getMode(heightMeasureSpec)
        toMeasure(widthMeasureSpec, heightMeasureSpec, sizeWidth, sizeHeight, modeWidth, modeHeight)
    }
    //默认
    private fun forDefault(widthMeasureSpec: Int, heightMeasureSpec: Int){

        var sizeWidth:Int = windowWidth
        var sizeHeight:Int = windowHeight
        var modeWidth:Int = MeasureSpec.getMode(widthMeasureSpec)
        var modeHeight:Int = MeasureSpec.getMode(heightMeasureSpec)
        //宽度选取
        if(modeWidth == MeasureSpec.EXACTLY){
           sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        //高度选取
        if(modeHeight == MeasureSpec.EXACTLY){
            sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        toMeasure(widthMeasureSpec, heightMeasureSpec, sizeWidth, sizeHeight, modeWidth, modeHeight)
    }
    //测量
    private fun toMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, sizeWidth:Int, sizeHeight:Int, modeWidth:Int, modeHeight:Int){
        views.clear()
        everyLineViewNumber.clear()
        val childCount:Int = childCount
        var nowLineWidth:Int = 0//当前行宽
        var maxLineWidth:Int = 0//最大行宽
        var maxLineHeight:Int = 0//最大行高
        var nowLineChild:Int = 0 //当前行child个数
        var nowLineNumber:Int = 0//当前行号,0代表第一行
        var nowHeight:Int = 0//当前行高
        for (i:Int in 0..childCount - 1){
            var childView:View = getChildAt(i)
            if(isOkClass(childView)){
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)//测量子view
                var childWidth:Int = childView.measuredWidth
                var childHeight:Int = childView.measuredHeight
                //终止
                if(childHeight + tbspec + maxLineHeight > sizeHeight){
                    break
                }else{
                    //换行
                    if(childWidth + lfspec + nowLineWidth> sizeWidth){
                        maxLineWidth = Math.max(maxLineWidth, nowLineWidth)
                        maxLineHeight += nowHeight
                        everyLineViewNumber.add(nowLineNumber, nowLineChild)
                        nowLineNumber++
                        views.add(childView)
                        nowLineChild = 1
                        nowLineWidth = childWidth + lfspec

                    }//最后一个
                    else if(i == childCount - 1){
                        maxLineWidth = Math.max(maxLineWidth, nowLineWidth)
                        nowLineChild++
                        views.add(childView)
                        everyLineViewNumber.add(nowLineNumber, nowLineChild)
                    } else{
                        maxLineWidth = Math.max(maxLineWidth, nowLineWidth)
                        nowLineWidth += childWidth + lfspec
                        views.add(childView)
                        nowLineChild++
                        nowHeight = Math.max(nowHeight, childHeight)
                    }
                }
            }
        }

        //设置测量值
        if(modeHeight == MeasureSpec.EXACTLY && modeWidth == MeasureSpec.EXACTLY){
            setMeasuredDimension(sizeWidth, sizeHeight)
        } else{
            setMeasuredDimension(maxLineWidth, maxLineHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var lineHeight:Int = 0//行高
        var number:Int = 0//view编号
        //所有行
        for (i:Int in 0..(everyLineViewNumber.size - 1)){
            var lineNumber:Int = everyLineViewNumber.get(i)
            var nowLineWidth:Int = 0
            var nowHeight:Int = 0
            //每一行
            for(j:Int in 0..lineNumber-1){
                var childView:View = views.get(number)
                var top:Int = settingBean.topSpec + lineHeight
                var bottom:Int = tbspec + childView.measuredHeight + lineHeight
                var left:Int = settingBean.leftSpec + nowLineWidth
                var right:Int = left + settingBean.rightSpec + childView.measuredWidth
                nowLineWidth = right//确定宽度
                nowHeight = Math.max(bottom, nowHeight)//确定行高
                childView.layout(left, top, right, bottom)
                number++
            }
            lineHeight = nowHeight//设置下一行起始行高
        }
    }

    //类型判定，只挑选符合TextView及其子类的view
    /**@param class1 //要检测的视图
     * @return true 允许布局 false 忽略布局
     * */
    private fun isOkClass(class1:View):Boolean{
        return TextView::class.java.isAssignableFrom(class1.javaClass)
    }
}
