package com.luca.sabatini.appello.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.list.ListaCorsi;
import com.luca.sabatini.appello.student.ProfiloUtente;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginIntroFragment extends Fragment {

    private static final String TAG = "LoginIntroFragment";
    public final static String EXTRA_ACTION = "com.example.luca.biometricsystem.logingeneroso";
    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";
    private TextView newAccount;
    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
    private Button signInOrCreate;
    private ProgressBar signInProgressBar;

    private LoginRoutingInterface callback;
    private Context context;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SharedPrefManager sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.login_intro_fragment, container, false);
        signInProgressBar = inflate.findViewById(R.id.sign_in_progress_bar);
        newAccount = inflate.findViewById(R.id.login_new_account);
        signInOrCreate = inflate.findViewById(R.id.login_button);
        passwordTextInput = inflate.findViewById(R.id.password);
        usernameTextInput = inflate.findViewById(R.id.username);

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNewAccount();
            }
        });

        signInOrCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignInButton();
            }
        });

        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginRoutingInterface) {
            this.callback = (LoginRoutingInterface) context;
        }
        else {
            this.callback = null;
        }
        this.context = context;
        sp = new SharedPrefManager(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
        this.context = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void loginNewAccount(){
        if(callback != null)
            callback.route("new_account");
    }

    private boolean validateEmail(){
        String emailValue = usernameTextInput.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            usernameTextInput.setError("Field can't be empty");
            return false;
        } else if(!emailValue.matches(emailPattern)){
            clear();
            usernameTextInput.setError("invalid e-mail");
            return false;
        } else{
            usernameTextInput.setError(null);
            return true;
        }
    }


    private boolean validatePassword(){
        String passwordValue = passwordTextInput.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            passwordTextInput.setError("Field can't be empty");
            return false;
        }else{
            passwordTextInput.setError(null);
            return true;
        }
    }

    private void clear(){
        usernameTextInput.getEditText().getText().clear();
        passwordTextInput.getEditText().getText().clear();
    }

    public Boolean isInputBad(){
        return (!validateEmail() | !validatePassword());
    }

    public boolean isStudent() {
        return sp.readMatricola() != -1;
    }

    public void loginSignInButton(){
        if(isInputBad()) {
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
            return;
        }
        signInProgressBar.setVisibility(View.VISIBLE);
        String username = usernameTextInput.getEditText().getText().toString().trim();
        String password = passwordTextInput.getEditText().getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signInProgressBar.setVisibility(View.INVISIBLE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }/*
                        else if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(context, "e-mail is not verified", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            clear();
                        }*/
                        else {
                            String emailBeforeAt = username.split("@")[0];
                            if (emailBeforeAt.contains(".")) {
                                Log.d(TAG, "setLastNameAndStudentId: login studente");
                                sp.writeMatricola(Long.parseLong(emailBeforeAt.split("\\.")[1]));
                            }
                            sp.writeSurname(emailBeforeAt.split("\\.")[0]);
                            sp.writeFirebaseId(firebaseAuth.getCurrentUser().getUid());

                            if(isStudent()){
                                context.startActivity(new Intent(context, ProfiloUtente.class));
                            }else{
                                context.startActivity(new Intent(context, ListaCorsi.class));
                            }
                        }
                    }
                });
    }

}
