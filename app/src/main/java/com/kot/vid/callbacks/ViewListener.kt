package com.kot.vid.callbacks

interface ViewListener<T> : ResponseListener<T> {
    fun showProgress()
    fun hideProgress()
}