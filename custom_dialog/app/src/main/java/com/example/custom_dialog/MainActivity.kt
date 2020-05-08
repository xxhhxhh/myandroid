package com.example.custom_dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.customdialogs.ok_cancel_dialog2.OkCancelDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var view: View = LayoutInflater.from(this).inflate(R.layout.dialog1, null)
        var okCancelDialog:OkCancelDialog = OkCancelDialog(this, null, OkCancelDialog.BOTH_BUTTON, "view")
        okCancelDialog.dismiss()
        findViewById<Button>(R.id.onclick).setOnClickListener {
            okCancelDialog.show()
        }
    }
}
