package com.lhl.playsdk;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

//import com.lhl.playsdk.databinding.ActivityMainBinding;
import com.lhl.App;
import com.relax.playgame.sdk.databinding.ActivityMainBinding;

public class MainActivity extends FragmentActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        activityMainBinding.setActivity(this);
    }
    public void login(){
        App.getApp().facebookLogin((uid)->{
            Log.e("=====",uid.toString());
        });
    }
}