package com.luca.sabatini.appello.entities;

public class StudentBuilder {
    private String studentId;
    private Long matricola;
    private String surname;
    private String photo;

    public StudentBuilder setStudentId(String studentId) {
        this.studentId = studentId;
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
        return new Student(studentId, matricola, surname, photo);
    }
}