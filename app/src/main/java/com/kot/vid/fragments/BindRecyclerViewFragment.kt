package com.kot.vid.fragments

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

abstract class BindRecyclerViewFragment<T : ViewDataBinding> : Fragment() {

    abstract fun getLayoutResourceId(): Int

    var t: T? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        t = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        return t?.root
    }

    fun getBinding(): T? {
        return t
    }

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}