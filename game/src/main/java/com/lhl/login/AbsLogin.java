package com.lhl.login;

import android.app.Activity;

import com.lhl.listener.LoginListener;

public abstract class AbsLogin implements ILogin {
    protected LoginListener listener;
    protected Activity activity;

    public AbsLogin(Activity activity, LoginListener listener) {
        this.activity = activity;
        this.listener = listener;
        assert listener != null : "LoginListener is null";
        assert activity != null : "activity  is null";
    }
}
