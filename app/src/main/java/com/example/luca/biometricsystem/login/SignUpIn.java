package com.example.luca.biometricsystem.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.luca.biometricsystem.Appello;
import com.example.luca.biometricsystem.ListaCorsi;
import com.example.luca.biometricsystem.entities.Persona;
import com.example.luca.biometricsystem.RegistrazioneFoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpIn {
    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";
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

    public SignUpIn(TextInputLayout email){
        this.email = email;
        auth = FirebaseAuth.getInstance();
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    private boolean validateEmail(){
        Log.i("FORGOT", "validate email");
        emailValue = email.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if(!emailValue.matches(emailPattern)){
            clearEmail();
            email.setError("invalid e-mail");
            return false;
        } else{
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

    private void clearEmail(){
        email.getEditText().getText().clear();
    }

    private void clearPassword(){
        password.getEditText().getText().clear();
    }

    private void clear(){
        clearEmail();
        clearPassword();
    }

    public Boolean confermaInput(){
        return (!validateEmail() | !validatePassword());
    }

    public void signIn(Activity activity){
        if(confermaInput()) return;
        //TextInputLayout textInputLayout = activity.findViewById(R.id.email_login);
        Persona persona = new Persona(emailValue);
        Log.i("LOGIN", "email= "+persona.getEmail() );
        Log.i("LOGIN", "lastName= "+persona.getLastName());
        Log.i("LOGIN", "studentId= "+persona.getStudentId() );
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
                            Intent intent;
                            if(persona.isStudent()){
                                intent = new Intent(activity, Appello.class);
                                intent.putExtra("Persona",persona);
                            }else{
                                intent = new Intent(activity, ListaCorsi.class);
                                intent.putExtra("Persona", persona);
                            }

                            Toast.makeText(activity, "Login", Toast.LENGTH_SHORT).show();
                            activity.startActivity(intent);
                        }
                    }
                });
    }

    public void signUpUser(Activity activity){
        if(confermaInput())return;
        auth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(activity, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()) {
                    Toast.makeText( activity , "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    auth.getCurrentUser().sendEmailVerification();
                    Toast.makeText(activity, "Sign up", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent( activity , RegistrazioneFoto.class));
                }
            }
        });
    }

    public void forgotPassword(Activity activity){
        if(!validateEmail()) return;
        auth.sendPasswordResetEmail(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changeActivity(Activity activity){
        Intent foto = new Intent(activity, RegistrazioneFoto.class);
        clear();
        activity.startActivity(foto);
    }


}
