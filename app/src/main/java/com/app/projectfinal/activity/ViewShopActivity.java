package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MyShopActivity.storeId;
import static com.app.projectfinal.activity.MyShopActivity.storeName;
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
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.MyProductAdapter;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewShopActivity extends AppCompatActivity {
    private RecyclerView rvAllProduct;
    private List<Product> products;
    private ProductAdapter productAdapter;
    private String storeIds, storeNames;
    private NestedScrollView nestedScrollView;
    private int page = 1;
    private TextView tvUserName;
    private CircleImageView ivAvatar;
    private ImageView ivCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop);

        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            storeIds = storeId;
            tvUserName.setText(storeName);

        } else {
            storeIds = bundle.getString(STORE_ID_PRODUCT);
            storeNames = bundle.getString(STORE_NAME_PRODUCT);
            tvUserName.setText(storeNames);

        }
        initAction();
        scrollPage();

    }

    private void initView() {
        rvAllProduct = findViewById(R.id.rvAllProduct);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        tvUserName = findViewById(R.id.tvUserName);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivCover = findViewById(R.id.ivCover);

    }

    private void initAction() {

        products = new ArrayList<>();
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvAllProduct.setHasFixedSize(true);
        rvAllProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(products, this);
        rvAllProduct.setAdapter(productAdapter);
        getProducts(page);
    }

    private void scrollPage() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    getProducts(page);
                }

            }
        });


    }

    private void getProducts(int page) {

        String url = PRODUCTS + "?" + "storeId" + "=" + storeIds + "&page=" + page + "&size=" + 12;
        ProgressBarDialog.getInstance(this).showDialog("Đang tải", ViewShopActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("products");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String productName = object.getString(NAME_PRODUCT);
                            String image1 = object.getString(IMAGE1_PRODUCT);
                            String price = object.getString(PRICE_PRODUCT);
                            String storeName = object.getString(STORE_NAME_PRODUCT);
                            String categoryName = object.getString(CATEGORY_NAME);
                            String description = object.getString(DESCRIPTION_PRODUCT);
                            String storeId = object.getString(STORE_ID_PRODUCT);
                            String quantity = object.getString(QUANTITY_PRODUCT);
                            String id = object.getString(ID_PRODUCT);
                            String unit = object.getString(UNIT_NAME);

                            products.add(new Product(price, productName, image1, description, storeName, categoryName, storeId, quantity, id, unit));
                            getInfoShop();
                        }
                        if (products != null) {
                            productAdapter = new ProductAdapter(products, ViewShopActivity.this);
                            rvAllProduct.setAdapter(productAdapter);
                        } else {

                        }

                        ProgressBarDialog.getInstance(ViewShopActivity.this).closeDialog();

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(ViewShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewShopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(ViewShopActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(ViewShopActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

    private void getInfoShop() {
        String url = ADD_STORES + "/" + storeIds;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("user");
                        String image1 = data.getString("image1");
                        String image2 = data.getString("image2");
                        Log.e("hh", image1+"ggggg"+ storeIds+"");
                        Glide.with(ViewShopActivity.this).load(image1).centerCrop().error(R.drawable.avatar_empty).into(ivAvatar);
                        Glide.with(ViewShopActivity.this).load(image2).centerCrop().error(R.drawable.ic_cover).into(ivCover);
                        ProgressBarDialog.getInstance(ViewShopActivity.this).closeDialog();

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(ViewShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewShopActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(ViewShopActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(ViewShopActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

}