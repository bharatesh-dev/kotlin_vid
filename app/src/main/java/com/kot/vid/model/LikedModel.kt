package com.kot.vid.model

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.youtube.model.VideoListResponse
import com.kot.vid.AppCredentials
import com.kot.vid.callbacks.ResponseListener
import com.kot.vid.utils.CommonUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LikedModel {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun fetchVideosOfRating(listener: ResponseListener<VideoListResponse>, rating: String) {
        val youTube = AppCredentials.getYoutube()
        val disposable = Single.fromCallable {
            youTube.videos().list("snippet,contentDetails").setMaxResults(50).setMyRating(rating).execute()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
            listener.onSuccess(response)
        }, { throwable ->
            if (throwable is UserRecoverableAuthIOException)
                listener.onAuthError(throwable.intent)
            else
                listener.onError(CommonUtils.getErrorMessage(throwable))
        })
        compositeDisposable.add(disposable)
    }

    fun rateVideo(listener: ResponseListener<String>, vidId: String, rating: String) {
        val youTube = AppCredentials.getYoutube()
        val disposable = Single.fromCallable {
            youTube.videos().rate(vidId, rating).executeUnparsed()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it?.statusCode == 204) {
                listener.onSuccess("")
            }
            it?.disconnect()
        }, { throwable ->
            if (throwable is UserRecoverableAuthIOException)
                listener.onAuthError(throwable.intent)
            else
                listener.onError(CommonUtils.getErrorMessage(throwable))
        })
        compositeDisposable.add(disposable)
    }


    fun dispose() {
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}