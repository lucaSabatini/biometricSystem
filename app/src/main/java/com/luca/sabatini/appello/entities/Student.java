package com.luca.sabatini.appello.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student extends RealmObject {
    @PrimaryKey
    public String firebaseId;
    public Long matricola;
    public String surname;
    public String photo;

    public Student(){}

    public Student(String firebaseId, Long matricola, String surname, String photo) {
        this.firebaseId = firebaseId;
        this.matricola = matricola;
        this.surname = surname;
        this.photo = photo;
    }
}
