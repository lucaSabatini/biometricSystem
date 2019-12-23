package com.luca.sabatini.appello.ui.courseList;

public abstract class ListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_COURSE = 1;

    abstract public int getType();
}
