package com.app.projectfinal.order.myOrder.fragment;

import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.TOTAL_PRICE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.order.OrderWaitAdapter;
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


public class DeliveryFragment extends Fragment {

    private View view;
    private RecyclerView rvDelivery;
    private TextView tvAmountDelivery;
    private List<Order> orders;
    private OrderWaitAdapter orderWaitAdapter;
    private LinearLayout lnShow, lnHide;
    private String nameStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_delivery, container, false);
        initView();
        initAction();
        getDelivery();
        return view;
    }

    private void getDelivery() {
        String urlOrder = ORDER + "?" + "userId=" + ConstantData.getUserId(getContext()) + "&status=" + 2;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlOrder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("orders");
                        int totalOrder = jsonObject.getInt(TOTAL);

                        if (totalOrder == 0) {
                            lnShow.setVisibility(View.GONE);
                            lnHide.setVisibility(View.VISIBLE);

                        } else {
                            lnShow.setVisibility(View.VISIBLE);
                            lnHide.setVisibility(View.GONE);
                        }
                        tvAmountDelivery.setText("Bạn có " + totalOrder + " đơn đang giao");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int totalPrice = object.getInt(TOTAL_PRICE);
                            nameStore = object.getString("name_store");
                            String id = object.getString("id");
                            JSONArray products = object.getJSONArray("products");
                            List<ItemOrder> itemOrders = new ArrayList<>();
                            for (int j = 0; j < products.length(); j++) {
                                JSONObject item = products.getJSONObject(j);
                                Gson gson = new Gson();
                                ItemOrder itemOrder = gson.fromJson(String.valueOf(item), ItemOrder.class);
                                itemOrders.add(itemOrder);
                                orders.add(new Order(products.length(), totalPrice, itemOrders, nameStore, id));

                            }
                            orderWaitAdapter = new OrderWaitAdapter(orders, getContext());
                            rvDelivery.setAdapter(orderWaitAdapter);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        ProgressBarDialog.getInstance(getContext()).closeDialog();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(getContext()).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getContext().getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);


    }

    private void initView() {
        rvDelivery = view.findViewById(R.id.rvDelivery);
        tvAmountDelivery = view.findViewById(R.id.tvAmountDelivery);
        lnShow = view.findViewById(R.id.lnShow);
        lnHide = view.findViewById(R.id.lnHide);
    }

    private void initAction() {
        orders = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvDelivery.setLayoutManager(layoutManager);

    }
}