package com.example.luca.biometricsystem.list;

import androidx.annotation.NonNull;

import com.example.luca.biometricsystem.entities.Corso;

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
        return corso.getNomeCorso();
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
