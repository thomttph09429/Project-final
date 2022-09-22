package com.app.projectfinal.order.shopOrder.fragment;

import static com.app.projectfinal.activity.MyShopActivity.storeId;
import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.TOTAL_PRICE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.order.myOrder.OrderAdapter;
import com.app.projectfinal.adapter.order.shopOrder.OrderConfirmAdapter;
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


public class CancelConfirmFragment extends Fragment {
    private View view;
    private RecyclerView rvCancel;
    private List<Order> orders;
    private int totalOrder, totalPrice;
    private TextView tvAmountWait;
    private OrderConfirmAdapter mOrderConfirmAdapter;
    private LinearLayout lnShow, lnHide;
    private String nameStore, orderId, userName;
    private  int status;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_cancel_confirm, container, false);
        initView();
        initAction();
        getOrderPendingConfirm();
        return view;
    }

    private void initView() {
        rvCancel = view.findViewById(R.id.rvCancel);
        tvAmountWait = view.findViewById(R.id.tvAmountWait);
        lnShow = view.findViewById(R.id.lnShow);
        lnHide = view.findViewById(R.id.lnHide);
    }

    private void initAction() {
        orders = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvCancel.setLayoutManager(layoutManager);

    }

    private void getOrderPendingConfirm() {
        String urlOrder =  ORDER + "?storeId=" + storeId  + "&status=" + 0 + "&page=1&size=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlOrder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("orders");
                        totalOrder = jsonObject.getInt(TOTAL);
                        if (totalOrder == 0) {
                            lnShow.setVisibility(View.GONE);
                            lnHide.setVisibility(View.VISIBLE);

                        } else {
                            lnShow.setVisibility(View.VISIBLE);
                            lnHide.setVisibility(View.GONE);
                        }
                        tvAmountWait.setText("Bạn có " + totalOrder + " đơn đã hủy");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            totalPrice = object.getInt(TOTAL_PRICE);
                            nameStore = object.getString("name_store");
                            orderId = object.getString("id");
                            status = object.getInt("status");
                            userName = object.getString("customerName");

                            JSONArray products = object.getJSONArray("products");
                            List<ItemOrder> itemOrders = new ArrayList<>();
                            for (int j = 0; j < products.length(); j++) {
                                JSONObject item = products.getJSONObject(j);
                                Gson gson = new Gson();
                                ItemOrder itemOrder = gson.fromJson(String.valueOf(item), ItemOrder.class);
                                itemOrders.add(itemOrder);

                            }
                            orders.add(new Order(products.length(), totalPrice, itemOrders, nameStore, orderId, status, userName));
                            mOrderConfirmAdapter = new OrderConfirmAdapter(orders, getContext() );
                            rvCancel.setAdapter(mOrderConfirmAdapter);
                            ProgressBarDialog.getInstance(getContext()).closeDialog();

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
}