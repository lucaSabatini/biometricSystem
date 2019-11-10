package com.example.luca.biometricsystem.entities;

import androidx.annotation.NonNull;

public class Corso {

    private static final String TAG = "Corso";
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

    @NonNull
    @Override
    public String toString() {
        return nomeCorso;
    }

}
