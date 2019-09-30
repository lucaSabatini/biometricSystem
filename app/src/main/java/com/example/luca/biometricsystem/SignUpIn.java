package com.example.luca.biometricsystem;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpIn {
    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextInputLayout email;
    private TextInputLayout password;
    private String emailValue;
    private String passwordValue;
    private Activity activity;
    private FirebaseAuth auth;

    public SignUpIn(TextInputLayout email, TextInputLayout password){
        this.email = email;
        this.password = password;
        auth = FirebaseAuth.getInstance();
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    private boolean validateEmail(){
        emailValue = email.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } /*else if(!emailValue.matches(emailPattern)){
            clear();
            email.setError("invalid e-mail");
            return false;
        }*/ else{
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        passwordValue = password.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        }else{
            password.setError(null);
            return true;
        }
    }

    private void clear(){
        email.getEditText().getText().clear();
        password.getEditText().getText().clear();
    }

    public Boolean confermaInput(){
        return (!validateEmail() | !validatePassword());
    }

    public void signIn(){
        auth.signInWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener(activity , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (passwordValue.length() < 6) {
                                password.setError("Password too short, enter minimum 6 characters!");
                            }else {
                                Toast.makeText(activity, "Authentication failed, sign up", Toast.LENGTH_LONG).show();
                            }
                        }else if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(activity, "e-mail is not verified", Toast.LENGTH_LONG).show();
                            clear();
                        } else {
                            Intent intent = new Intent(activity, RegistrazioneFoto.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }
                });
    }

    public void signUpUser(){
        auth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(activity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()) {
                    Toast.makeText( activity , "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    auth.getCurrentUser().sendEmailVerification();
                    activity.startActivity(new Intent( activity , RegistrazioneFoto.class));
                    activity.finish();
                }
            }
        });
    }

    public void changeActivity(Activity activity){
        Intent foto = new Intent(activity, RegistrazioneFoto.class);
        clear();
        activity.startActivity(foto);
    }

    public void takeLastName(){

    }

    public void takeStudentiId(){

    }


}
