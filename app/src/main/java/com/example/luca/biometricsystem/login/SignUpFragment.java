package com.example.luca.biometricsystem.login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.biometricsystem.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment implements OnSignUpListener {
    private static final String TAG = "SignUpFragment";
    private TextInputLayout email;
    private String emailValue;
    private TextInputLayout password;
    private String passwordValue;

    public SignUpFragment() {
        // Required empty public constructor
    }


    private void setEmail(TextInputLayout email){
        this.email = email;
    }

    private void setPassword(TextInputLayout password){
        this.password = password;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);


        return inflate;
    }

    @Override
    public void signUp() {
        SignUpIn signUpIn = new SignUpIn(getActivity().findViewById(R.id.email), getActivity().findViewById(R.id.password));
        signUpIn.signUpUser(getActivity());
        //parseEmailPassword.changeActivity(getActivity());
    }
}
