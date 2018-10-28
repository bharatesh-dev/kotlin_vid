package com.kot.vid.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.kot.vid.R
import kotlinx.android.synthetic.main.empty_fragment.*

class EmptyFragment : BaseFragment() {

    companion object {
        fun instance(title: String): Fragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            val fragment = EmptyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutResource() = R.layout.empty_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = arguments?.getString("title", "Empty Fragment")
    }
}