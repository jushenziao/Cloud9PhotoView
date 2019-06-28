package com.aicc.cloud9.helper

interface ItemTouchHelperAdapter {

    open val start: Int
        get() = 0

    open val end: Int
        get() = 0

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}