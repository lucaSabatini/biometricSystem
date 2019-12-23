package com.luca.sabatini.appello.student.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Persona persona = new Persona(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView lastNameTextView = root.findViewById(R.id.text_profile_name);
        TextView studentIdTextView = root.findViewById(R.id.text_profile_student_id);
        TextView emailTextView = root.findViewById(R.id.text_profile_email);
        if(persona.isStudent()){
            FloatingActionButton fab = getActivity().findViewById(R.id.fab1);
            fab.show();
            studentIdTextView.setVisibility(View.VISIBLE);
            lastNameTextView.setText(persona.getLastName());
            studentIdTextView.setText(persona.getStudentId());
            emailTextView.setText(persona.getEmail());
        }
        else{
            lastNameTextView.setText(persona.getLastName());
            emailTextView.setText(persona.getEmail());
        }


        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), persona.getEmail() ,Toast.LENGTH_LONG ).show();
            }
        });
        return root;
    }
}