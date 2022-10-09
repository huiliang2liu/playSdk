package com.lhl;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lhl.listener.LoginListener;
import com.lhl.listener.PayListener;
import com.lhl.login.ILogin;
import com.lhl.pay.IPay;
import com.lhl.result.Result;
import com.lhl.result.ResultImpl;
import com.lhl.result.activity.ResultCallback;

public class StartActivity implements Application.ActivityLifecycleCallbacks {
    private Activity mActivity;
    private static final int LOGIN_CODE = 1;
    public static final String LOGIN_UID = "login_uid";

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreStarted(activity);
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostStarted(activity);
    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreResumed(activity);
    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostResumed(activity);
    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPrePaused(activity);
    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostPaused(activity);
    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreStopped(activity);
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostStopped(activity);
    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreSaveInstanceState(activity, outState);
    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Application.ActivityLifecycleCallbacks.super.onActivityPostSaveInstanceState(activity, outState);
    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {
        if (mActivity == activity)
            mActivity = null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


    public void login(@ILogin.Types int type, LoginListener listener) {
        if (mActivity == null)
            return;
        new ILogin.Builder().setActivity(mActivity).setType(type).setListener(listener).build().login();
    }


    public void pay(int num, String goods, float price, String currency, String passThrough, PayListener listener, @IPay.Types int type) {
        if (mActivity == null)
            return;
        new IPay.Builder().setActivity(mActivity).setFlag(type).setListener(listener).build().pay(num, goods, price, currency, passThrough);
    }

    public void startActivity(Intent intent) {
        if (intent == null || mActivity == null)
            return;
        mActivity.startActivity(intent);
    }

    public void startActivityForResult(int requestCode, Intent intent, ResultCallback callback) {
        if (intent == null || mActivity == null)
            return;
        Result result = new ResultImpl(mActivity);
        result.startActivityForResult(requestCode, intent, new ResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (callback != null)
                    callback.onActivityResult(requestCode, resultCode, data);
            }
        });
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options, ResultCallback callback) throws IntentSender.SendIntentException {
        if (intent == null || mActivity == null)
            return;
        Result result = new ResultImpl(mActivity);
        result.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options, new ResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (callback != null)
                    callback.onActivityResult(requestCode, resultCode, data);
            }
        });
    }

}
