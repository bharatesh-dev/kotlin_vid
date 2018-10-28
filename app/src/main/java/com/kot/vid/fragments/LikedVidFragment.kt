package com.kot.vid.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.google.api.services.youtube.model.Video
import com.kot.vid.R
import com.kot.vid.adapters.LikedVideosAdapter
import com.kot.vid.databinding.FragmentLikedBinding
import com.kot.vid.ui.linearVertical
import com.kot.vid.utils.CommonUtils
import com.kot.vid.viewmodel.LikedViewModel
import kotlinx.android.synthetic.main.fragment_liked.*

class LikedVidFragment : BindRecyclerViewFragment<FragmentLikedBinding>() {

    private var adapter: LikedVideosAdapter? = null
    private var fragmentBinding: FragmentLikedBinding? = null
    private var viewModel: LikedViewModel? = null
    private var likesVideosList: MutableList<Video>? = null

    companion object {
        var fragment: Fragment? = null
        fun instance(): Fragment {
            if (fragment == null)
                fragment = LikedVidFragment()
            return fragment as Fragment
        }
    }

    override fun getLayoutResourceId() = R.layout.fragment_liked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LikedViewModel::class.java)

        viewModel?.let { it ->
            it.getVideos().observe(this, Observer<MutableList<Video>> { e -> e?.let { onVideos(e) } })
            it.init("none")
            it.getRatingResponse().observe(this, Observer { e -> e?.let { ratingResponse(e) } })
            it.errorMessage().observe(this, Observer { e -> e?.let { showErrorMsg(e) } })
            it.getAuthIntent().observe(this, Observer { authError(it) })
        }
    }

    private fun authError(it: Intent?) {
        CommonUtils.isAuthError = true
        startActivityForResult(it, CommonUtils.AUTH_ERROR_REQUEST)
    }

    private fun ratingResponse(position: Int) {
        adapter?.removeItem(position)
    }

    private fun showErrorMsg(msg: String) {
        toast(msg)
    }

    override fun onStart() {
        super.onStart()
        if (likesVideosList == null) {
            viewModel?.requestLikedVideos()
        } else {
            onVideos(likesVideosList!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBinding = getBinding()
        fragmentBinding?.model = viewModel
    }

    private fun onVideos(items: MutableList<Video>) {
        likesVideosList = items
        adapter = LikedVideosAdapter()
        adapter?.setViewModel(viewModel!!)
        adapter?.addItems(items)
        recyclerView.adapter = adapter
        recyclerView.linearVertical()
    }
}