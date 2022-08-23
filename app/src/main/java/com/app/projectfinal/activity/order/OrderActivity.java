package com.app.projectfinal.activity.order;


import static com.app.projectfinal.utils.Constant.ADDRESS;
import static com.app.projectfinal.utils.Constant.ADD_PRODUCTS;
import static com.app.projectfinal.utils.Constant.CATEGORY_ID;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME;
import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.AddProductActivity;
import com.app.projectfinal.adapter.chooseProduct.ChooseProductToBuyAdapter;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.model.ChooseProduct;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    private TextView tvUserName, tvPhoneNumber, tvAddress, tvNameShop, tvPrice, tvNote;
    private ChooseProductToBuyAdapter buyAdapter;
    private List<Cart> cartListChecked;
    private RecyclerView rvPay;
    private String totalAmount, addressId;
    private AppCompatButton btnBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        getAddress();
        initAction();
        Intent intent = getIntent();
        //receive cartListChecked from CartActivity
        cartListChecked = intent.getParcelableArrayListExtra("cartListChecked");
        totalAmount = intent.getExtras().getString("totalAmount");
        tvPrice.setText(totalAmount);
        tvNameShop.setText(cartListChecked.get(0).getNameShop());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPay.setLayoutManager(layoutManager);
        buyAdapter = new ChooseProductToBuyAdapter(cartListChecked, this);
        rvPay.setAdapter(buyAdapter);
        clickOrderProducts();

    }


    private void initView() {
        tvAddress = findViewById(R.id.tvAddress);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        rvPay = findViewById(R.id.rvPay);
        tvNameShop = findViewById(R.id.tvNameShop);
        tvPrice = findViewById(R.id.tvPrice);
        btnBuy = findViewById(R.id.btnBuy);
        tvNote = findViewById(R.id.tvNote);
    }

    private void clickOrderProducts() {
        btnBuy.setOnClickListener(v -> {
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            JSONObject object = new JSONObject();

            try {
                for (int i = 0; i < cartListChecked.size(); i++) {

                    obj.put(ID_PRODUCT, cartListChecked.get(i).getIdProduct());
                    obj.put(STORE_ID_PRODUCT, cartListChecked.get(i).getIdShop());
                    obj.put(QUANTITY_PRODUCT, cartListChecked.get(i).getAmount());
                    obj.put(PRICE_PRODUCT, ValidateForm.getPriceToInt(cartListChecked.get(i).getPrice()));
                    array.put(obj);
                }


                object.put("products", array);
                object.put("userId", ConstantData.getUserId(this));
                object.put("addressId", addressId);
                object.put("note", tvNote.getText().toString().trim());

                Log.e("address", object + "");


            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ORDER, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    showToast("Đơn hàng đã được đặt", R.drawable.ic_mark);


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
                    headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                    return headers;
                }
            };
            VolleySingleton.getInstance(OrderActivity.this).getRequestQueue().add(jsonObjectRequest);
        });
    }

    private void initAction() {
    }

    private void showToast(String text, int src) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
                    addressId = addressUser.getId();
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