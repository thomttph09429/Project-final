package com.app.projectfinal.order.shopOrder.fragment;

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
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeliveryConfirmFragment extends Fragment {

    private View view;
    private RecyclerView rvDelivery;
    private TextView tvAmountDelivery;
    private List<Order> orders;
    private OrderAdapter orderAdapter;
    private LinearLayout lnShow, lnHide;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_delivery_confirm, container, false);
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
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        int totalOrder = jsonObject.getInt(TOTAL);
                        if (totalOrder==0){
                            lnShow.setVisibility(View.GONE);
                            lnHide.setVisibility(View.VISIBLE);

                        }else {
                            lnShow.setVisibility(View.VISIBLE);
                            lnHide.setVisibility(View.GONE);
                        }
                        tvAmountDelivery.setText("Bạn có " + totalOrder + " đơn đang giao");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int totalPrice = object.getInt(TOTAL_PRICE);

                            JSONArray jsonArray1 = object.getJSONArray("products");
                            Log.e("price", jsonArray1 + "");
//                            orders.add(new Order(jsonArray1.length(), totalPrice));
//
//                            orderWaitAdapter = new OrderWaitAdapter(orders, getContext());
//                            rvDelivery.setAdapter(orderWaitAdapter);


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