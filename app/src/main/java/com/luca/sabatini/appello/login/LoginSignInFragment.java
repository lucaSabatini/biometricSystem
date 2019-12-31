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

import com.luca.sabatini.appello.ProfessorProfile;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Persona;
import com.luca.sabatini.appello.student.CameraActivity;
import com.luca.sabatini.appello.student.UserProfile;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginSignInFragment extends Fragment {

    private static final String TAG = "LoginSignInFragment";
    private LoginRoutingInterface callback;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Context context;
    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";


    private String action;
    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
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
        passwordTextInput = inflate.findViewById(R.id.password);
        usernameTextInput = inflate.findViewById(R.id.username);
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

    public boolean isStudent() {
        return sp.readMatricola() != -1;
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
        if(isInputBad()) {
            Toast.makeText(context, getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
            return;
        }
        signUpProgressBar.setVisibility(View.VISIBLE);
        String username = usernameTextInput.getEditText().getText().toString().trim();
        String password = passwordTextInput.getEditText().getText().toString().trim();

        if(action != null && action.equals("register")){
            firebaseAuth.createUserWithEmailAndPassword(username, password)
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
                                //firebaseAuth.getCurrentUser().sendEmailVerification();
                                String emailBeforeAt = username.split("@")[0];
                                if (emailBeforeAt.contains(".")) {
                                    Log.d(TAG, "setLastNameAndStudentId: login studente");
                                    sp.writeMatricola(Long.parseLong(emailBeforeAt.split("\\.")[1]));
                                }
                                sp.writeSurname(emailBeforeAt.split("\\.")[0]);
                                sp.writeFirebaseId(firebaseAuth.getCurrentUser().getUid());

                                if(isStudent()){
                                    context.startActivity(new Intent(context, UserProfile.class));
                                }else{
                                    context.startActivity(new Intent(context, ProfessorProfile.class));
                                }
                                getActivity().finish();
                            }
                        }
                    });
        }
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
