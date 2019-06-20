package com.aicc.cloud9photoview.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

object ScreenUtils {

    fun getScreenHeight(ctx:Context):Int{
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }

    fun getDrawingCacheWithColorBg(contentView: View, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            contentView.width,
            contentView.height, Bitmap.Config.ARGB_8888
        )
        bitmap.eraseColor(color)
        val canvas = Canvas(bitmap)
        contentView.draw(canvas)
        return bitmap
    }
}