package com.example.luca.biometricsystem.biometricsystem;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luca.biometricsystem.Appello;
import com.example.luca.biometricsystem.ParseEmailPassword;
import com.example.luca.biometricsystem.R;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class SignUpFragment extends Fragment implements OnSignUpListener{
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
        ParseEmailPassword parseEmailPassword = new ParseEmailPassword(getActivity().findViewById(R.id.email), getActivity().findViewById(R.id.password));
        if(parseEmailPassword.confermaInput()){
            return;
        }
        parseEmailPassword.setActivity(getActivity());
        parseEmailPassword.signUpUser();
        //parseEmailPassword.changeActivity(getActivity());
        Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
}
