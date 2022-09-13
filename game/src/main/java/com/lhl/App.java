package com.lhl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

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
import com.lhl.login.ILogin;
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

    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    public void startActivityForResult(int requestCode, Intent intent, ResultCallback callback) {
        activity.startActivityForResult(requestCode, intent, callback);
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
