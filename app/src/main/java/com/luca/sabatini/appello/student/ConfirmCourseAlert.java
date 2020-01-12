package com.luca.sabatini.appello.student;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.ui.courseList.ListaCorsi;

import static com.luca.sabatini.appello.student.CameraActivity.EXTRA_ACTION;

public class ConfirmCourseAlert extends DialogFragment {

    private static final String TAG = "ConfirmCourseAlert";
    private String title;
    private String namePositiveButton;
    private String nameNegativeButton;
    private String message;

    public ConfirmCourseAlert(String corso){
        title = "Attendance session found for course: " + corso;
        message = "Starting biometric recognition?";
        nameNegativeButton = "No";
        namePositiveButton = "Yes";
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
                        Intent intent = new Intent(getContext(), CameraActivity.class);
                        intent.putExtra(EXTRA_ACTION, "verification");
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                        dialogInterface.cancel();
                        ((Activity) getContext()).finish();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.KEYCODE_BACK) {
                    Log.d("alert", "onClick: annulla");
                    dialogInterface.cancel();
                    ((Activity) getContext()).finish();
                }
                return true;
            }
        });
        return dialog;
    }

}
