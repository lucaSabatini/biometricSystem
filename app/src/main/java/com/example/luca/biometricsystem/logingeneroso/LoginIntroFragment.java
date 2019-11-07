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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginIntroFragment extends Fragment implements OnCompleteListener<AuthResult>  {

    private static final String TAG = "LoginIntroFragment";
    TextView newAccount;
    Button login;
    EditText username;
    EditText password;
    Button signInOrCreate;
    ProgressBar signInProgressBar;

    private LoginRoutingInterface callback;
    private Context context;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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




    /// todo: ovviamente questo è da migliorare
    private boolean validate(){
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
    }

    public void loginSignInButton(){
        if(validate()) {
            signInProgressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(username.getEditableText().toString(), password.getEditableText().toString())
                    .addOnCompleteListener((Activity) context, this);
        }
        else{
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        signInProgressBar.setVisibility(View.INVISIBLE);
        if (task.isSuccessful()) {
            startActivity(new Intent(context, ListaCorsi.class));
        } else {
            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInWithEmail:failure", task.getException());
        }
    }

}