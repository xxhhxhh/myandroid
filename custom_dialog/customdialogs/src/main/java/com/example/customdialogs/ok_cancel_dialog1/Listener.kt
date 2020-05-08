package com.example.customdialogs.ok_cancel_dialog1

import android.content.DialogInterface

abstract class Listener : DialogInterface.OnCancelListener,
    DialogInterface.OnClickListener,
    DialogInterface.OnDismissListener,
    DialogInterface.OnKeyListener,
    DialogInterface.OnMultiChoiceClickListener,
    DialogInterface.OnShowListener {}
