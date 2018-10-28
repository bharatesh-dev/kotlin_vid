package com.kot.vid.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.api.services.youtube.model.Video
import com.kot.vid.BR
import com.kot.vid.R
import com.kot.vid.databinding.RatingVideoItemBinding
import com.kot.vid.viewmodel.LikedViewModel
import kotlinx.android.synthetic.main.rating_video_item.view.*

class LikedVideosAdapter : BaseAdapter<Video, LikedVideosAdapter.ViewHolder>() {

    private lateinit var model: LikedViewModel

    fun setViewModel(model: LikedViewModel) {
        this.model = model
    }

    override fun createViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getLayoutResourceId() = R.layout.rating_video_item

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding = RatingVideoItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return createViewHolder(binding.root)
    }

    inner class ViewHolder(val view: View) : BaseViewHolder<Video>(view) {

        override fun onBind(item: Video, vh: RecyclerView.ViewHolder) {
            val binding = DataBindingUtil.getBinding<RatingVideoItemBinding>(view)
            binding?.setVariable(BR.video, item)
            view.action.setOnClickListener {
                model.rateVideo(item.id, vh.adapterPosition)
            }
        }
    }
}