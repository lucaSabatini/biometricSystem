package com.luca.sabatini.appello.student.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.entities.Persona;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    Persona persona;


    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");
        persona = new Persona(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
    }

    public void setStudentId(){
        mText.setValue(persona.getStudentId());
    }

    public void setLastName(){
        mText.setValue(persona.getLastName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}