package com.example.theflowlayout;


//初始化设置信息
public class SettingBean {

    public SettingBean(){}
    /**设置模式
     * @param mode
     * @see MODE
     * */
    public SettingBean(MODE mode){
        this.mode = mode;
    }
    /**@param maxHeight //最大高
     * @param  maxWidth //最大宽
     * @param  mode //模式
     * */
    public SettingBean(MODE mode, int maxWidth, int maxHeight){
        this.mode = mode;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }
    /**@param maxHeight //最大高
     * @param  maxWidth //最大宽
     * @param  mode //模式
    *@param leftSpec//左间隔
     *@param rightSpec//右间隔
     *@param topSpec //上间隔
     *@param bottomSpec //低间隔
     * */
    public SettingBean(MODE mode, int maxWidth, int maxHeight, int leftSpec, int rightSpec, int topSpec, int bottomSpec){
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.mode = mode;
        this.bottomSpec = bottomSpec;
        this.leftSpec = leftSpec;
        this.rightSpec = rightSpec;
        this.topSpec = topSpec;
    }

    private int maxWidth = 0;//最大宽
    private int maxHeight = 0;//最大高
    private MODE mode = MODE.MODE_DP;//模式
    //margin
    private int leftSpec = 5;//左间隔
    private int rightSpec = 5;//右间隔
    private int topSpec = 5;//上间隔
    private int bottomSpec = 5;//低间隔

    public int getBottomSpec() { return bottomSpec; }
    public int getLeftSpec() { return leftSpec; }
    public int getRightSpec() { return rightSpec; }
    public int getTopSpec() { return topSpec; }
    public void setBottomSpec(int bottomSpec) { this.bottomSpec = bottomSpec; }
    public void setLeftSpec(int leftSpec) { this.leftSpec = leftSpec; }
    public void setRightSpec(int rightSpec) { this.rightSpec = rightSpec; }
    public void setTopSpec(int topSpec) { this.topSpec = topSpec; }
    public MODE getMode() { return mode; }
    public int getMaxHeight() { return maxHeight; }
    public int getMaxWidth() { return maxWidth; }
    public void setMaxHeight(int maxHeight) { this.maxHeight = maxHeight; }
    public void setMaxWidth(int maxWidth) { this.maxWidth = maxWidth; }

}
