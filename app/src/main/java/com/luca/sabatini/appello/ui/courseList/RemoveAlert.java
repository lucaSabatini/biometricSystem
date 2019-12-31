package com.luca.sabatini.appello.ui.courseList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;

public class RemoveAlert extends DialogFragment {

    private static final String TAG = "RemoveAlert";
    private String title;
    private Corso c;
    private String namePositiveButton;
    private String nameNegativeButton;
    private String message;
    private CourseListFragment listaCorsi;
    private RemoveAlertListener listener;

    public RemoveAlert(CourseListFragment listaCorsi, Corso c){
        title = "Delete course";
        message = "Are you sure to delete the course?";
        nameNegativeButton = "No";
        namePositiveButton = "Yes";
        this.listaCorsi = listaCorsi;
        Log.d(TAG, "RemoveAlert: "+ c);
        this.c = c;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_custom);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_remove_corso, null);
        builder.setView(view)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: Rimouvi");
                        listaCorsi.removeItemFromMap(c);
                        //listaCorsi.removeItem(position);
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                        //listener.removeCorso(true);
                        //swipeToDelete.undoDelete();
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
