package com.luca.sabatini.appello.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.luca.sabatini.appello.R;


public class SharedPrefManager {

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    public static final String DEFAULT_VALUE = "DEFAULT_VALUE";

    private static final String SURNAME = "surname";
    private static final String FIREBASEID = "firebaseid";
    private static final String MATRICOLA = "matricola";
    private static final String IS_REGISTERED = "is_registered";
    private static final String SESSIONID = "sessionid";
    private static final String ANNOCORSO = "annocorso";
    private static final String NOMECORSO = "nomecorso";
    private static final String CORSOID = "corsoid";
    private static final String REGISTRATIONID = "registrationid";
    private static final String EXIT_IS_CHECKED ="exit_is_checked";

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

    private void writeString(String key, String value){
        if(sharedPrefEditor == null) init();
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    private String readString(String key){
        if(key == null) return DEFAULT_VALUE;
        if(sharedPref == null) openHandler();
        return sharedPref.getString(key, DEFAULT_VALUE);
    }

    private void writeLong(String key, Long value){
        if(sharedPrefEditor == null) init();
        sharedPrefEditor.putLong(key, value);
        sharedPrefEditor.commit();
    }

    private Long readLong(String key){
        if(key == null) return -1L;
        if(sharedPref == null) openHandler();
        return sharedPref.getLong(key, -1L);
    }

    private void writeBool(String key, boolean value){
        if(sharedPrefEditor == null) init();
        sharedPrefEditor.putBoolean(key, value);
        sharedPrefEditor.commit();
    }

    private Boolean readBool(String key){
        if(key == null) return false;
        if(sharedPref == null) openHandler();
        return sharedPref.getBoolean(key, false);
    }


    public void writeSurname(String surname){writeString(SURNAME, surname);}

    public String readSurname(){return readString(SURNAME);}

    public void writeFirebaseId(String firebaseId){writeString(FIREBASEID, firebaseId);}

    public String readFirebaseId(){return readString(FIREBASEID);}

    public void writeMatricola(Long matricola){writeLong(MATRICOLA, matricola);}

    public Long readMatricola(){return readLong(MATRICOLA);}

    public void writeIsRegistered(Boolean isRegistered){writeBool(IS_REGISTERED, isRegistered);}

    public Boolean readIsRegistered(){return readBool(IS_REGISTERED);}

    public void writeExitIsChecked(Boolean isRegistered){writeBool(EXIT_IS_CHECKED, isRegistered);}

    public Boolean readExitIsChecked(){return readBool(EXIT_IS_CHECKED);}

    public void writeSessionId(Long sessionId){writeLong(SESSIONID, sessionId);}

    public Long readSessionId(){return readLong(SESSIONID);}

    public void writeNomeCorso(String firebaseId){writeString(NOMECORSO, firebaseId);}

    public String readNomeCorso(){return readString(NOMECORSO);}

    public void writeAnnoCorso(Long sessionId){writeLong(ANNOCORSO, sessionId);}

    public Long readAnnoCorso(){return readLong(ANNOCORSO);}

    public void writeCorsoId(Long corsoId){writeLong(CORSOID, corsoId);}

    public Long readCorsoId(){return readLong(CORSOID);}

    public void writeRegistrationId(String registrationId){writeString(REGISTRATIONID, registrationId);}

    public String readRegistrationId(){return readString(REGISTRATIONID);}

    public void resetSharedPref(){
        writeSurname(SharedPrefManager.DEFAULT_VALUE);
        writeMatricola(-1l);
        writeFirebaseId(SharedPrefManager.DEFAULT_VALUE);
        writeSessionId(-1L);
    }
}
