package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyShopActivity extends AppCompatActivity {
    private TextView tvStoreName;
    private LinearLayout lnStartSell, lnMyProduct;
    private String storeId, storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        initView();
        getInfoShop();
        startSell();
        openMyProductScreen();
    }


    private void initView() {
        tvStoreName = findViewById(R.id.tvStoreName);
        lnStartSell = findViewById(R.id.lnStartSell);
        lnMyProduct = findViewById(R.id.lnMyProduct);
    }


    private void startSell() {
        lnStartSell.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
        });


    }

    private void openMyProductScreen() {
        lnMyProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyProductActivity.class);
            startActivity(intent);
        });


    }

    /**
     * receive info of shop and get detail store
     * <pre>
     *     author:ThomTT1
     *     date:18/06/2022
     * </pre>
     */
    private void getInfoShop() {
        Bundle bundle = getIntent().getExtras();
        storeId = bundle.getString(STORE_ID_PRODUCT);
        String urlProducts = ADD_STORES + "/" + storeId;
        Log.e("store", storeId);
        ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("user");
                        storeName = data.getString(STORE_NAME_PRODUCT);
                        tvStoreName.setText(storeName);
                        ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MyShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyShopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();

            }
        });
        VolleySingleton.getInstance(MyShopActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

}