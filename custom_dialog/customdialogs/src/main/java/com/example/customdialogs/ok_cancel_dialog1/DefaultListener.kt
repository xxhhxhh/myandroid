package com.example.customdialogs.ok_cancel_dialog1

import android.content.DialogInterface
import android.view.KeyEvent

class DefaultListener : Listener() {

    override fun onCancel(dialog: DialogInterface?) {
       dialog!!.dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
        dialog!!.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {

    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
       dialog!!.dismiss()
        return true
    }

    override fun onShow(dialog: DialogInterface?) {

    }
}
