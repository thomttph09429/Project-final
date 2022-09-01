package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.ROLE;
import static com.app.projectfinal.utils.Constant.ROLE_SAVE;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.adapter.ViewPagerAdapter;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import com.app.projectfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    private AHBottomNavigation bottomNavigation;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigation = findViewById(R.id.AHBottomNavigation);
        viewPager2 = findViewById(R.id.ViewPager);
        viewPager2.setAdapter(new ViewPagerAdapter(this));

        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home, R.drawable.custom_drawable_bottom_nav_home, R.color.white);
        AHBottomNavigationItem notification = new AHBottomNavigationItem(R.string.notification, R.drawable.custom_drawable_bottom_nav_notifications, R.color.color_main);
        AHBottomNavigationItem user = new AHBottomNavigationItem(R.string.personal, R.drawable.custom_drawable_bottom_nav_user, R.color.color_main);

        bottomNavigation.addItem(home);
        bottomNavigation.addItem(notification);
        bottomNavigation.addItem(user);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#1CAE81"));
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#1CAE81"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager2.setCurrentItem(position);
                bottomNavigation.setIconActiveColor(position, R.color.red);
                bottomNavigation.setTitleActiveColor(position, R.color.red);
                return true;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
            }
        });

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void countCart(int count) {
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf(count))
                .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                .build();
        bottomNavigation.setNotification(notification, 1);
    }

    /**
     * function to check if user already register store or not
     * if role=2=> registered store
     * if role =1=>not registered store yet
     * <pre>
     *     author:ThomTT
     *     date:03/08/2022
     *     TODO
     * </pre>
     */
//    public void checkIfUserRegisterShop() {
//
//        String url = UPDATE_USER + "/" + SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(Constant.USER_ID_SAVE);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                if (response != null) {
//                    try {
//                        JSONObject jsonObject = response.getJSONObject("data");
//                        JSONObject user = jsonObject.getJSONObject("user");
//                        String role = user.getString(ROLE);
//                        if (role.equals(2)) {
//                            SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(ROLE_SAVE, role);
//                        } else {
//                            SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(ROLE_SAVE, "1");
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
//    }

}
