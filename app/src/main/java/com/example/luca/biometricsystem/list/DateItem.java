package com.example.luca.biometricsystem.list;

public class DateItem extends ListItem{
    private Integer year;

    public DateItem(Integer year){
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
}
