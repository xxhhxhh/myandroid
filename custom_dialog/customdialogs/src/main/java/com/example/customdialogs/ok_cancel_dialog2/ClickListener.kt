package com.example.customdialogs.ok_cancel_dialog2

interface ClickListener {

    //确定按钮单击
    fun onPostiveClick(dialog: OkCancelDialog);
    //取消按钮单击
    fun onNegativeClick(dialog: OkCancelDialog);
}
