package com.app.projectfinal.activity;


import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.AVATAR;
import static com.app.projectfinal.utils.Constant.BUY_NOW;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.PHONE;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ProductByShopAdapter;
import com.app.projectfinal.bottom.ChooseCartFragment;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ValidateForm;
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

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivProduct,ivBack;
    private TextView tvName, tvPrice, tvNameShop, tvQuantity, tvCategory, tvDescription;
    private Bundle data;
    private String productName, price, image1, storeName, categoryName, description, storeIdS, quantity, phone, unitName, avatar;
    private RecyclerView rvProductByStoreId;
    private ProductByShopAdapter productByShopAdapter;
    private List<Product> products;
    private AppCompatImageButton btnAddCart, btnChat;
    private AppCompatButton btnBuy;
    private String idProduct;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RelativeLayout rlShop;
    private CircleImageView ivAvatar;
    private LinearLayoutCompat lnBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        initActions();
        receiveProductDetailWhenClick();
        displayProduct();
        showProductsByStore();
    }



    private void initActions() {
        products = new ArrayList<>();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        rlShop.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date 28/07/2022
     * </pre>
     */
    private void initView() {
        ivProduct = findViewById(R.id.iv_image_product);
        tvName = findViewById(R.id.tv_name);
        tvPrice = findViewById(R.id.tv_price);
        tvNameShop = findViewById(R.id.tv_name_shop);
        rvProductByStoreId = findViewById(R.id.rvProductByStoreId);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnChat = findViewById(R.id.btnChat);
        toolbar = findViewById(R.id.toolbar);
        rlShop = findViewById(R.id.rlShop);
        btnBuy = findViewById(R.id.btnBuy);
        ivAvatar = findViewById(R.id.ivAvatar);
        lnBottom = findViewById(R.id.lnBottom);
        ivBack= findViewById(R.id.ivBack);

    }

    /**
     * get bundle result from home screen (to ProductAdapter from DetailProductActivity )
     * <pre>
     *     author: ThomTT
     *     date: 28/07/2022
     * </pre>
     */
    private void receiveProductDetailWhenClick() {
        data = getIntent().getExtras();
        productName = data.getString(NAME_PRODUCT);
        price = data.getString(PRICE_PRODUCT);
        image1 = data.getString(IMAGE1_PRODUCT);
        storeName = data.getString(STORE_NAME_PRODUCT);
        categoryName = data.getString(CATEGORY_NAME);
        description = data.getString(DESCRIPTION_PRODUCT);
        storeIdS = data.getString(STORE_ID_PRODUCT);
        quantity = data.getString(QUANTITY_PRODUCT);
        idProduct = data.getString(ID_PRODUCT);
        unitName = data.getString(UNIT_NAME);
        if (storeIdS.equals(storeId)) {
            lnBottom.setVisibility(View.GONE);
        }

    }

    /**
     * show detail product
     * <pre>
     *     author:ThomTT
     *     date:28/07/2022
     *
     * </pre>
     */
    private void displayProduct() {
        tvName.setText(ValidateForm.capitalizeFirst(productName));
        tvPrice.setText(ValidateForm.getDecimalFormattedString(price));
        tvNameShop.setText(ValidateForm.capitalizeFirst(storeName));
        tvDescription.setText(ValidateForm.capitalizeFirst(description));
        tvCategory.setText(ValidateForm.capitalizeFirst(categoryName));
        tvQuantity.setText(quantity);
        Glide.with(getApplicationContext()).load(image1).error(R.drawable.ic_image_error).into(ivProduct);

    }

    /**
     * pass data to ChooseCartFragment
     * <pre>
     *     author:ThomTT
     *     date:
     *     todo
     * </pre>
     */
    private void clickAddCart() {
        Bundle bundle = new Bundle();
        bundle.putString(PRICE_PRODUCT, ValidateForm.getDecimalFormattedString(price));
        bundle.putString(QUANTITY_PRODUCT, quantity);
        bundle.putString(IMAGE1_PRODUCT, image1);
        bundle.putString(NAME_PRODUCT, productName);
        bundle.putString(ID_PRODUCT, idProduct);
        bundle.putString(STORE_ID_PRODUCT, storeIdS);
        bundle.putString(NAME_STORE, storeName);
        bundle.putString(UNIT_NAME, unitName);
        bundle.putString(DESCRIPTION_PRODUCT, description);
        bundle.putString(CATEGORY_NAME, categoryName);
        bundle.putString(BUY_NOW, "buy");


        ChooseCartFragment chooseCartFragment = new ChooseCartFragment();
        chooseCartFragment.setArguments(bundle);
        chooseCartFragment.show(getSupportFragmentManager(), "ChooseCartFragment");

    }

    /**
     * show product by store id
     * <pre>
     *     author:ThomTT
     *     date: 01/08/2022
     * </pre>
     */
    private void showProductsByStore() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvProductByStoreId.setLayoutManager(layoutManager);
        String url = PRODUCTS + "?" + "storeId" + "=" + storeIdS;
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
                            String idProduct = object.getString(ID_PRODUCT);
                            String unitName = object.getString(UNIT_NAME);

                            Log.e("idpro", idProduct);
                            products.add(new Product(price, productName, image1, description, storeName, categoryName, storeId, quantity, idProduct, unitName));
                            getDetailProduct();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
                productByShopAdapter = new ProductByShopAdapter(products, DetailProductActivity.this);
                rvProductByStoreId.setAdapter(productByShopAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(DetailProductActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * open chat screen when click button chat
     * <pre>
     *     author ThomTT
     *     date:03/08/2022
     *
     * </pre>
     */
    private void clickChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PHONE, phone);
        bundle.putString(STORE_ID_PRODUCT, storeIdS);
        bundle.putString(STORE_NAME_PRODUCT, storeName);
        Log.e("hehehe", phone + "" + storeIdS + "" + storeName + "");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void openViewShopScreen() {
        Intent intent = new Intent(this, ViewShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID_PRODUCT, storeIdS);
        bundle.putString(STORE_NAME_PRODUCT, storeName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Call api get phone number of store
     * <pre>
     *     author: ThomTT1
     *     date: 10/08/2022
     * </pre>
     */
    private void getDetailProduct() {
        String url = PRODUCTS + "/" + idProduct;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("product");
                        phone = data.getString(PHONE);
                        getAvatar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

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
                headers.put("Authorization", ConstantData.getToken(getApplicationContext().getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(DetailProductActivity.this).getRequestQueue().add(jsonObjectRequest);


    }

    private void getAvatar() {
        String url = ADD_STORES + "/" + storeIdS;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("store");
                        String avatar = data.getString("image1");
                        Glide.with(getApplicationContext()).load(avatar).error(R.drawable.ic_image_error).into(ivAvatar);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

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
                headers.put("Authorization", ConstantData.getToken(getApplicationContext().getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(DetailProductActivity.this).getRequestQueue().add(jsonObjectRequest);


    }


    private void buyNow() {
        Bundle bundle = new Bundle();
        bundle.putString(PRICE_PRODUCT, ValidateForm.getDecimalFormattedString(price));
        bundle.putString(QUANTITY_PRODUCT, quantity);
        bundle.putString(IMAGE1_PRODUCT, image1);
        bundle.putString(NAME_PRODUCT, productName);
        bundle.putString(ID_PRODUCT, idProduct);
        bundle.putString(STORE_ID_PRODUCT, storeIdS);
        bundle.putString(NAME_STORE, storeName);
        bundle.putString(UNIT_NAME, unitName);
        bundle.putString(DESCRIPTION_PRODUCT, description);
        bundle.putString(CATEGORY_NAME, categoryName);
        bundle.putString(BUY_NOW, BUY_NOW);


        ChooseCartFragment chooseCartFragment = new ChooseCartFragment();
        chooseCartFragment.setArguments(bundle);
        chooseCartFragment.show(getSupportFragmentManager(), "ChooseCartFragment");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlShop:
                openViewShopScreen();
                break;
            case R.id.btnBuy:
                buyNow();
                break;
            case R.id.btnAddCart:
                clickAddCart();
                break;
            case R.id.btnChat:
                clickChat();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }



}