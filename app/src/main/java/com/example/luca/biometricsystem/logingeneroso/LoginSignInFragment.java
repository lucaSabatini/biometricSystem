package com.example.luca.biometricsystem.logingeneroso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.luca.biometricsystem.list.ListaCorsi;
import com.example.luca.biometricsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginSignInFragment extends Fragment implements OnCompleteListener<AuthResult> {

    private static final String TAG = "LoginSignInFragment";
    private LoginRoutingInterface callback;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Context context;
    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";


    private String action;
    private TextInputLayout username;
    private TextInputLayout password;
    private Button signInOrCreate;
    private TextView signInTextView;
    private ProgressBar signUpProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.login_signin_fragment, container, false);
        signUpProgressBar = inflate.findViewById(R.id.sign_up_progress_bar);
        signInTextView = inflate.findViewById(R.id.textView);
        signInOrCreate = inflate.findViewById(R.id.login_button);
        password = inflate.findViewById(R.id.password);
        username = inflate.findViewById(R.id.username);
        signInOrCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignInButton();
            }
        });

        if(action != null && action.equals("register")){
            signInOrCreate.setText(R.string.login_create_account);
            signInTextView.setText(getResources().getString(R.string.login_signup_text));
        }

        Log.d(TAG, "onCreateView: "+action);

        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginRoutingInterface)
            callback = (LoginRoutingInterface) context;
        else {
            callback = null;
        }

        action = getArguments().getString("action");
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
        this.context = null;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(action != null && action.equals("register")){
            signInOrCreate.setText(R.string.login_create_account);
            signInTextView.setText(getResources().getString(R.string.login_signup_text));
        }
    }*/

    private void loginSignInButton(){
        if(!confermaInput()) {
            signUpProgressBar.setVisibility(View.VISIBLE);
            if(action != null && action.equals("register")){
                firebaseAuth.createUserWithEmailAndPassword(username.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                        .addOnCompleteListener((Activity) context, this);
            } else{
                firebaseAuth.signInWithEmailAndPassword(username.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                        .addOnCompleteListener((Activity) context, this);
            }
        }
        else{
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    /// todo: ovviamente questo è da migliorare
    /*private boolean validate(){
        String sUsername = username.getText().toString();
        if(sUsername.length() == 0)
            return false;

        int atIndex = sUsername.lastIndexOf('@');
        int dotIndex = sUsername.lastIndexOf('.');

        if(atIndex == -1 || dotIndex == -1 || atIndex > dotIndex)
            return false;

        // TODO: aggiungere questo caso: action != null && action.equals("register")
        String sPassword = password.getText().toString();
        if(sPassword.length() == 0)
            return false;

        return true;
    }*/

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


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        signUpProgressBar.setVisibility(View.INVISIBLE);
        if (task.isSuccessful()) {
            startActivity(new Intent(context, ListaCorsi.class));

        } else {
            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInWithEmail:failure", task.getException());
        }
    }
}
