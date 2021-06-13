package com.sagarmishra.futsal.utils

import android.content.Context
import android.util.DisplayMetrics

class Utility() {
    fun calculateColumns(context: Context,colWidth:Int):Int
    {
        var displayMetrics:DisplayMetrics = context.resources.displayMetrics
        var screenWidthDp:Float = displayMetrics.widthPixels / displayMetrics.density
        var column = (screenWidthDp/colWidth +0.5).toInt()
        return column
    }
}