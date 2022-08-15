package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.app.projectfinal.R;

public class MyProductActivity extends AppCompatActivity {
    private RecyclerView rvMyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        initView();
    }

    private void initView() {
        rvMyProduct=findViewById(R.id.rvMyProduct);
    }
}