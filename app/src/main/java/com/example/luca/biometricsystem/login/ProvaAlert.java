package com.example.luca.biometricsystem.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.luca.biometricsystem.R;


public class ProvaAlert extends DialogFragment {

    private EditText editText;
    private AppCompatActivity activity;
    private String nomeCorso;
    private String title;
    private String namePositiveButton;
    private String nameNegativeButton;
    private ProvaAlertListener listener;

    public ProvaAlert(AppCompatActivity activity, String title, String namePositiveButton, String nameNegativeButton){
        this.activity = activity;
        this.title = title;
        this.nameNegativeButton = nameNegativeButton;
        this.namePositiveButton = namePositiveButton;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.color_button);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_add_corso, null);
        builder.setView(view)
                .setTitle(title)
        //builder.setMessage(Html.fromHtml(title))
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: ciao");
                        EditText nomeCorso = view.findViewById(R.id.edit_text_nome_corso);
                        listener.getText(nomeCorso.getText().toString());
                        //setNomeCorso(nomeCorso.getText().toString());
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                    }
                });
                //.setView(layoutInflater.inflate(R.layout.popup_add_corso, null));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (ProvaAlertListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "implement ProvaAlertListener");
        }
    }

    public interface ProvaAlertListener{
        void getText(String nomeCorso);
    }

}
