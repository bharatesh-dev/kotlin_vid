package com.kot.vid

import android.content.Context
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

object AppCredentials {
    private var youTube: YouTube? = null
    private var disposable: Disposable? = null

    @JvmStatic
    fun getYoutubeInstance(context: Context, account: GoogleSignInAccount?, listener: CredentialListener) {
        if (youTube == null) {
            disposable = Single.fromCallable {
                val credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(YouTubeScopes.YOUTUBE))
                credential.selectedAccount = account?.account
                credential.selectedAccountName = account?.email
                youTube = YouTube.Builder(AndroidHttp.newCompatibleTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName(context.getString(R.string.app_name))
                        .build()
                youTube
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t ->
                listener.onCredentialSuccess(t!!)
            }, {
                listener.onCredentialError("Login : Something went wrong")
            })
        } else {
            listener.onCredentialSuccess(youTube!!)
        }
    }

    fun dispose() {
        disposable?.dispose()
    }

    interface CredentialListener {
        fun onCredentialSuccess(youtube: YouTube)
        fun onCredentialError(msg: String)
    }

    @JvmStatic
    fun getYoutube(): YouTube {
        return youTube!!
    }

    @JvmStatic
    @BindingAdapter("bind:url")
    fun url(imageView: ImageView, url: String) {
        Picasso.get().load(url).resize(360, 130).centerCrop().into(imageView)
    }
}