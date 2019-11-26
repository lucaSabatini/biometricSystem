package com.example.luca.biometricsystem.utils;

public class RestConstants {
    private static final String BASEURL = "https://appello-project.appspot.com";
    private static final String COURSE = BASEURL + "/courses";

    public static String getCourseUrl(String accessToken, String uid){
        return String.format("%s/readCourse?accessToken=%s&uid=%s", COURSE, accessToken, uid);
    }

    public static String postCourseUrl(String accessToken, String uid){
        return String.format("%s/createCourse?accessToken=%s&uid=%s", COURSE, accessToken, uid);
    }

    public static String deleteCourseUrl(String accessToken, String uid, Long id){
        return String.format("%s/deleteCourse?accessToken=%s&uid=%s&courseId=%s", COURSE, accessToken, uid, id);
    }

    public static String updateCourseUrl(String accessToken, String uid){
        return String.format("%s/updateCourse?accessToken=%s&uid=%s", COURSE, accessToken, uid);
    }

    public static String getAllCoursesByIdUrl(String accessToken, String uid){
        return String.format("%s/getCoursesByUserId?accessToken=%s&uid=%s", COURSE, accessToken, uid);
    }

}
