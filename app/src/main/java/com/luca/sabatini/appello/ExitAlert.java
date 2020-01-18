package com.luca.sabatini.appello;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.ui.courseList.CourseListFragment;
import com.luca.sabatini.appello.ui.courseList.RemoveAlert;
import com.luca.sabatini.appello.utils.SharedPrefManager;

public class ExitAlert extends DialogFragment {
    private static final String TAG = "ExitAlert";
    private String title;
    private Context context;
    private Activity activity;
    private String namePositiveButton;
    private String nameNegativeButton;
    private String message;
    private SharedPrefManager sp;

    public ExitAlert(Context context, Activity activity){
        title = "Closing the application";
        message = "Are you sure you want to close the application?";
        nameNegativeButton = "No";
        namePositiveButton = "Yes";
        this.context = context;
        sp = new SharedPrefManager(this.context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.alert_dialog_custom);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_exit_dialog, null);
        builder.setView(view)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckBox checkBox = view.findViewById(R.id.skip);
                        if (checkBox.isChecked()) {
                            sp.writeExitIsChecked(true);
                        }
                        activity.finishAffinity();
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CheckBox checkBox = view.findViewById(R.id.skip);
                        if (checkBox.isChecked()) {
                            sp.writeExitIsChecked(true);
                        }
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }
}
