package com.kot.vid.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.Subscription
import com.kot.vid.R

class SubscriptionView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    init {
        inflate(context, R.layout.subscription_item, null)
    }

    constructor(context: Context?, attrs: AttributeSet?, style: Int) : this(context, attrs) {

    }

    fun bindData(item: Subscription) {
        val title = findViewById<TextView>(R.id.title)
        val newCount = findViewById<TextView>(R.id.new_count)
        val thumbnail = findViewById<ImageView>(R.id.thumbnail)

        title.text = item.snippet.title
        item.contentDetails.newItemCount?.let {
            if (item.contentDetails.newItemCount > 0) {
                newCount.text = item.contentDetails.newItemCount.toString()
            }
        }
        thumbnail.loadImageSmall(item.snippet.thumbnails.default.url, 80, 60)
    }
}