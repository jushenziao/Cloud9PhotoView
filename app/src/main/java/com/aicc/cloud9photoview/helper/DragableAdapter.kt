package com.aicc.cloud9photoview.helper

import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9photoview.DragableViewHolder

abstract class DragableAdapter : RecyclerView.Adapter<DragableViewHolder>(),
    ItemTouchHelperAdapter {

    open val start: Int
        get() = 0

    open val end: Int
        get() = 0
}