package com.example.luca.biometricsystem;

public class CorsoItem {
    private int imageResource;
    private String nomeCorso;

    public CorsoItem(int imageResource, String nomeCorso){
        this.imageResource = imageResource;
        this.nomeCorso = nomeCorso;
    }

    public void changeText1(String text){
        nomeCorso = text;
    }

    public int getImageResource(){
        return imageResource;
    }

    public String getNomeCorso(){
        return nomeCorso;
    }
}
