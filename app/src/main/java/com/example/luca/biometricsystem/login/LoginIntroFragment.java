package com.example.luca.biometricsystem.login;

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

import com.example.luca.biometricsystem.Appello;
import com.example.luca.biometricsystem.CameraActivity;
import com.example.luca.biometricsystem.RegistrazioneFoto;
import com.example.luca.biometricsystem.entities.Persona;
import com.example.luca.biometricsystem.list.ListaCorsi;
import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.utils.SharedPrefManager;
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
    private Button login;
    private TextInputLayout username;
    private TextInputLayout password;
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
        //login = inflate.findViewById(R.id.login_button);
        signInOrCreate = inflate.findViewById(R.id.login_button);
        password = inflate.findViewById(R.id.password);
        username = inflate.findViewById(R.id.username);

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
        String emailValue = username.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if(!emailValue.matches(emailPattern)){
            clear();
            username.setError("invalid e-mail");
            return false;
        } else{
            username.setError(null);
            return true;
        }
    }


    private boolean validatePassword(){
        String passwordValue = password.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            password.setError("Field can't be empty");
            return false;
        }else{
            password.setError(null);
            return true;
        }
    }

    private void clear(){
        username.getEditText().getText().clear();
        password.getEditText().getText().clear();
    }

    public Boolean confermaInput(){
        return (!validateEmail() | !validatePassword());
    }


    public void loginSignInButton(){
        if(!confermaInput()) {
            signInProgressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(username.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            signInProgressBar.setVisibility(View.INVISIBLE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.getEditText().getText().length() < 6) {
                                    password.setError("Password too short, enter minimum 6 characters!");
                                }else {
                                    Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                }
                            }/*else if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(context, "e-mail is not verified", Toast.LENGTH_LONG).show();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                clear();
                            }*/ else {
                                // se studente andare su activity per confermare presenza
                                // altrimenti andare su ListaCorsi
                                Persona persona = new Persona(username.getEditText().getText().toString().trim());
                                sp.writeString("uid", firebaseAuth.getCurrentUser().getUid());
                                Log.d(TAG, "onComplete: provola !");
                                Log.d(TAG, "onComplete: " + firebaseAuth.getCurrentUser().getUid());
                                if(persona.isStudent()){
                                    Intent intent = new Intent(context, CameraActivity.class);
                                    Toast.makeText( context, "Login", Toast.LENGTH_SHORT).show();
                                    intent.putExtra(EXTRA_ACTION,"login");
                                    context.startActivity(intent);
                                }else{
                                    Toast.makeText( context, "Login", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context, ListaCorsi.class));
                                }

                            }
                        }
                    });
        }
        else{
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        signInProgressBar.setVisibility(View.INVISIBLE);
        if (task.isSuccessful()) {
            startActivity(new Intent(context, ListaCorsi.class));
        } else {
            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInWithEmail:failure", task.getException());
        }
    }*/

}