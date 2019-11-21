package com.example.luca.biometricsystem;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BiometricSystem extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmConfiguration config = new RealmConfiguration.Builder().build();
    }
}
