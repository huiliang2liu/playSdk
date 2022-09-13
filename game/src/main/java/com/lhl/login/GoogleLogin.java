package com.lhl.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lhl.App;
import com.lhl.listener.LoginListener;
import com.lhl.model.User;

class GoogleLogin extends AbsLogin {
    private static final String GOOGLE_WEB_CLIENT_KEY = "google_web_client_key";
    private static final String TAG = "GoogleLogin";
    private static final int RC_SIGN_IN = 1000000;

    public GoogleLogin(Activity activity, LoginListener listener) {
        super(activity,listener);
    }

    @Override
    public void login() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account != null) {
            handleSignInResult(account);
            return;
        }
        String webClientKey = null;
        try {
            webClientKey = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData.getString(GOOGLE_WEB_CLIENT_KEY, "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail();
        if (webClientKey != null && !webClientKey.isEmpty())
            builder.requestIdToken(webClientKey);
        GoogleSignInClient client = GoogleSignIn.getClient(activity, builder.build());
        App.getApp().startActivityForResult(RC_SIGN_IN, client.getSignInIntent(), (int requestCode, int resultCode, Intent data) -> {
            if (requestCode != RC_SIGN_IN)
                return;
            if (resultCode != Activity.RESULT_OK) {
                handleSignInResult(null);
                return;
            }
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task == null) {
                handleSignInResult(null);
                return;
            }
            try {
                handleSignInResult(task.getResult(ApiException.class));
            } catch (ApiException e) {
                e.printStackTrace();
                handleSignInResult(null);
            }
        });
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        Log.e(TAG, "handleSignInResult");
        if (account == null) {
            listener.onLoginFailure();
            return;
        }
        Log.e(TAG, "success");
        User user = new User();
        user.email = account.getEmail();
        user.id = account.getId();
        user.token = account.getIdToken();
        listener.onLoginSucceed(user);
    }

}
