package com.aicc.cloud9photoview

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.aicc.cloud9photoview.helper.SimpleItemTouchHelperCallback
import com.aicc.cloud9photoview.util.Constants
import java.util.*

class Dispatcher() {

    private val mSelectedPhotos: ArrayList<Uri>? = ArrayList()
    private lateinit var photoView: PhotoRecyclerView;
    private lateinit var mAdapter: ImageAdapter;
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mParentView: ParentView

    fun onCreate(ctx: Context, photoRecyclerView: PhotoRecyclerView, parentView: ParentView) {
        mAdapter = ImageAdapter(ctx, this, mSelectedPhotos)
        val layoutManage = GridLayoutManager(ctx, Constants.MAX_PHOTO_COLUMNS)
        photoRecyclerView.setLayoutManager(layoutManage)
        photoRecyclerView.setAdapter(mAdapter)
        val callback = SimpleItemTouchHelperCallback(ctx, this, mAdapter)
        callback.setOnDrawListener(parentView)
        mParentView = parentView
        photoView = photoRecyclerView
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(photoRecyclerView)
    }

    fun onStartDrag(viewHolder: DragableViewHolder) {
        mParentView.onStartDrag(viewHolder)
        mItemTouchHelper.startDrag(viewHolder)
    }

    fun onItemMove(imageAdapter: ImageAdapter, fromPosition: Int, toPosition: Int) {
        val photo = mSelectedPhotos!!.removeAt(fromPosition)
        mSelectedPhotos.add(toPosition, photo)
        imageAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    fun clearView() {
        mParentView.clear()
        photoView.adapter?.notifyDataSetChanged()
    }

    fun onPhotoResult(uris: List<Uri>) {
        mSelectedPhotos?.clear()
        mSelectedPhotos?.addAll(uris)
        mAdapter.notifyDataSetChanged()
    }

    fun onDestroy() {

    }
}