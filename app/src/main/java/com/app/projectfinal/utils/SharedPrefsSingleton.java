package com.app.projectfinal.utils;

import static com.app.projectfinal.utils.Constant.MY_SHARED_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsSingleton {

    private SharedPreferences sharedPref;
    private Context appContext;
    private static SharedPrefsSingleton instance;
    public static synchronized SharedPrefsSingleton getInstance(Context applicationContext) {
        if (instance == null)
            instance = new SharedPrefsSingleton(applicationContext);
        return instance;
    }

    private SharedPrefsSingleton(Context applicationContext) {
        appContext = applicationContext;
        sharedPref = appContext.getSharedPreferences(
                MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }


    public void putStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return sharedPref.getString(key, "");

    }
    public void deleteAll() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

}
