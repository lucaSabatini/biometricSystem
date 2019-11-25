package com.example.luca.biometricsystem.entities;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Corso extends RealmObject {

    //private static final String TAG = "Corso";
    @PrimaryKey
    public Long id;
    public String name;
    public String uid;
    public Long year;

    public Corso(){}

    @NonNull
    @Override
    public String toString() {
        return String.format("name:%s year: %s uid: %s", name, year, uid);
    }

}
