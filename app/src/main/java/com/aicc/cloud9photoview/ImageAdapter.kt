package com.aicc.cloud9photoview

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9.Dispatcher
import com.aicc.cloud9.DragableViewHolder
import com.aicc.cloud9.util.Constants
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine

class ImageAdapter(var context: Context, val dispatcher: Dispatcher<Uri>, var list: List<Uri>?) :
    RecyclerView.Adapter<DragableViewHolder>() {
    var mInflater: LayoutInflater

    override fun getItemCount(): Int {
        var total = list?.size ?: 0
        if (total < Constants.MAX_PHOTO_NUMS) {
            total = total + 1
        }
        return total
    }

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DragableViewHolder {
        val view = mInflater.inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DragableViewHolder, position: Int) {
        if (holder !is ViewHolder || list == null) {
            return
        }
        if (position == list!!.size && list!!.size < Constants.MAX_PHOTO_NUMS) {
            holder.imageView.setImageResource(R.drawable.ic_plus)
            holder.itemView.setOnClickListener { v -> openPhotoSelector() }
        } else {
            Glide.with(context)
                .load(list!![position])
                .into(holder.imageView)

            holder.itemView.setOnLongClickListener { view ->
                if (holder.adapterPosition == list!!.size && list!!.size < Constants.MAX_PHOTO_NUMS) {
                    return@setOnLongClickListener true
                }
                dispatcher.onStartDrag(holder)
                false
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun openPhotoSelector() {
        if (context is Activity) {
            val activity: Activity = context as Activity;
            Matisse.from(activity)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(MainActivity.REQUEST_CODE_CHOOSE)
        }

    }

    internal inner class ViewHolder(itemView: View) : DragableViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.image)
        }
    }
}