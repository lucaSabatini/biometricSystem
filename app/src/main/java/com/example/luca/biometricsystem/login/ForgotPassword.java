package com.example.luca.biometricsystem.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.luca.biometricsystem.R;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email_forgot_password);
    }

    public void sendEmail(View view){
        SignUpIn forgotPassword = new SignUpIn(email);
        forgotPassword.forgotPassword(this);
    }
}
