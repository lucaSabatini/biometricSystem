package com.luca.sabatini.appello.entities;

public class CheckSessionResponse {
    private Long sessionId;
    private String professorId;
    private String courseId;
    private String courseName;
    private String registrationId;

    public Long getSessionId() { return sessionId; }

    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
