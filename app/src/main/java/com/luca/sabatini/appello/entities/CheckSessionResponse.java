package com.luca.sabatini.appello.entities;

public class CheckSessionResponse {
    private String professorId;
    private String nomeCorso;
    private String corsoId;

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getNomeCorso() {
        return nomeCorso;
    }

    public void setNomeCorso(String nomeCorso) {
        this.nomeCorso = nomeCorso;
    }

    public String getCorsoId() {
        return corsoId;
    }

    public void setCorsoId(String corsoId) {
        this.corsoId = corsoId;
    }
}
