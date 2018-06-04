package com.alilozano.zerozero;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Upao on 26/05/2018.
 */

public class ZeroSharedPreferences {
    private static String MY_PREFS_NAME = "MY_PREFS_NAME";
    public static String UID_LOGUED_USER = "UID_LOGUED_USER";

    private SharedPreferences sharedPreferences;
    public ZeroSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(
                MY_PREFS_NAME,
                Context.MODE_PRIVATE);
    }

    public void setString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }
}
