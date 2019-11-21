package com.example.luca.biometricsystem.list;

import com.example.luca.biometricsystem.entities.Corso;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class CorsoPostObject {


        @SerializedName("data")
        public Corso data;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }


}
