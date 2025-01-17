package com.luca.sabatini.appello.student.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.login.LoginActivity;
import com.luca.sabatini.appello.utils.SharedPrefManager;

public class Signout extends DialogFragment{
    private final static String TAG = "Signout";
    private SharedPrefManager sp;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_custom);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_remove_corso, null);
        sp = new SharedPrefManager(getContext());

        builder.setView(view)
                .setTitle("Logout")
                .setMessage("Are you sure to logout?")
                .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.resetSharedPref();
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                })
                .setNegativeButton("ABORT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                    }
                });
        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }
}
