package com.lhl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.lhl.app.life.AppLifeListener;
import com.lhl.app.life.Life;
import com.lhl.listener.LoginListener;
import com.lhl.listener.PayListener;
import com.lhl.login.ILogin;
import com.lhl.pay.IPay;
import com.lhl.result.Result;
import com.lhl.result.ResultImpl;
import com.lhl.result.activity.ResultCallback;

import java.util.ArrayList;
import java.util.List;

public class App implements ViewModelStoreOwner, HasDefaultViewModelProviderFactory, AppLifeListener {
    private Application context;
    private ViewModelStore store = new ViewModelStore();
    private Life life;
    private List<AppLifeListener> lifeListeners = new ArrayList<>();
    private ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getDeclaredConstructor(Application.class).newInstance(context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                return modelClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };
    static App app;
    private StartActivity activity;

    App(Context context) {
        this.context = (Application) context.getApplicationContext();
        life = new Life(this.context);
        life.setAppLifeListener(this);
        activity = new StartActivity();
        this.context.registerActivityLifecycleCallbacks(activity);
    }

    public static App getApp() {
        return app;
    }

    public void googleLogin(LoginListener listener) {
        assert listener != null : "listener is null";
        activity.login(ILogin.LOGIN_IN_GOOGLE, listener);
    }

    public void facebookLogin(LoginListener listener) {
        assert listener != null : "listener is null";
        activity.login(ILogin.LOGIN_IN_FACEBOOK, listener);
    }

    public void huaWeiAuthorization(LoginListener listener) {
        assert listener != null : "listener is null";
        activity.login(ILogin.LOGIN_IN_HUA_WEI_AUTHORIZATION, listener);
    }

    public void huaWeiIdToken(LoginListener listener) {
        assert listener != null : "listener is null";
        activity.login(ILogin.LOGIN_IN_HUA_WEI_ID_TOKEN, listener);
    }

    public void googlePay(int num, String goods, float price, String currency, String passThrough, PayListener listener) {
        assert listener != null : "listener is null";
        activity.pay(num, goods, price, currency, passThrough, listener, IPay.GOOGLE_PAY);
    }

    public void huaWeiPay(int num, String goods, float price, String currency, String passThrough, PayListener listener) {
        assert listener != null : "listener is null";
        activity.pay(num, goods, price, currency, passThrough, listener, IPay.HUA_WEI_PAY);
    }

    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    public void startActivityForResult(int requestCode, Intent intent, ResultCallback callback) {
        activity.startActivityForResult(requestCode, intent, callback);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options, ResultCallback callback) throws IntentSender.SendIntentException {
        activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options, callback);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return store;
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return factory;
    }

    @Override
    public void onAppResume() {
    }

    @Override
    public void onAppPause() {
    }
}
