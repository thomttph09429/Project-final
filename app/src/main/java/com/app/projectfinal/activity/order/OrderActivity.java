package com.app.projectfinal.activity.order;


import static com.app.projectfinal.utils.Constant.ADDRESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.chooseProduct.ChooseProductToBuyAdapter;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.model.ChooseProduct;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    private TextView tvUserName, tvPhoneNumber, tvAddress, tvNameShop;
    private ChooseProductToBuyAdapter buyAdapter;
    private List<Cart> cartListChecked;
    private RecyclerView rvPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        getAddress();
        initAction();
        Intent intent = getIntent();

        cartListChecked = intent.getParcelableArrayListExtra("cartListChecked");
        tvNameShop.setText(cartListChecked.get(0).getNameShop());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPay.setLayoutManager(layoutManager);
        buyAdapter = new ChooseProductToBuyAdapter(cartListChecked, this);
        rvPay.setAdapter(buyAdapter);


    }


    private void initView() {
        tvAddress = findViewById(R.id.tvAddress);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        rvPay = findViewById(R.id.rvPay);
        tvNameShop = findViewById(R.id.tvNameShop);

    }

    private void initAction() {
    }

    private void getAddress() {
        String url = ADDRESS + "?userId=" + ConstantData.getUserId(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                    JSONObject object = jsonArray.getJSONObject(0);
                    Gson gson = new Gson();
                    AddressUser addressUser = gson.fromJson(String.valueOf(object), AddressUser.class);
                    tvAddress.setText(addressUser.getLocation());
                    tvUserName.setText(addressUser.getCustomerName());
                    tvPhoneNumber.setText(addressUser.getPhone());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OrderActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderActivity.this, error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(OrderActivity.this.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }
}