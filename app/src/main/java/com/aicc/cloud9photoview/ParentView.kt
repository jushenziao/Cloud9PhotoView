package com.aicc.cloud9photoview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9photoview.helper.SimpleItemTouchHelperCallback
import com.aicc.cloud9photoview.util.ScreenUtils

class ParentView : RelativeLayout, SimpleItemTouchHelperCallback.OnDrawListener {

    private var mDragingBitmap: Bitmap? = null;

    private val mDstRect = Rect()
    val ints = IntArray(2)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mDragingBitmap != null && !mDragingBitmap!!.isRecycled()) {
            canvas.drawBitmap(mDragingBitmap, null, mDstRect, null)
        }
    }

    fun onStartDrag(viewHolder: DragableViewHolder) {
        if (mDragingBitmap != null && !mDragingBitmap!!.isRecycled()) {
            mDragingBitmap!!.recycle()
        }
        mDragingBitmap = ScreenUtils.getDrawingCacheWithColorBg(viewHolder.itemView, Color.TRANSPARENT)
    }

    fun drawBitmap(bitmap: Bitmap, dX: Float, dY: Float, xOffset: Float, yOffset: Float) {
        mDragingBitmap = bitmap
        val scaleWidth = bitmap.width.toFloat() * 0.1f * 0.5f
        mDstRect.set(
            (dX + xOffset - scaleWidth).toInt(),
            (dY + yOffset - scaleWidth).toInt(),
            (dX + xOffset + mDragingBitmap!!.getWidth().toFloat() + scaleWidth).toInt(),
            (dY + yOffset + mDragingBitmap!!.getHeight().toFloat() + scaleWidth).toInt()
        )
        invalidate()
    }

    fun clear() {
        mDstRect.setEmpty()
        mDragingBitmap?.let {
            if (!mDragingBitmap!!.isRecycled()) {
                mDragingBitmap!!.recycle()
            }
            mDragingBitmap = null
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // 看似直接取viewHolder.itemView.getX即可
        // 但在ItemtouchHelper中getSelectedDxDy可见dX不恒等于translationX 因此需要手动计算
        val itemViewX = viewHolder.itemView.left + dX
        val itemViewY = viewHolder.itemView.top + dY
        recyclerView.getLocationInWindow(ints)
        //如果只在 RecyclerView内部绘制达不到微博朋友圈那种效果 将Bitmap和位置消息交给父View绘制
        mDragingBitmap?.let {
            drawBitmap(
                it, itemViewX, itemViewY,
                recyclerView.x, recyclerView.y
            )
        }
    }
}
