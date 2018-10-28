package com.kot.vid.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.api.services.youtube.model.Subscription
import com.kot.vid.R
import com.kot.vid.ui.loadImageSmall
import com.kot.vid.utils.CommonUtils

class SubscriptionAdapter(list: MutableList<Subscription>) : BaseAdapter<Subscription, BaseViewHolder<Subscription>>() {

    init {
        addItems(list)
    }

    override fun getLayoutResourceId() = R.layout.subscription_item

    override fun createViewHolder(v: View): BaseViewHolder<Subscription> {
        return ViewHolder(v)
    }

    class ViewHolder(v: View) : BaseViewHolder<Subscription>(v) {
        val title = v.findViewById<TextView>(R.id.title)
        val newCount = v.findViewById<TextView>(R.id.new_count)
        val thumbnail = v.findViewById<ImageView>(R.id.thumbnail)

        override fun onBind(item: Subscription, position: RecyclerView.ViewHolder) {
            title.text = item.snippet.title
            item.contentDetails.newItemCount?.let {
                if (item.contentDetails.newItemCount > 0) {
                    newCount.visibility = View.VISIBLE
                    newCount.text = item.contentDetails.newItemCount.toString()
                } else {
                    newCount.visibility = View.GONE
                }
            }
            thumbnail.loadImageSmall(CommonUtils.getThumbnail(item.snippet.thumbnails), 300, 300)
        }
    }

}