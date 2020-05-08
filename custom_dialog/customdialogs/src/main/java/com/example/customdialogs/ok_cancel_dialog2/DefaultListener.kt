package com.example.customdialogs.ok_cancel_dialog2

import android.content.DialogInterface
import android.view.KeyEvent

class DefaultListener : Listener() {

    override fun onCancel(dialog: DialogInterface?) {
        dialog!!.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {

    }

    override fun onPostiveClick(dialog: OkCancelDialog) {
        dialog.dismiss()
    }

    override fun onNegativeClick(dialog: OkCancelDialog) {
        dialog.dismiss()
    }

    override fun onShow(dialog: DialogInterface?) {

    }

}
