package com.luca.sabatini.appello.ui.listaCorsi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListaCorsiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListaCorsiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}