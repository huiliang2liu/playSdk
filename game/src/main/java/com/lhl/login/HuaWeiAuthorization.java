package com.lhl.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.feature.result.AbstractAuthAccount;
import com.lhl.App;
import com.lhl.listener.LoginListener;
import com.lhl.model.User;
import com.lhl.result.activity.ResultCallback;

public class HuaWeiAuthorization extends AbsLogin {
    private AccountAuthParams authParams;
    private AccountAuthService accountAuthService;
    private boolean isGame;

    public HuaWeiAuthorization(Activity activity, LoginListener listener) {
        super(activity, listener);
        authParams = new AccountAuthParamsHelper(isGame ? AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME : AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAuthorizationCode()
                .setId()
                .setAccessToken()
                .createParams();
        accountAuthService = AccountAuthManager.getService(activity, authParams);
    }

    @Override
    public void login() {
        App.getApp().startActivityForResult(0, accountAuthService.getSignInIntent(), new ResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode != 0) {
                    return;
                }
                if (resultCode != Activity.RESULT_OK) {
                    listener.onLoginFailure();
                    return;
                }
                Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
                if (authAccountTask.isSuccessful()) {
                    AuthAccount account = authAccountTask.getResult();
                    Log.e("=====",account.toString());
                    User user = new User();
                    user.id = account.getOpenId();
                    user.email = account.getEmail();
                    user.token = account.getAccessToken();
//                    user.uid = account.getUid();
                    user.code = account.getAuthorizationCode();
                    user.name = account.getDisplayName();
                    Log.e("=====",user.toString());
                    listener.onLoginSucceed(user);
                    return;
                }
                listener.onLoginFailure();
            }
        });
    }
}
