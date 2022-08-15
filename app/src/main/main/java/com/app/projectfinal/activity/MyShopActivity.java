package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.STORE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projectfinal.R;
import com.app.projectfinal.data.SharedPrefsSingleton;

public class MyShopActivity extends AppCompatActivity {
    private TextView tvStoreName;
    private LinearLayout lnStartSell, lnMyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        initView();
        getInf();
        startSell();
        openMyProductScreen();
    }


    private void initView() {
        tvStoreName = findViewById(R.id.tvStoreName);
        lnStartSell = findViewById(R.id.lnStartSell);
        lnMyProduct = findViewById(R.id.lnMyProduct);
    }

    private void getInf() {
        tvStoreName.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(STORE_NAME));
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

}