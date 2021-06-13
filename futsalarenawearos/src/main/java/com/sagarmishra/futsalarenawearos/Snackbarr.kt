package com.sagarmishra.futsalarenawearos

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message:String)
{
    var snk = Snackbar.make(this,message, Snackbar.LENGTH_LONG)
    snk.setAction("OK", View.OnClickListener {
        snk.dismiss()
    })
    snk.show()
}