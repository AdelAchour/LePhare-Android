package com.production.achour_ar.phareenquete.managers;

public class FirebaseManager {

    public static String getValueString(Object object){
        String result = "";
        if (object != null){
            return object.toString();
        }
        return result;
    }

}
