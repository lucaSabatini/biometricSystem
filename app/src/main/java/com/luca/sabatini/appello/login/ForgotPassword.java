package com.luca.sabatini.appello.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.R;

public class ForgotPassword extends AppCompatActivity {
    private TextInputLayout email;
    private final static String TAG = "ForgotPassword";
    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email_forgot_password);
    }

    public void sendEmail(View view){
        if(!validateEmail()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            finishAffinity();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }
                });
    }

    private boolean validateEmail(){
        String emailValue = email.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if(!emailValue.matches(emailPattern)){
            email.getEditText().getText().clear();
            email.setError("invalid e-mail");
            return false;
        } else{
            email.setError(null);
            return true;
        }
    }



}
