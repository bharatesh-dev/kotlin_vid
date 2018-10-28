package com.kot.vid.fragments

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.youtube.model.ActivityListResponse
import com.kot.vid.AppCredentials
import com.kot.vid.callbacks.ApiRequest
import com.kot.vid.callbacks.ResponseListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeModule : ApiRequest {
    
    var callbakcs: ResponseListener<ActivityListResponse>

    //this constructor is just for example, best practice is to use it as primary constructor.
    constructor(c: ResponseListener<ActivityListResponse>) {
        this.callbakcs = c
    }

    private var disposable: Disposable? = null

    override fun fetchHomeVideos() {
        disposable = Single.fromCallable { AppCredentials.getYoutube().activities().list("snippet,contentDetails").setMaxResults(15).setHome(true).execute() }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    callbakcs.onSuccess(response)
                }, { e ->
                    if (e is UserRecoverableAuthIOException) {
                        callbakcs.onAuthError(e.intent)
                    } else {
                        callbakcs.onError("Something went wrong.. " + e.javaClass.simpleName)
                        e.printStackTrace()
                    }
                })
    }

    override fun onDestroy() {
        disposable?.let {
            if (!it.isDisposed)
                it.dispose()
        }
    }
}