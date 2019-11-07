package com.example.luca.biometricsystem.entities;

public class Corso {

    private String nomeCorso;
    public  Corso(String nomeCorso){
        this.nomeCorso = nomeCorso;
    }

    public String getNomeCorso() {
        return nomeCorso;
    }

    public void setNomeCorso(String nomeCorso) {
        this.nomeCorso = nomeCorso;
    }
}
