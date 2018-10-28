package com.kot.vid.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kot.vid.R

class ItemsDividerDecorator(val context: Context, val orientation: Int = DividerItemDecoration.VERTICAL) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION)
            return

        val size = context.resources.getDimensionPixelOffset(R.dimen.normal)
        outRect.top = size
        outRect.left = size
        outRect.right = size

        if (position == parent.adapter?.itemCount?.minus(1))
            outRect.bottom = size * 2
    }
}