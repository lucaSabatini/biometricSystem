package com.luca.sabatini.appello.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.luca.sabatini.appello.R;


public class SharedPrefManager {

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    public static final String NULL_KEY = "NULL_KEY";
    public static final String DEFAULT_VALUE = "DEFAULT_VALUE";

    private static final String SURNAME = "surname";
    private static final String FIREBASEID = "firebaseid";
    private static final String MATRICOLA = "matricola";

    public SharedPrefManager (Context context){
        this.context = context;
    }

    public void openHandler(){
        if(sharedPref == null){
            sharedPref =  context.getSharedPreferences(
                    context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        }
    }

    public void init(){
        if(sharedPref == null)openHandler();
        sharedPrefEditor = sharedPref.edit();
    }

    public void writeString(String key, String value){
        if(sharedPrefEditor == null) init();
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public String readString(String key){
        if(key == null) return NULL_KEY;
        if(sharedPref == null) openHandler();
        return sharedPref.getString(key, DEFAULT_VALUE);
    }

    public void writeLong(String key, Long value){
        if(sharedPrefEditor == null) init();
        sharedPrefEditor.putLong(key, value);
        sharedPrefEditor.commit();
    }

    public Long readLong(String key){
        if(key == null) return -1L;
        if(sharedPref == null) openHandler();
        return sharedPref.getLong(key, -1L);
    }

    public void writeSurname(String surname){writeString(SURNAME, surname);}

    public String readSurname(){return readString(SURNAME);}

    public void writeFirebaseId(String firebaseId){writeString(FIREBASEID, firebaseId);}

    public String readFirebaseId(){return readString(FIREBASEID);}

    public void writeMatricola(Long matricola){writeLong(MATRICOLA, matricola);}

    public Long readMatricola(){return readLong(MATRICOLA);}
}
