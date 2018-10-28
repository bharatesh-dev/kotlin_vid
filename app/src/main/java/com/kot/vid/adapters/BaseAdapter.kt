package com.kot.vid.adapters

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH>() {

    var items: MutableList<T> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    @LayoutRes
    abstract fun getLayoutResourceId(): Int

    abstract fun createViewHolder(v: View): VH

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VH {
        return createViewHolder(LayoutInflater.from(p0.context).inflate(getLayoutResourceId(), p0, false))
    }

    override fun onBindViewHolder(vh: VH, pos: Int) {
        if (!items.isEmpty()) {
            vh.onBind(items[pos], vh)
        }
    }

    //Add initial/refresh items and call notifyDataSetChanged
    fun addItems(list: MutableList<T>) {
        this.items.clear()
        this.items.addAll(list)
        notifyDataSetChanged()
    }

    fun appendItems(list: MutableList<T>) {
        this.items.addAll(list)
    }

    fun removeItem(position: Int) {
        if (items.isNotEmpty()) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}