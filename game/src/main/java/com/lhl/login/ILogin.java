package com.lhl.login;

import android.app.Activity;

import androidx.annotation.IntDef;

import com.lhl.listener.LoginListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ILogin {
    int LOGIN_IN_GOOGLE = 1;
    int LOGIN_IN_FACEBOOK = LOGIN_IN_GOOGLE<<1;
    int LOGIN_IN_HUA_WEI_AUTHORIZATION = LOGIN_IN_FACEBOOK<<1;
    int LOGIN_IN_HUA_WEI_ID_TOKEN = LOGIN_IN_HUA_WEI_AUTHORIZATION<<1;

    @IntDef({LOGIN_IN_GOOGLE,LOGIN_IN_FACEBOOK,LOGIN_IN_HUA_WEI_AUTHORIZATION,LOGIN_IN_HUA_WEI_ID_TOKEN})
    @Retention(RetentionPolicy.SOURCE)
    @interface Types {
    }

    void login();

    class Builder {
        private Activity activity;
        private LoginListener listener;
        private int type;

        public Builder() {

        }

        public Builder setListener(LoginListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setType(@Types int type) {
            this.type = type;
            return this;
        }

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public ILogin build() {
            assert activity != null : "activity is null";
            assert listener != null : "LoginListener is null";
            if (type == LOGIN_IN_GOOGLE)
                return new GoogleLogin(activity, listener);
            if(type == LOGIN_IN_FACEBOOK)
                return new FacebookLogin(activity,listener);
            if(type == LOGIN_IN_HUA_WEI_AUTHORIZATION)
                return new HuaWeiAuthorization(activity,listener);
            if(type == LOGIN_IN_HUA_WEI_ID_TOKEN)
                return new HuaWeiIdToken(activity,listener);
            return null;
        }
    }
}
