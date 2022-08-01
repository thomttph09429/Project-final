package com.app.projectfinal.activity;


import static com.app.projectfinal.utils.Constant.CATEGORY;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ProductAdapter;
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
    private String productName, price, image1, storeName, categoryName, description, storeId, quantity;
    private RecyclerView rvProductByStoreId;
    private ProductAdapter productAdapter;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        initActions();
        receiveProductDetailWhenClick();
        displayProduct();
        showProductsByStory();
    }

    private void initActions() {
        products = new ArrayList<>();

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
        tvQuantity.setText(ValidateForm.capitalizeFirst(quantity));
        Glide.with(this).load(image1).error(R.drawable.ic_image_error).into(ivProduct);

    }

    /**
     * show product by store id
     * <pre>
     *     author:ThomTT
     *     date: 01/08/2022
     * </pre>
     */
    private void showProductsByStory() {
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
                            products.add(new Product(price, productName, image1, description, storeName, categoryName, storeId, quantity));
                            ProgressBarDialog.getInstance(DetailProductActivity.this).closeDialog();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
                productAdapter = new ProductAdapter(products, DetailProductActivity.this);
                rvProductByStoreId.setAdapter(productAdapter);

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