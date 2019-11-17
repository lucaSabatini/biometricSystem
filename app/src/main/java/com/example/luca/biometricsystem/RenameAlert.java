package com.example.luca.biometricsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.luca.biometricsystem.list.ListaCorsi;
import com.example.luca.biometricsystem.login.ProvaAlert;

public class RenameAlert extends DialogFragment {
    private static final String TAG ="ChangeNameAlert";

    private String title;
    private int position;
    private String namePositiveButton;
    private String nameNegativeButton;
    private ProvaAlert.ProvaAlertListener listener;
    private ListaCorsi listaCorsi;

    public RenameAlert(ListaCorsi listaCorsi, int position){
        title = "Rinomina corso";
        nameNegativeButton = "Annulla";
        namePositiveButton = "Rinomina";
        this.listaCorsi = listaCorsi;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_rename_corso, null);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: ciao");
                        EditText nomeCorso = view.findViewById(R.id.edit_text_nome_corso);
                        if(nomeCorso.getText().toString().equals("")) return;
                        listaCorsi.changeNameItem(position, nomeCorso.getText().toString().trim());
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                    }
                });
        AlertDialog alertDialog = builder.create();


        return alertDialog;
    }
}
