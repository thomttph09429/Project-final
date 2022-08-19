package com.app.projectfinal.utils;

import static com.app.projectfinal.utils.Constant.TOKEN;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;

import android.content.Context;

import com.app.projectfinal.data.SharedPrefsSingleton;

public class ConstantData {
    public static String getToken(Context context) {
        String token = SharedPrefsSingleton.getInstance(context).getStringValue(TOKEN);
        return token;
    }
    public static String getUserId(Context context) {
        String token = SharedPrefsSingleton.getInstance(context).getStringValue(USER_ID_SAVE);
        return token;
    }
}
