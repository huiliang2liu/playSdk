package com.lhl.listener;

import com.lhl.model.User;

public interface LoginListener {
    void onLoginSucceed(User user);
   default void onLoginFailure(){

   }
}
