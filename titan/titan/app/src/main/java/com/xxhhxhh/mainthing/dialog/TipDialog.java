package com.xxhhxhh.mainthing.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

public class TipDialog extends AlertDialog.Builder
{
    public TipDialog(Context context, String tipText)
    {
        super(context);
        TextView textView = new TextView(context);
        textView.setText(tipText);
        textView.setTextSize(20);
        setView(textView);
        setPositiveButton("确定", null);
    }
}
