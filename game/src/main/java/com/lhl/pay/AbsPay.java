package com.lhl.pay;

import android.app.Activity;

import com.lhl.listener.PayListener;

public abstract class AbsPay implements IPay {
    protected Activity activity;
    protected PayListener listener;

    public AbsPay(Activity activity, PayListener listener) {
        this.activity = activity;
        this.listener = listener;
        assert activity != null : "activity is null";
        assert listener != null : "listener iis null";
    }
}
