package com.lhl.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.lhl.listener.LoginListener;
import com.lhl.model.User;

import java.util.Arrays;

public class FacebookLogin extends AbsLogin implements FacebookCallback<LoginResult> {
    private static final String TAG = "FacebookLogin";
    private volatile static boolean isInit = false;

    public FacebookLogin(Activity activity, LoginListener listener) {
        super(activity, listener);
        if (!isInit)
            synchronized (FacebookLogin.class) {
                if (!isInit) {
                    isInit = true;
                    FacebookSdk.sdkInitialize(activity.getApplicationContext());
                    AppEventsLogger.activateApp(activity.getApplication());
                }
            }
    }

    @Override
    public void login() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            loginResult(accessToken);
            return;
        }
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        CallbackManager callbackManger = CallbackManager.Factory.create();
        LoginFragment fragment = new LoginFragment();
        fragment.callbackManger = callbackManger;
        FragmentManager manager = activity.getFragmentManager();
        if (manager.findFragmentByTag(TAG) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, TAG);
            if (android.os.Build.VERSION.SDK_INT >= 24)
                transaction.commitNow();
            else {
                transaction.commitAllowingStateLoss();
                manager.executePendingTransactions();
            }
        }
        LoginManager.getInstance()
                .registerCallback(callbackManger, this);
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
    }

    public static class LoginFragment extends Fragment {
        private CallbackManager callbackManger;

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManger.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        loginResult(loginResult != null ? loginResult.getAccessToken() : null);
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "onCancel");
        loginResult(null);
    }

    @Override
    public void onError(@NonNull FacebookException e) {
        Log.e(TAG, "onError", e);
        loginResult(null);
    }

    private void loginResult(AccessToken token) {
        if (token == null) {
            listener.onLoginFailure();
            return;
        }
        User user = new User();
        user.token = token.getToken();
        user.id = token.getUserId();
        listener.onLoginSucceed(user);
//        user.email = loginResult.getAccessToken().

    }
}
