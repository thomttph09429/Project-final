package com.app.projectfinal.utils;

import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOKEN;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
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
    public static String getStoreId(Context context) {
        String token = SharedPrefsSingleton.getInstance(context).getStringValue(STORE_ID_PRODUCT);
        return token;
    }
    public static void showToast(String text, int src, Context context, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate(R.layout.custom_toast,view. findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
