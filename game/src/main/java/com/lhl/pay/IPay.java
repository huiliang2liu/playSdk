package com.lhl.pay;

import android.app.Activity;

import androidx.annotation.IntDef;

import com.lhl.listener.PayListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IPay {
    int GOOGLE_PAY = 1;

    void pay(int num, String goods, float price, String currency, String passThrough);


    @IntDef({GOOGLE_PAY})
    @Retention(RetentionPolicy.SOURCE)
    @interface Types {
    }

    class Builder {
        private Activity activity;
        private PayListener listener;
        private int flag;

        public Builder() {

        }

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setListener(PayListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setFlag(@Types int flag) {
            this.flag = flag;
            return this;
        }

        public IPay build() {
            if (flag == GOOGLE_PAY)
                return new GooglePay(activity, listener);
            return null;
        }
    }
}
