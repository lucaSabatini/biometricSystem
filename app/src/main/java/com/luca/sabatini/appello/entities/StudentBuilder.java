package com.luca.sabatini.appello.entities;

public class StudentBuilder {
    private String firebaseId;
    private Long matricola;
    private String surname;
    private String photo;

    public StudentBuilder setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
        return this;
    }

    public StudentBuilder setMatricola(Long matricola) {
        this.matricola = matricola;
        return this;
    }

    public StudentBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public StudentBuilder setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Student createStudent() {
        return new Student(firebaseId, matricola, surname, photo);
    }
}