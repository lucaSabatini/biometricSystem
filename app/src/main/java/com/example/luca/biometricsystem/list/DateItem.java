package com.example.luca.biometricsystem.list;

import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DateItem extends ListItem{

    private static final String TAG = "DateItem";
    private Integer year;

    public DateItem(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + getYear();
    }
}
