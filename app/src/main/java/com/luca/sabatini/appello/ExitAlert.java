package com.luca.sabatini.appello;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.ui.courseList.CourseListFragment;
import com.luca.sabatini.appello.ui.courseList.RemoveAlert;

public class ExitAlert extends DialogFragment {
    private static final String TAG = "ExitAlert";
    private String title;
    private Context context;
    private Activity activity;
    private String namePositiveButton;
    private String nameNegativeButton;
    private String message;

    public ExitAlert(Context context, Activity activity){
        title = "Closing the application";
        message = "Are you sure you want to close the application?";
        nameNegativeButton = "No";
        namePositiveButton = "Yes";
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.alert_dialog_custom);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_remove_corso, null);
        builder.setView(view)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finishAffinity();
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }
}
