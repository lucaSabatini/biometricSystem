package com.example.luca.biometricsystem.entities;

import androidx.annotation.NonNull;

import io.realm.RealmObject;

public class Corso extends RealmObject {

    //private static final String TAG = "Corso";
    public String name;
    public String uid;
    public Long year;

    public Corso(){}

    public Corso( String name){
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("name:%s year: %s uid: %s", name, year, uid);
    }

}
