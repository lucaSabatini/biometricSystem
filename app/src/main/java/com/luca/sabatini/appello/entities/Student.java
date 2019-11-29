package com.luca.sabatini.appello.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student extends RealmObject {
    @PrimaryKey
    public String studentId;
    public Long matricola;
    public String surname;
}
