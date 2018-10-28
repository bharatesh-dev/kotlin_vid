package com.kot.vid.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.kot.vid.AppCredentials
import com.kot.vid.R
import com.kot.vid.fragments.HomeFragment
import com.kot.vid.fragments.LikedVidFragment
import com.kot.vid.fragments.SubscriptionsFragment
import com.kot.vid.utils.CommonUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_home_page.*

private const val RECORD_NAVG_FLOW = "navg_flow_record"

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener, AppCredentials.CredentialListener, GoogleApiClient.OnConnectionFailedListener {

    private val navigationFlow = mutableListOf<Int>()

    companion object {
        @JvmStatic
        fun intent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun getResourceLayout() = R.layout.activity_home_page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationBar.setOnNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)

        AppCredentials.getYoutubeInstance(this, getLoggedAccount(), this)

        savedInstanceState?.let {
            val list = savedInstanceState.getIntArray(RECORD_NAVG_FLOW)?.toMutableList()
            if (list != null) {
                navigationFlow.addAll(list)
            }
        }
    }

    override fun onCredentialError(msg: String) {
        CommonUtils.toast(this, "Something went wrong!!")
        AppCredentials.dispose()
    }

    override fun onCredentialSuccess(youtube: YouTube) {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            fragLoader.add(HomeFragment.instance(), false)
            navigationFlow.add(R.id.menu_home)
        }
        AppCredentials.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_page, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.logout) {
            logoutUser()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Scope(YouTubeScopes.YOUTUBE)).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
        val task = googleSignInClient.signOut()

        val progressBar = ProgressDialog(this)
        progressBar.setMessage("signing out.. please wait")
        progressBar.show()
        if (task.isSuccessful) {
            progressBar.dismiss()
            finish()
        } else {
            progressBar.dismiss()
            CommonUtils.toast(this, "Sign-out failed, error occurred" + task.exception)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        CommonUtils.toast(this, "item selected");
        var fragment: Fragment? = null
        when (p0.itemId) {
            R.id.menu_home -> {
                CommonUtils.log("home navId= ${p0.itemId}")
                fragment = getFragment(HomeFragment::class.java.simpleName)
            }
            R.id.menu_subscription -> {
                CommonUtils.log("subs navId= ${p0.itemId}")
                fragment = getFragment(SubscriptionsFragment::class.java.simpleName)
            }
            R.id.menu_favourites -> {
                CommonUtils.log("liked navId= ${p0.itemId}")
                fragment = getFragment(LikedVidFragment::class.java.simpleName)
            }
        }

        fragment?.let {
            showOrAddFragment(it)
            recordFlow(p0.itemId)
            return true
        }
        return false
    }

    private fun getFragment(tag: String): Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        CommonUtils.loge("Tag: $tag frag=$fragment")
        if (fragment != null)
            return fragment

        when (tag) {
            HomeFragment::class.java.simpleName -> {
                return HomeFragment.instance()
            }
            SubscriptionsFragment::class.java.simpleName -> {
                return SubscriptionsFragment.instance()
            }
            LikedVidFragment::class.java.simpleName -> {
                return LikedVidFragment.instance()
            }
        }
        return null
    }

    @SuppressLint("CheckResult")
    private fun showOrAddFragment(loadFragment: Fragment) {
        var hideFragment: Fragment? = null
        Observable.fromIterable(supportFragmentManager.fragments).filter { f -> f.isVisible }
                .subscribe({ f -> hideFragment = f }, { CommonUtils.loge("No Fragment visible") }, {
                    CommonUtils.log("show=$loadFragment hide=$hideFragment")
                    if (fragLoader.isAdded(loadFragment::class.java.simpleName))
                        fragLoader.show(loadFragment, hideFragment)
                    else
                        fragLoader.add(loadFragment, true)
                })

    }

    private fun recordFlow(id: Int) {
        if (navigationFlow.contains(id))
            navigationFlow.remove(id)
        navigationFlow.add(id)
    }

    override fun onBackPressed() {

        if (navigationFlow.isEmpty())
            finish()
        else {
            navigationFlow.removeAt(navigationFlow.lastIndex)
            if (navigationFlow.isEmpty()) {
                finish()
            } else {
                CommonUtils.loge("Id= ${navigationFlow.last()}")
                navigationBar.selectedItemId = navigationFlow.last()
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putIntArray(RECORD_NAVG_FLOW, navigationFlow.toIntArray())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (CommonUtils.isAuthError) {
            CommonUtils.isAuthError = false
            if (resultCode != Activity.RESULT_OK) {
                finish()
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        CommonUtils.toast(this, "Something went wrong")
    }
}