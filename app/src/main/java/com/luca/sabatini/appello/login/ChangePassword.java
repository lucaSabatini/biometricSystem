package com.luca.sabatini.appello.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.utils.SharedPrefManager;

public class ChangePassword extends AppCompatActivity {

    private TextInputLayout currentPasswordInputLayout;
    private TextInputLayout newPasswordInputLayout;
    private TextInputLayout newPasswordAgainInputLayout;
    private SharedPrefManager sp;
    private Button changePasswordButton;
    private static final String TAG = "ChangePassword";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPasswordInputLayout = findViewById(R.id.current_password_input_layout);
        newPasswordInputLayout = findViewById(R.id.new_password_input_layout);
        newPasswordAgainInputLayout = findViewById(R.id.new_password_again_input_layout);
        changePasswordButton = findViewById(R.id.change_password_button);
        sp = new SharedPrefManager(getApplicationContext());
    }

    public void changePasswordButton(View view){
        if (!confermaInput()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String oldPassword = currentPasswordInputLayout.getEditText().getText().toString();
            String newPassword = newPasswordInputLayout.getEditText().getText().toString();
            String newPasswordAgain = newPasswordAgainInputLayout.getEditText().getText().toString();
            if (user != null && checkNewPassword(newPassword, newPasswordAgain)) {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            if (oldPassword.length() < 6) {
                                currentPasswordInputLayout.setError("Password too short, enter minimum 6 characters!");
                            }else if(newPassword.length() < 6){
                                newPasswordInputLayout.setError("Password too short, enter minimum 6 characters!");

                            }else if( newPasswordAgain.length() < 6){
                                newPasswordAgainInputLayout.setError("Password too short, enter minimum 6 characters!");
                            }else {
                                Toast.makeText( getApplicationContext(), "password incorrect", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "onClick:failure", task.getException());
                            }
                        }else{
                            Toast.makeText( getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
                            sp.resetSharedPref();
                            finishAffinity();
                            startActivity(new Intent( getApplicationContext(), LoginActivity.class));
                        }
                    }
                });
            }else if(!checkNewPassword(newPassword, newPasswordAgain)){
                newPasswordInputLayout.setError("Password different!");
                newPasswordAgainInputLayout.setError("Password different!");

            }

        }

    }

    private boolean validateOldPassword(){
        String passwordValue = currentPasswordInputLayout.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            currentPasswordInputLayout.setError("Field can't be empty");
            return false;
        }else{
            currentPasswordInputLayout.setError(null);
            return true;
        }
    }

    private boolean validateNewPassword(){
        String passwordValue = newPasswordInputLayout.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            newPasswordInputLayout.setError("Field can't be empty");
            return false;
        }else{
            newPasswordInputLayout.setError(null);
            return true;
        }
    }
    private boolean validateNewPasswordAgain(){
        String passwordValue = newPasswordAgainInputLayout.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            newPasswordAgainInputLayout.setError("Field can't be empty");
            return false;
        }else{
            newPasswordAgainInputLayout.setError(null);
            return true;
        }
    }


    public Boolean confermaInput(){
        return (!validateOldPassword() | !validateNewPasswordAgain() | !validateNewPassword());
    }

    private boolean checkNewPassword(String passWord1, String password2){
        return passWord1.equals(password2);
    }
}
