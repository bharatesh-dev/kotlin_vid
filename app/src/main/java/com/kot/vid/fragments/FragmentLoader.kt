package com.kot.vid.fragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.kot.vid.R
import com.kot.vid.utils.CommonUtils

class FragmentLoader(private val manager: FragmentManager) {

    fun replace(frag: Fragment, id: Int) {
        val transaction = manager.beginTransaction()
        val tag = frag::class.java.name
        transaction.replace(id, frag, tag)
        transaction.addToBackStack(tag)
        transaction.commit()
    }

    fun add(frag: Fragment, backStack: Boolean) {
        //CommonUtils.log("add tag=${frag::class.java.simpleName}")
        val transaction = manager.beginTransaction()
        transaction.add(R.id.container, frag, frag::class.java.simpleName)
        if (backStack)
            transaction.addToBackStack(frag::class.java.simpleName)
        transaction.commit()
    }

    fun show(f1: Fragment, f0: Fragment?) {
        val transaction = manager.beginTransaction()
        if (f0 != null)
            transaction.hide(f0)
        transaction.show(f1).commit()
    }

    fun isAdded(tag: String): Boolean {
        val frag = manager.findFragmentByTag(tag)
        return frag != null
    }

    fun replace(frag: Fragment) {
        replace(frag, R.id.container)
    }

    fun pop() {
        manager.popBackStack()
    }
}