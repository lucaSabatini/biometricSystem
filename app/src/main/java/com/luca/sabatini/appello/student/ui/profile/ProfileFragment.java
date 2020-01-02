package com.luca.sabatini.appello.student.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luca.sabatini.appello.ExitAlert;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.utils.SharedPrefManager;


public class ProfileFragment extends Fragment {

    private final static String TAG ="ProfileFragment";
    private ProfileViewModel profileViewModel;
    private SharedPrefManager sp;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Persona persona = new Persona(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = new SharedPrefManager(root.getContext());
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ExitAlert exitAlert = new ExitAlert(root.getContext(),getActivity());
                exitAlert.show(getFragmentManager(), "ExitAlert");
            }
        };
        //requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        TextView lastNameTextView = root.findViewById(R.id.text_profile_name);
        TextView studentIdTextView = root.findViewById(R.id.text_profile_student_id);
        TextView emailTextView = root.findViewById(R.id.text_profile_email);
        if(isStudent()){
            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
            String matricola = sp.readMatricola().toString();
            String surname = sp.readSurname();
            FloatingActionButton fab = getActivity().findViewById(R.id.fab1);
            fab.show();
            studentIdTextView.setVisibility(View.VISIBLE);
            lastNameTextView.setText(surname);
            studentIdTextView.setText(matricola);
            surname = surname + "." + matricola + "@studenti.uniroma1.it";
            emailTextView.setText(surname);
        }
        else{
            String surname = sp.readSurname();
            lastNameTextView.setText(surname);
            surname = surname + "@di.uniroma1.it";
            emailTextView.setText(surname);
        }


        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), emailTextView.getText().toString() ,Toast.LENGTH_LONG ).show();
            }
        });
        return root;
    }



    public boolean isStudent() {
        return sp.readMatricola() != -1;
    }
}