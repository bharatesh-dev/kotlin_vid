package com.kot.vid.fragments

import android.content.Intent
import com.google.api.services.youtube.model.ActivityListResponse
import com.kot.vid.callbacks.ResponseListener
import com.kot.vid.callbacks.ViewListener

class HomePresenter(private var viewListener: ViewListener<ActivityListResponse>?) : ResponseListener<ActivityListResponse> {

    private val model = HomeModule(this)

    fun makeHomVideosRequest() {
        viewListener?.showProgress()
        model.fetchHomeVideos()
    }

    fun destroy() {
        viewListener = null
        model.onDestroy()
    }

    override fun onSuccess(response: ActivityListResponse) {
        viewListener?.hideProgress()
        viewListener?.onSuccess(response)
    }

    override fun onError(e: String) {
        viewListener?.hideProgress()
        viewListener?.onError(e)
    }

    override fun onAuthError(intent: Intent) {
        viewListener?.hideProgress()
        viewListener?.onAuthError(intent)
    }
}