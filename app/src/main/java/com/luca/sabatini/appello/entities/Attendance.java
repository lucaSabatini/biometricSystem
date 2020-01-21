package com.luca.sabatini.appello.entities;

public class Attendance {
    private Long sessionId;
    private String courseId;
    private String matricola;
    private Long timestamp;
    private String date;

    private Attendance(){}

    public Attendance(Long sessionId, String courseId, String matricola, Long timestamp, String date) {
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.matricola = matricola;
        this.timestamp = timestamp;
        this.date = date;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
