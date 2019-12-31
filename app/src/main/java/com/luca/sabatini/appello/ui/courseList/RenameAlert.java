package com.luca.sabatini.appello.ui.courseList;

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

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;

public class RenameAlert extends DialogFragment {
    private static final String TAG ="ChangeNameAlert";

    private String title;
    private int position;
    private String namePositiveButton;
    private String nameNegativeButton;
    private CourseListFragment listaCorsi;
    private Corso c;

    public RenameAlert(CourseListFragment listaCorsi, Corso c){
        title = "Rename course";
        nameNegativeButton = "Cancel";
        namePositiveButton = "Rename";
        this.listaCorsi = listaCorsi;
        this.position = position;
        this.c = c;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_custom);
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
