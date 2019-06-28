package com.aicc.cloud9photoview.helper

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9photoview.Dispatcher
import com.aicc.cloud9photoview.util.ScreenUtils

class SimpleItemTouchHelperCallback(
    private val mContext: Context,
    private val mDispatcher: Dispatcher
) :
    ItemTouchHelper.Callback() {
    private var mOnDrawListener: OnDrawListener? = null

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = (ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val start = mDispatcher.start
        val end = mDispatcher.end
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        if (source.adapterPosition >= start && source.adapterPosition <= end
            && target.adapterPosition >= start && target.adapterPosition <= end
        ) {
            mDispatcher.onItemMove(source.adapterPosition, target.adapterPosition)
            return true
        }
        return false
    }


    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        } else {
            val listener: OnDrawListener? = mOnDrawListener;
            listener?.let {
                listener.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun getBoundingBoxMargin(): Int {
        return ScreenUtils.getScreenHeight(mContext)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        mDispatcher.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.alpha = ALPHA_FULL
        mDispatcher.clearView()
    }

    fun setOnDrawListener(onDrawListener: OnDrawListener) {
        mOnDrawListener = onDrawListener
    }

    fun removeOnDrawListener() {
        mOnDrawListener = null
    }

    interface OnDrawListener {
        fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        )
    }

    companion object {
        private val ALPHA_FULL = 1.0f
    }
}
