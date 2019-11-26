package com.example.luca.biometricsystem.list;

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

import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.entities.Corso;
import com.example.luca.biometricsystem.list.ListaCorsi;

public class RenameAlert extends DialogFragment {
    private static final String TAG ="ChangeNameAlert";

    private String title;
    private int position;
    private String namePositiveButton;
    private String nameNegativeButton;
    private ListaCorsi listaCorsi;
    private Corso c;

    public RenameAlert(ListaCorsi listaCorsi, Corso c){
        title = "Rinomina corso";
        nameNegativeButton = "Annulla";
        namePositiveButton = "Rinomina";
        this.listaCorsi = listaCorsi;
        this.position = position;
        this.c = c;
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
                        listaCorsi.changeNameItem(nomeCorso.getText().toString(), c.year, c.id);
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
