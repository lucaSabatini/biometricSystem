package com.example.luca.biometricsystem.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

    /*public void sendEmail(View view){
        LoginIntroFragment forgotPassword = new LoginIntroFragment();
        forgotPassword.forgotPassword(this, email.getEditText().getText().toString().trim());
    }*/
}