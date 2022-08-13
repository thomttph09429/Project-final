package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.app.projectfinal.R;
import com.app.projectfinal.adapter.CartAdapter;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initAction();
        getListCartFromDB();
    }

    private void initAction() {
        cartList = new ArrayList<>();
        LinearLayoutManager manager= new LinearLayoutManager(this);
        rvCart.setLayoutManager(manager);
    }

    private void initView() {
        rvCart = findViewById(R.id.rvCart);
    }

    private void getListCartFromDB() {
        cartList= CartDatabase.getInstance(this).cartDAO().getCards();
        CartAdapter cartAdapter = new CartAdapter(this, cartList);
        rvCart.setAdapter(cartAdapter);

    }
}