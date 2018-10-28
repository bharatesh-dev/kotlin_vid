package com.kot.vid.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kot.vid.fragments.FragmentLoader

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getResourceLayout(): Int

    protected lateinit var fragLoader: FragmentLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResourceLayout())
        fragLoader = FragmentLoader(supportFragmentManager)
    }
    
    fun getLoggedAccount(): GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
}