package com.lhl.playsdk;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.lhl.playsdk.databinding.ActivityMainBinding;
import com.lhl.App;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import com.relax.playgame.sdk.databinding.ActivityMainBinding;

public class MainActivity extends FragmentActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        activityMainBinding.setActivity(this);
    }

    public void login() {
        App.getApp().googleLogin((uid) -> {
            Log.e("=====", uid.toString());
        });
        new Thread() {
            @Override
            public void run() {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(8, TimeUnit.SECONDS)
                        .readTimeout(8, TimeUnit.SECONDS)
                        .writeTimeout(8, TimeUnit.SECONDS)
                        .addInterceptor(logging);
                Test test = new Retrofit.Builder()
                        .baseUrl("https://pay.imobybox.com")
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(Test.class);
                Test.ReqAppEnter enter = new Test.ReqAppEnter();
                try {
                    Log.e("======", test.appEnter(enter).execute().body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}