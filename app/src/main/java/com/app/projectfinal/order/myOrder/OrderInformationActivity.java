package com.app.projectfinal.order.myOrder;

import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.TOTAL_PRICE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.print.PageRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.chooseProduct.ChooseProductToBuyAdapter;
import com.app.projectfinal.adapter.order.ItemOrderAdapter;
import com.app.projectfinal.adapter.order.OrderWaitAdapter;
import com.app.projectfinal.listener.ListenerUnit;
import com.app.projectfinal.model.order.DetailOrder;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInformationActivity extends AppCompatActivity {

    private List<DetailOrder> detailOrders;
    private String orderId;
    private TextView tvUserName, tvPhoneNumber, tvAddress, tvNameShop;
    private RecyclerView rvListOrder;
    private ItemOrderAdapter mItemOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        initView();
        initAction();
        getDetailOrder();
    }

    private void initView() {
        tvAddress = findViewById(R.id.tvAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvUserName = findViewById(R.id.tvUserName);
        tvNameShop = findViewById(R.id.tvNameShop);
        rvListOrder = findViewById(R.id.rvListOrder);
    }

    private void initAction() {
        detailOrders = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("id");
        Log.e("orderId", orderId+"");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvListOrder.setLayoutManager(layoutManager);

    }

    private void getDetailOrder() {
        String urlOrder = ORDER + "/" + orderId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlOrder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject orders = jsonObject.getJSONObject("order");
                        Gson gson = new Gson();
                        DetailOrder     detailOrder    = gson.fromJson(String.valueOf(orders), DetailOrder.class);
                        getInfor(detailOrder);
                        detailOrders.add(detailOrder);
                        mItemOrderAdapter = new ItemOrderAdapter(detailOrder.getProducts() , OrderInformationActivity.this);
                        rvListOrder.setAdapter(mItemOrderAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OrderInformationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        ProgressBarDialog.getInstance(OrderInformationActivity.this).closeDialog();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderInformationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(OrderInformationActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(OrderInformationActivity.this.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(OrderInformationActivity.this).getRequestQueue().add(jsonObjectRequest);

    }


    private void getInfor(DetailOrder detailOrder) {
        tvNameShop.setText(detailOrder.getName_store());
        tvAddress.setText(detailOrder.getLocation());
        tvPhoneNumber.setText(detailOrder.getCustomerPhone() + "");
        tvUserName.setText(detailOrder.getCustomerName());
    }
}