package com.kot.vid.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(view: View, var listener: View.OnClickListener? = null) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: T, position: RecyclerView.ViewHolder)

    init {
        view.setOnClickListener { listener }
    }
}