package com.kot.vid.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.kot.vid.AppCredentials
import com.kot.vid.R
import com.kot.vid.utils.CommonUtils
import kotlinx.android.synthetic.main.content_login.*

private const val REQUEST_CODE_SIGN_IN = 11
private const val REQUEST_RESOLUTION_CODE = 12

class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener, AppCredentials.CredentialListener {

    override fun getResourceLayout() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignIn.setOnClickListener { signIn() }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        CommonUtils.toast(this, "SignIn failed ${p0.errorMessage}")
        if (p0.hasResolution()) {
            p0.startResolutionForResult(this, REQUEST_RESOLUTION_CODE)
        }
    }

    private fun signIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Scope(YouTubeScopes.YOUTUBE)).requestEmail().build()
        val gSignInClient = GoogleSignIn.getClient(this, signInOptions)
        startActivityForResult(gSignInClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account = task.result // Sign in success
                AppCredentials.getYoutubeInstance(this, account, this)
            } else {
                CommonUtils.toast(this, "SignIn failed")
            }
        } else if (requestCode == REQUEST_RESOLUTION_CODE && resultCode == Activity.RESULT_OK) {
            signIn()
        }
    }

    private fun toHomePage() {
        startActivity(HomeActivity.intent(this))
        finish()
    }

    override fun onCredentialError(msg: String) {
        CommonUtils.toast(this, "SignIn failed $msg")
    }

    override fun onCredentialSuccess(youtube: YouTube) {
        toHomePage()
    }
}
