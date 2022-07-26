package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.app.projectfinal.R;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView iv_detail_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

    }
}