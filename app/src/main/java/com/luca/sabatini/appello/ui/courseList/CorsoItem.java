package com.luca.sabatini.appello.ui.courseList;

import androidx.annotation.NonNull;

import com.luca.sabatini.appello.entities.Corso;

public class CorsoItem extends ListItem{

    private static final String TAG = "ListItem";
    private int imageResource;
    private Corso corso;

    public CorsoItem(Corso corso, int imageResource){
        this.corso = corso;
        this.imageResource = imageResource;
    }

    public int getImageResource(){
        return imageResource;
    }

    public String getNomeCorso(){
        return corso.name;
    }

    public void setNomeCorso(String nomeCorso){
        corso.name = nomeCorso;
    }

    @NonNull
    @Override
    public String toString() {
        return getNomeCorso();
    }

    @Override
    public int getType() {
        return TYPE_COURSE;
    }
}
