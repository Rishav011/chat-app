package com.example.fireapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class fireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
