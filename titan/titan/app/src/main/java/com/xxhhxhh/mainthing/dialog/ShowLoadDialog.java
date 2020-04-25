package com.xxhhxhh.mainthing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.example.titan.R;

public class ShowLoadDialog extends AlertDialog.Builder
{

    public ShowLoadDialog(Context context)
    {
        super(context);
        LinearLayout one = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.yanzheng_dialog, null);
        setView(one);
    }
}
