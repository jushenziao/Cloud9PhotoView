package com.aicc.cloud9photoview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aicc.cloud9.Dispatcher
import com.aicc.cloud9.ParentView
import com.aicc.cloud9.util.Constants
import com.zhihu.matisse.Matisse
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_CHOOSE = 100;
    }

    lateinit var mSelected: List<Uri>
    lateinit var mDispatcher: Dispatcher<Uri>;
    val mSelectedPhotos: ArrayList<Uri> = ArrayList()
    private lateinit var mAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
        )
        setContentView(R.layout.activity_main)
        mDispatcher = Dispatcher<Uri>();
        var content: ViewGroup = findViewById(android.R.id.content)
        val photoRecyclerView = findViewById<RecyclerView>(R.id.photo_view)
        val parentView = findViewById<ParentView>(R.id.parent)
        mAdapter = ImageAdapter(this, mDispatcher, mSelectedPhotos)
        val layoutManage = GridLayoutManager(this, Constants.MAX_PHOTO_COLUMNS)
        photoRecyclerView.setLayoutManager(layoutManage)
        photoRecyclerView.setAdapter(mAdapter)
        mDispatcher.onCreate(parentView, this, photoRecyclerView, mSelectedPhotos)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDispatcher.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            mSelected = Matisse.obtainResult(data!!)
            Log.d(TAG, "mSelected: $mSelected")
            onPhotoResult(mSelected)
        }
    }

    fun onPhotoResult(uris: List<Uri>) {
        mSelectedPhotos?.clear()
        mSelectedPhotos?.addAll(uris)
        mAdapter.notifyDataSetChanged()
    }
}
