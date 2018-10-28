package com.kot.vid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.youtube.YouTube;
import com.kot.vid.AppCredentials;
import com.kot.vid.R;
import com.kot.vid.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SplashScreenActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, AppCredentials.CredentialListener {

    @Override
    public int getResourceLayout() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInAccount account = getLoggedAccount();

        if (account != null) {
            AppCredentials.getYoutubeInstance(this, getLoggedAccount(), this);
        } else {
            toLoginPage();
        }
    }

    private void toLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toHomePage() {
        startActivity(HomeActivity.intent(this));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        CommonUtils.toast(this, "Connection failed " + connectionResult.getErrorMessage());
    }

    @Override
    public void onCredentialSuccess(@NotNull YouTube youtube) {
        toHomePage();
    }

    @Override
    public void onCredentialError(@NotNull String msg) {
        CommonUtils.toast(this, "Something went wrong!!");
    }
}