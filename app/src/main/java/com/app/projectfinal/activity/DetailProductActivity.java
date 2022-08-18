package com.app.projectfinal.activity;


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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.adapter.ProductByShopAdapter;
import com.app.projectfinal.bottom.ChooseCartFragment;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ProgressBarDialog;
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

public class DetailProductActivity extends AppCompatActivity {
    private ImageView ivProduct;
    private TextView tvName, tvPrice, tvNameShop, tvQuantity, tvCategory, tvDescription;
    private Bundle data;
    private String productName, price, image1, storeName, categoryName, description, storeId, quantity, phone, unitName;
    private RecyclerView rvProductByStoreId;
    private ProductByShopAdapter productByShopAdapter;
    private List<Product> products;
    private AppCompatImageButton btnAddCart, btnChat;
    private String idProduct;
    private Toolbar toolbar;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        initActions();
        receiveProductDetailWhenClick();
        displayProduct();
        showProductsByStore();
        clickAddCart();
        clickChat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_carts:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.action_report:
                Toast.makeText(this, "Báo cáo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                Toast.makeText(this, "Hỗ trợ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initActions() {
        products = new ArrayList<>();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(null);

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
        storeId = data.getString(STORE_ID_PRODUCT);
        quantity = data.getString(QUANTITY_PRODUCT);
        idProduct = data.getString(ID_PRODUCT);
        unitName = data.getString(UNIT_NAME);


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
        Glide.with(this).load(image1).error(R.drawable.ic_image_error).into(ivProduct);

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
        btnAddCart.setOnClickListener(v -> {
            Log.e("pricedd", price + quantity + image1);
            Bundle bundle = new Bundle();
            bundle.putString(PRICE_PRODUCT, ValidateForm.getDecimalFormattedString(price));
            bundle.putString(QUANTITY_PRODUCT, quantity);
            bundle.putString(IMAGE1_PRODUCT, image1);
            bundle.putString(NAME_PRODUCT, productName);
            bundle.putString(ID_PRODUCT, idProduct);
            bundle.putString(STORE_ID_PRODUCT, storeId);
            bundle.putString(NAME_STORE, storeName);
            bundle.putString(UNIT_NAME, unitName);

            ChooseCartFragment chooseCartFragment = new ChooseCartFragment();
            chooseCartFragment.setArguments(bundle);
            chooseCartFragment.show(getSupportFragmentManager(), "ChooseCartFragment");

        });
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
        String url = PRODUCTS + "?" + "storeId" + "=" + storeId;
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
                Toast.makeText(DetailProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
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
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(PHONE, phone);
            bundle.putString(STORE_ID_PRODUCT, storeId);
            bundle.putString(STORE_NAME_PRODUCT, storeName);
            intent.putExtras(bundle);
            startActivity(intent);

        });
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        VolleySingleton.getInstance(DetailProductActivity.this).getRequestQueue().add(jsonObjectRequest);


    }
}