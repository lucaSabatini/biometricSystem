package com.luca.sabatini.appello.entities;

import android.util.Log;

import java.io.Serializable;

public class Persona implements Serializable {

    private static final String TAG = "Persona";
    private String email;
    private String lastName;
    private String studentId;
    private boolean isStudent;

    public Persona(String email){
        this.email = email;
        isStudent = false;
        setLastNameAndStudentId();
    }

    private void setLastNameAndStudentId(){
        String lastNameAndStudentId = email.split("@")[0];
        if (lastNameAndStudentId.contains(".")) {
            Log.d(TAG, "setLastNameAndStudentId: "+"studente");
            setLastName(lastNameAndStudentId.split("\\.")[0]);
            //if(getLastName().equals("sabatini")) return;
            setStudentId(lastNameAndStudentId.split("\\.")[1]);
        } else{
            setLastName(lastNameAndStudentId);
        }
    }

    public String getEmail(){
        return email;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setStudentId(String studentId){
        isStudent=true;
        this.studentId = studentId;
    }

    public String getStudentId(){
        return studentId;
    }

    public String getLastName(){
        return lastName;
    }

    public boolean isStudent(){
        return isStudent;
    }

}
