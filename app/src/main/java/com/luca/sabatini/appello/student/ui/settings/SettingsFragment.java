package com.luca.sabatini.appello.student.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Persona;


public class SettingsFragment extends Fragment {

    private SettingsViewModel logoutViewModel;
    private Button aboutButton;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Persona persona = new Persona(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
        if(persona.isStudent()){
            FloatingActionButton fab = getActivity().findViewById(R.id.fab1);
            fab.hide();
        }

        logoutViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        aboutButton = root.findViewById(R.id.about_button);
        logoutButton = root.findViewById(R.id.logout_button);
        final TextView textView = root.findViewById(R.id.text_logout);
        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about(view);
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
                Signout signout = new Signout();
                signout.show(getFragmentManager(),"signout");
            }
        });

        return root;
    }

    public void logout(View view){
        Toast.makeText(getActivity(), "click logout", Toast.LENGTH_SHORT).show();
    }

    public void about(View view){
        Toast.makeText(getActivity(), "click about", Toast.LENGTH_SHORT).show();

    }
}