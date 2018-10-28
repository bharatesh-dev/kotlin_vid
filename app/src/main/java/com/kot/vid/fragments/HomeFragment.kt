package com.kot.vid.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import com.google.api.services.youtube.model.ActivityListResponse
import com.kot.vid.R
import com.kot.vid.adapters.ActivitiesAdapter
import com.kot.vid.callbacks.ViewListener
import com.kot.vid.ui.linearVertical
import com.kot.vid.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), ViewListener<ActivityListResponse> {

    private lateinit var presenter: HomePresenter
    private var activities: ActivityListResponse? = null

    companion object {
        var fragment: Fragment? = null
        fun instance(): Fragment {
            if (fragment == null)
                fragment = HomeFragment()
            return fragment as Fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = HomePresenter(this)
    }

    override fun getLayoutResource() = R.layout.fragment_home

    override fun onStart() {
        super.onStart()
        if (activities == null) {
            presenter.makeHomVideosRequest()
        } else {
            updateUI(activities!!)
        }
    }

    private fun updateUI(response: ActivityListResponse) {
        if (response.isNotEmpty() && response.items.isNotEmpty()) {
            val adapter = ActivitiesAdapter()
            adapter.addItems(response.items)
            recyclerView.adapter = adapter
            //recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            recyclerView.linearVertical()
        } else {
            onError("Oops, No videos found")
        }
    }

    override fun onSuccess(response: ActivityListResponse) {
        activities = response
        updateUI(activities!!)
    }

    override fun onStop() {
        presenter.destroy()
        super.onStop()
    }

    override fun showProgress() {
        progressBar(true)
    }

    override fun hideProgress() {
        progressBar(false)
    }

    override fun onAuthError(intent: Intent) {
        CommonUtils.isAuthError = true
        startActivityForResult(intent, CommonUtils.AUTH_ERROR_REQUEST)
    }
}