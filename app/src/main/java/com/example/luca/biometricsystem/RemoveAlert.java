package com.example.luca.biometricsystem;

import android.app.Dialog;
import android.content.Context;
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

import com.example.luca.biometricsystem.login.ProvaAlert;

public class RemoveAlert extends DialogFragment {

    private static final String TAG = "RemoveAlert";
    private String title;
    private String namePositiveButton;
    private String nameNegativeButton;
    private String message;
    private SwipeToDelete swipeToDelete;
    private RemoveAlertListener listener;

    public RemoveAlert(SwipeToDelete swipeToDelete){
        title = "Rimuovi corso";
        message = "Sei sicuro di rimuovere il corso?";
        nameNegativeButton = "No";
        namePositiveButton = "Si";
        this.swipeToDelete = swipeToDelete;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_remove_corso, null);
        builder.setView(view)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: Rimouvi");
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                        //listener.removeCorso(true);
                        swipeToDelete.undoDelete();
                        dialogInterface.cancel();
                    }
                });
        //.setView(layoutInflater.inflate(R.layout.popup_add_corso, null));
        return builder.create();
    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (RemoveAlertListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "implement RemoveAlert");
        }
    }*/

    public interface RemoveAlertListener{
        void removeCorso(boolean undo);
    }
}
