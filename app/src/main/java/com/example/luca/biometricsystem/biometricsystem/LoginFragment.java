package com.example.luca.biometricsystem.biometricsystem;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.luca.biometricsystem.Persona;
import com.example.luca.biometricsystem.SignUpIn;
import com.example.luca.biometricsystem.R;
import com.google.android.material.textfield.TextInputLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
        inflate.findViewById(R.id.forgot_password).setOnClickListener(v ->
                Toast.makeText(getContext(), "Forgot password clicked", Toast.LENGTH_SHORT).show());
        return inflate;
    }

    @Override
    public void login() {
        SignUpIn signUpIn = new SignUpIn(getActivity().findViewById(R.id.email_login), getActivity().findViewById(R.id.password_login));
        //signUpIn.setActivity(getActivity());
        signUpIn.signIn(getActivity());
        //parseEmailPassword.changeActivity(getActivity());
    }
}
