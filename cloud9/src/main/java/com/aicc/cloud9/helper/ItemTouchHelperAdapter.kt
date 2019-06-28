package com.aicc.cloud9.helper

interface ItemTouchHelperAdapter {

    fun start(): Int

    fun end(): Int

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}