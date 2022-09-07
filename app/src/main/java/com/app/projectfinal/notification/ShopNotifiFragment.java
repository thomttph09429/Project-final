package com.app.projectfinal.notification;

import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.TOTAL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.projectfinal.adapter.NotifyAdapter;
import com.app.projectfinal.model.order.DetailOrder;
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


public class ShopNotifiFragment extends Fragment {

    private View view;
    private RecyclerView rvNotify;
    private List<DetailOrder> orders;
    private NotifyAdapter notifyAdapter;
    private LinearLayout lnHide;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_shop_notifi, container, false);
        initView();
        initAction();
        getProducts();
        return view;
    }

    private void initView() {
        rvNotify = view.findViewById(R.id.rvNotify);
        lnHide = view.findViewById(R.id.lnHide);
    }
    private void initAction() {
        orders = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNotify.setLayoutManager(layoutManager);
    }
    private void getProducts() {

        String url = ORDER + "?storeId=" + storeId+ "&page=" + 1 + "&size=" + 50;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray order = jsonObject.getJSONArray("orders");
                      int  totalOrder = jsonObject.getInt(TOTAL);
                        if (totalOrder == 0) {
                            rvNotify.setVisibility(View.GONE);
                            lnHide.setVisibility(View.VISIBLE);

                        } else {
                            rvNotify.setVisibility(View.VISIBLE);
                            lnHide.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < order.length(); i++) {
                            JSONObject object = order.getJSONObject(i);
                            Gson gson = new Gson();
                            DetailOrder detailOrder = gson.fromJson(String.valueOf(object), DetailOrder.class);
                            orders.add(detailOrder);
                            notifyAdapter = new NotifyAdapter(orders, getContext(),"yes");
                            rvNotify.setAdapter(notifyAdapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
    }

}