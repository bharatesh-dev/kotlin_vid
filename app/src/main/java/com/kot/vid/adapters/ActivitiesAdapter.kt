package com.kot.vid.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.Activity
import com.kot.vid.R
import com.kot.vid.ui.loadImage

class ActivitiesAdapter : BaseAdapter<Activity, ActivitiesAdapter.ViewHolder>() {

    override fun getLayoutResourceId() = R.layout.adapter_activities

    override fun createViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : BaseViewHolder<Activity>(view) {
        var title: TextView
        var thumbnail: ImageView

        init {
            title = view.findViewById(R.id.title)
            thumbnail = view.findViewById(R.id.thumbnail)
        }

        override fun onBind(t: Activity, position: RecyclerView.ViewHolder) {
            val snippet = t.snippet
            title.text = snippet.title
            thumbnail.loadImage(snippet.thumbnails.high)
        }
    }
}