package com.luca.sabatini.appello.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student extends RealmObject {
    @PrimaryKey
    public String studentId;
    public Long matricola;
    public String surname;
    public String photo;

    public Student(){}

    public Student(String studentId, Long matricola, String surname, String photo) {
        this.studentId = studentId;
        this.matricola = matricola;
        this.surname = surname;
        this.photo = photo;
    }
}
