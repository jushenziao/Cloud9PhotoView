package com.aicc.cloud9

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9.helper.ItemTouchHelperAdapter
import com.aicc.cloud9.helper.SimpleItemTouchHelperCallback

class Dispatcher<T> : ItemTouchHelperAdapter {

    private lateinit var photoView: RecyclerView;
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mParentView: ParentView
    private lateinit var mContext: Context
    private lateinit var mTouchCallBack: SimpleItemTouchHelperCallback<T>
    private lateinit var mPosProvider: IPosProvider
    private var inited: Boolean = false
    private var mRoot: ViewGroup? = null

    private var mSelectedPhotos: java.util.ArrayList<T>? = java.util.ArrayList()

    /**
     * 支持两种创建方式 1，外部传入ParentView 2 外部传入rootView 内部创建parentView添加到rootView里
     */
    @MainThread
    fun onCreate(
        rootContainer: ViewGroup, ctx: Context, photoRecyclerView: RecyclerView,
        list: java.util.ArrayList<T>, posProvider: IPosProvider
    ) {
        checkIsInit()
        inited = true
        mRoot = rootContainer
        mContext = ctx
        mTouchCallBack = SimpleItemTouchHelperCallback(ctx, this)
        photoView = photoRecyclerView
        mItemTouchHelper = ItemTouchHelper(mTouchCallBack)
        mItemTouchHelper.attachToRecyclerView(photoRecyclerView)
        mSelectedPhotos = list
        mPosProvider = posProvider
    }

    @MainThread
    fun onCreate(
        parentView: ParentView, ctx: Context, photoRecyclerView: RecyclerView,
        list: java.util.ArrayList<T>, posProvider: IPosProvider
    ) {
        checkIsInit()
        inited = true
        mParentView = parentView
        mContext = ctx
        mTouchCallBack = SimpleItemTouchHelperCallback(ctx, this)
        photoView = photoRecyclerView
        mItemTouchHelper = ItemTouchHelper(mTouchCallBack)
        mItemTouchHelper.attachToRecyclerView(photoRecyclerView)
        mSelectedPhotos = list
        mPosProvider = posProvider
    }

    private fun checkIsInit() {
        if (inited) {
            throw IllegalArgumentException("***********Cloud9 Dispatcher has been inited...**********")
        }
    }

    fun onStartDrag(viewHolder: DragableViewHolder) {
        //开始拖拽时 找到Activity的contentView
        mRoot?.let {
            mParentView = ParentView(mContext)
            val layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            mParentView.layoutParams = layoutParams
            mRoot!!.addView(mParentView)
        }

        mTouchCallBack.setOnDrawListener(mParentView)
        mParentView.onStartDrag(viewHolder)
        mItemTouchHelper.startDrag(viewHolder)
    }

    fun innerItemMove(fromPosition: Int, toPosition: Int) {
        val photo = mSelectedPhotos?.removeAt(fromPosition)
        photo?.let { mSelectedPhotos?.add(toPosition, it) }
        photoView.adapter?.notifyItemMoved(fromPosition, toPosition)
    }

    fun clearView() {
        mParentView.clear()
        mRoot?.removeView(mParentView)
        mTouchCallBack.removeOnDrawListener()
        photoView.adapter?.notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition == toPosition || mPosProvider.canItemMove(fromPosition, toPosition)) {
            return false
        }
        innerItemMove(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
    }


    override fun start(): Int {
        return mPosProvider.start()
    }

    override fun end(): Int {
        return mPosProvider.end()
    }

    fun onDestroy() {

    }
}