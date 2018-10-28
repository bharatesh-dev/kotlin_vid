package com.kot.vid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoListResponse
import com.kot.vid.ProgressError
import com.kot.vid.callbacks.ResponseListener
import com.kot.vid.model.LikedModel

class LikedViewModel : ViewModel() {

    private var videoList: MutableLiveData<MutableList<Video>> = MutableLiveData()
    private var rateVideoPosition = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<String>()
    private var authIntent = MutableLiveData<Intent>()
    private var model: LikedModel? = null
    var progressData: ProgressError = ProgressError(ObservableField(""), ObservableBoolean(false))
    private var rateType = "none"

    fun init(rate: String) {
        model = LikedModel()
        rateType = rate
    }

    fun requestLikedVideos() {
        progressData.progress.set(true)
        model?.fetchVideosOfRating(object : ResponseListener<VideoListResponse> {
            override fun onAuthError(intent: Intent) {
                progressData.progress.set(false)
                authIntent.value = intent
            }

            override fun onSuccess(response: VideoListResponse) {
                progressData.progress.set(false)
                if (response.items.isNotEmpty()) {
                    videoList.value = response.items
                } else {
                    progressData.error.set("No videos found")
                }
            }

            override fun onError(error: String) {
                progressData.progress.set(false)
                progressData.error.set(error)
            }
        }, "like")
    }

    fun rateVideo(videoId: String, position: Any?) {
        if (position == null)
            return
        if (position is Int) {
            progressData.progress.set(true)
            model?.rateVideo(object : ResponseListener<String> {
                override fun onAuthError(intent: Intent) {
                    progressData.progress.set(false)
                    authIntent.value = intent
                }

                override fun onSuccess(response: String) {
                    progressData.progress.set(false)
                    rateVideoPosition.value = position
                }

                override fun onError(msg: String) {
                    progressData.progress.set(false)
                    errorMessage.value = msg
                }
            }, videoId, rateType)
        }
    }

    override fun onCleared() {
        super.onCleared()
        model?.dispose()
    }

    fun getVideos(): MutableLiveData<MutableList<Video>> {
        return videoList
    }

    fun getRatingResponse() = rateVideoPosition

    fun errorMessage() = errorMessage

    fun getAuthIntent() = authIntent
}