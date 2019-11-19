package com.example.luca.biometricsystem.utils;

public class RestConstants {
    private static final String BASEURL = "https://appello-project.appspot.com";
    private static final String COURSE = BASEURL + "/courses";

    public static String getCourseUrl(String accessToken, String uid){
        return String.format("%s/getCourse?accessToken=%s&uid%s", COURSE, accessToken, uid);
    }

    public static String postCourseUrl(String accessToken, String uid){
        return String.format("%s/postCourse?accessToken=%s&uid%s", COURSE, accessToken, uid);
    }

    public static String deleteCourseUrl(String accessToken, String uid){
        return String.format("%s/deleteCourse?accessToken=%s&uid%s", COURSE, accessToken, uid);
    }

    public static String getAllCoursesByIdUrl(String accessToken, String uid){
        return String.format("%s/getAllCoursesById?accessToken=%s&uid%s", COURSE, accessToken, uid);
    }

}
