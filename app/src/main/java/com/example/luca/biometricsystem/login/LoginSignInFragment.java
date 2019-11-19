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

import static com.example.luca.biometricsystem.login.LoginIntroFragment.EXTRA_ACTION;


public class LoginSignInFragment extends Fragment {

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

    private  SharedPrefManager sp;

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
        sp = new SharedPrefManager(context);
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
                        .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signUpProgressBar.setVisibility(View.INVISIBLE);

                                //Toast.makeText(context, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText( context , "SignUp failed." ,
                                            Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "signUpWithEmail:failure", task.getException());
                                } else {
                                    // se Studente andare su activity per inserire foto
                                    // altrimenti su LoginActivity
                                    Persona persona = new Persona(username.getEditText().getText().toString().trim());
                                    //firebaseAuth.getCurrentUser().sendEmailVerification();
                                    sp.writeString("uid", firebaseAuth.getCurrentUser().getUid());
                                    Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent( context , LoginActivity.class));
                                    if(persona.isStudent()){
                                        //firebaseAuth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent( context , RegistrazioneFoto.class);
                                        intent.putExtra(EXTRA_ACTION, "signup");
                                        context.startActivity(intent);
                                    }else{
                                        //firebaseAuth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent( context , LoginActivity.class));
                                    }
                                }
                            }
                        });
            } else{
                firebaseAuth.signInWithEmailAndPassword(username.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                        .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                signUpProgressBar.setVisibility(View.INVISIBLE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.getEditText().getText().length() < 6) {
                                        password.setError("Password too short, enter minimum 6 characters!");
                                    }else {
                                        Toast.makeText(context, "Authentication failed, sign up", Toast.LENGTH_LONG).show();
                                    }
                                }/*else if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(context, "e-mail is not verified", Toast.LENGTH_LONG).show();
                                    clear();
                                }*/ else {
                                    // se studente andare su activity per confermare presenza
                                    // altrimenti andare su ListaCorsi
                                    sp.writeString("uid", firebaseAuth.getCurrentUser().getUid());
                                    Persona persona = new Persona(username.getEditText().getText().toString().trim());
                                    if(persona.isStudent()){
                                        //firebaseAuth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(context, "Login", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent( context , RegistrazioneFoto.class);
                                        intent.putExtra(EXTRA_ACTION, "login");
                                        context.startActivity(intent);
                                    }else{
                                        Toast.makeText( context, "Login", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, ListaCorsi.class));
                                    }
                                }
                            }
                        });
            }
        }
        else{
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
        }
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


    /*@Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        signUpProgressBar.setVisibility(View.INVISIBLE);
        if (!task.isSuccessful()) {
            if (password.getEditText().getText().length() < 6) {
                password.setError("Password too short, enter minimum 6 characters!");
            }
            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInWithEmail:failure", task.getException());

        }else if(!firebaseAuth.getCurrentUser().isEmailVerified()){
            Toast.makeText(context, "e-mail is not verified", Toast.LENGTH_LONG).show();
            clear();
        }
        else {
            firebaseAuth.getCurrentUser().sendEmailVerification();
            Toast.makeText(context, "Sign up", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, ListaCorsi.class));
        }
    }*/
}
