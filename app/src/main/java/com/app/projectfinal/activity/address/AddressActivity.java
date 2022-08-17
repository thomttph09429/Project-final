package com.app.projectfinal.activity.address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.app.projectfinal.R;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lnAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initAction();
    }

    private void initAction() {
        lnAddress.setOnClickListener(this::onClick);
    }

    private void initView() {
        lnAddress= findViewById(R.id.lnAddress);
    }
    private  void openAddAddressScreen(){
        startActivity(new Intent(this, AddAddressActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lnAddress:
                openAddAddressScreen();
                break;
        }

    }
}