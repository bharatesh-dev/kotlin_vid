package com.kot.vid.ui

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.google.api.services.youtube.model.Thumbnail
import com.google.api.services.youtube.model.ThumbnailDetails
import com.kot.vid.R
import com.kot.vid.utils.CommonUtils
import com.squareup.picasso.Picasso

fun ImageView.loadImage(t: Thumbnail?) {
    val url = t?.url
    if (url != null) {
        Picasso.get().load(url).resize(360, 180).centerCrop().into(this)
    }
}

fun ImageView.loadImageSmall(url: String, w: Int, h: Int) {
    if (url.isNotBlank()) {
        Picasso.get().load(url).fit().into(this)
    }
}

fun RecyclerView.linearVertical() {
    val manager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    val divider = ItemsDividerDecorator(this.context)
    addItemDecoration(divider)
    layoutManager = manager
}

fun RecyclerView.gridLayout() {
    val manager = GridLayoutManager(this.context, 2)
    val divider = ItemsDividerDecorator(this.context, manager.orientation)
    addItemDecoration(divider)
    layoutManager = manager
}

fun ThumbnailDetails.highUrl(): String {
    return CommonUtils.getThumbnail(this)
}