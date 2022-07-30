package com.app.projectfinal.activity;


import static com.app.projectfinal.utils.Constant.CATEGORY;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.model.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView ivProduct;
    private TextView tvName, tvPrice, tvNameShop;
    private Bundle data;
    private String productName, price, image1, storeName, categoryName, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();

        receiveProductDetailWhenClick();
        displayProduct();
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
        tvName.setText(productName);
        tvPrice.setText(price);
        tvNameShop.setText(storeName);
        Glide.with(this).load(image1).error(R.drawable.ic_image_error).into(ivProduct);

    }
}