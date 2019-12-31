package com.luca.sabatini.appello.entities;

public class CheckSessionResponse {
    private String professorId;
    private String corsoId;
    private String corsoName;
    private String registrationId;

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getNomeCorso() {
        return corsoName;
    }

    public void setNomeCorso(String corsoName) {
        this.corsoName = corsoName;
    }

    public String getCorsoId() {
        return corsoId;
    }

    public void setCorsoId(String corsoId) {
        this.corsoId = corsoId;
    }

    public String getCorsoName() {
        return corsoName;
    }

    public void setCorsoName(String corsoName) {
        this.corsoName = corsoName;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
