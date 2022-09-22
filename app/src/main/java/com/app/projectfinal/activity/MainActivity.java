package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_STORES;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.adapter.ViewPagerAdapter;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.VolleySingleton;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.app.projectfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    private AHBottomNavigation bottomNavigation;
    private ViewPager2 viewPager2;
    public static String storeId, storeName;
    public static int status, total;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isHaveAccount();

        bottomNavigation = findViewById(R.id.AHBottomNavigation);
        viewPager2 = findViewById(R.id.ViewPager);
        viewPager2.setAdapter(new ViewPagerAdapter(this));

        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.home, R.drawable.custom_drawable_bottom_nav_home, R.color.color_main);
        AHBottomNavigationItem notification = new AHBottomNavigationItem(R.string.notification, R.drawable.custom_drawable_bottom_nav_notifications, R.color.color_main);
        AHBottomNavigationItem user = new AHBottomNavigationItem(R.string.personal, R.drawable.custom_drawable_bottom_nav_user, R.color.color_main);

        bottomNavigation.addItem(home);
        bottomNavigation.addItem(notification);
        bottomNavigation.addItem(user);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#ffffff"));

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

    public void isHaveAccount() {
        String urlProducts = ADD_STORES + "/" + "?userId=" + ConstantData.getUserId(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        total= jsonObject.getInt("total");
                        JSONArray data = jsonObject.getJSONArray("stores");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                             status= jsonObject1.getInt("status");
                            storeId = jsonObject1.getString("id");
                            storeName = jsonObject1.getString("storeName");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(MainActivity.this));
                return headers;
            }
        };
        VolleySingleton.getInstance(MainActivity.this).getRequestQueue().add(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
