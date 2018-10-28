package com.kot.vid.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kot.vid.activities.LoginActivity
import com.kot.vid.utils.CommonUtils
import kotlinx.android.synthetic.main.layout_progress_error.*

abstract class BaseFragment : Fragment() {

    abstract fun getLayoutResource(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    fun log(msg: String) {
        Log.i("TAG", msg)
    }

    fun loge(msg: String) {
        Log.e("TAG", msg)
    }

    protected fun progressBar(show: Boolean) {
        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onError(msg: String) {
        progressBar(false)
        if (CommonUtils.AUTH_ERROR == msg) {
            toLoginPage()
        } else {
            error?.visibility = View.VISIBLE
            error?.text = msg
        }
    }

    private fun toLoginPage() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    fun onError(id: Int) {
        onError(getString(id))
    }

    open fun onAuthError(intent: Intent) {
        CommonUtils.isAuthError = true
        startActivityForResult(intent, CommonUtils.AUTH_ERROR_REQUEST)
    }
}