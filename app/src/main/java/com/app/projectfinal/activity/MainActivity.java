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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.adapter.ViewPagerAdapter;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import com.app.projectfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    private AHBottomNavigation bottomNavigation;
    private ViewPager2 viewPager2;
    public static String storeId;
    public static int role;

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
        String urlProducts = UPDATE_USER + "/" + ConstantData.getUserId(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("user");
                         role = data.getInt(ROLE);
                        storeId = data.getString(STORE_ID_PRODUCT);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

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


}
