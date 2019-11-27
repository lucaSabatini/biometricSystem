package com.luca.sabatini.appello;

import android.app.Application;

import io.realm.Realm;

public class Appello extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmConfiguration config = new RealmConfiguration.Builder().build();
    }
}
