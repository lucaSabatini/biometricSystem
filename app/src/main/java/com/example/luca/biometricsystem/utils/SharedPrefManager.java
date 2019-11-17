package com.example.luca.biometricsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.luca.biometricsystem.R;

public class SharedPrefManager {

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    public static final String NULL_KEY = "NULL_KEY";
    public static final String DEFAULT_VALUE = "DEFAULT_VALUE";

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
}
