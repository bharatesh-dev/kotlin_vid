package com.kot.vid.fragments

import android.support.v4.app.Fragment
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.youtube.model.SubscriptionListResponse
import com.kot.vid.AppCredentials
import com.kot.vid.R
import com.kot.vid.adapters.SubscriptionAdapter
import com.kot.vid.ui.gridLayout
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_items.*
import java.util.concurrent.Callable

class SubscriptionsFragment : RecyclerViewFragment(), SingleObserver<SubscriptionListResponse> {

    private var mSubscriptions: SubscriptionListResponse? = null
    private var disposable: Disposable? = null

    companion object {
        var fragment: Fragment? = null
        fun instance(): Fragment {
            if (fragment == null)
                fragment = SubscriptionsFragment()
            return fragment as Fragment
        }
    }

    override fun onStart() {
        super.onStart()
        if (mSubscriptions == null) {
            fetchSubscriptions()
        } else {
            updateUI(mSubscriptions!!)
        }
    }

    private fun updateUI(response: SubscriptionListResponse) {
        if (response.isNotEmpty() && response.items.isNotEmpty()) {
            val adapter = SubscriptionAdapter(response.items)
            recyclerView.adapter = adapter
            recyclerView.gridLayout()
        } else {
            onError(R.string.no_subsc_found)
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (e is UserRecoverableAuthIOException) {
            progressBar(false)
            onAuthError(e.intent)
        } else {
            onError("Error: " + e.javaClass.simpleName)
        }
    }

    override fun onSuccess(t: SubscriptionListResponse) {
        progressBar(false)
        mSubscriptions = t
        updateUI(mSubscriptions!!)
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    private fun fetchSubscriptions() {
        progressBar(true)
        //using anonymous inner class
        Single.fromCallable(object : Callable<SubscriptionListResponse> {
            override fun call(): SubscriptionListResponse {
                return AppCredentials.getYoutube().subscriptions().list("snippet,contentDetails").setMine(true).setMaxResults(25).execute()
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this)
    }
}