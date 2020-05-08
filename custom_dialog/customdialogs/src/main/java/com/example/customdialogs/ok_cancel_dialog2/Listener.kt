package com.example.customdialogs.ok_cancel_dialog2

import android.content.DialogInterface

abstract class Listener : DialogInterface.OnCancelListener,
    DialogInterface.OnDismissListener,
    ClickListener,
    DialogInterface.OnShowListener {}
