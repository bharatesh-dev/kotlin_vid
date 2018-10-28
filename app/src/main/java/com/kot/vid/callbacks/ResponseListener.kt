package com.kot.vid.callbacks

import android.content.Intent

interface ResponseListener<T> {
    fun onSuccess(response: T)
    fun onError(msg: String)
    fun onAuthError(intent: Intent)
}