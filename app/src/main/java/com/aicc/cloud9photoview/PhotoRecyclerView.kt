package com.aicc.cloud9photoview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9photoview.helper.SimpleItemTouchHelperCallback
import com.aicc.cloud9photoview.util.ScreenUtils

class PhotoRecyclerView : RecyclerView{

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}
}
