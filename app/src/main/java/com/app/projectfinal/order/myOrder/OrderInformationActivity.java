package com.app.projectfinal.order.myOrder;

import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.STATUS;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.ChatActivity;
import com.app.projectfinal.activity.ViewShopActivity;
import com.app.projectfinal.adapter.order.myOrder.ItemOrderAdapter;
import com.app.projectfinal.model.order.DetailOrder;
import com.app.projectfinal.order.shopOrder.OrderShopInformationActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private List<DetailOrder> detailOrders;
    private String orderId, storeId, storeName, phoneStore;
    private TextView tvUserName, tvPhoneNumber, tvAddress, tvNameShop, tvCodeOrder, tvTotalPrice, tvTimeOrder, tvPay, tvComplete, tvCancel;
    private RecyclerView rvListOrder;
    private ItemOrderAdapter mItemOrderAdapter;
    private RelativeLayout rlCancel, rlComplete, rlPay, rlTimeOrder, rlCode;
    private TextView tvStatusPending, tvStatusDelivery, tvStatusComplete, tvStatusCancel, tvViewShop;
    private AppCompatButton btnCancel, btnProcess,btnContact;
    private LinearLayoutCompat lnBottom;
    private ImageView ivBack;

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
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCodeOrder = findViewById(R.id.tvCodeOrder);
        tvTimeOrder = findViewById(R.id.tvTimeOrder);
        tvPay = findViewById(R.id.tvPay);
        tvComplete = findViewById(R.id.tvComplete);
        tvCancel = findViewById(R.id.tvCancel);
        tvViewShop = findViewById(R.id.tvViewShop);
        btnContact = findViewById(R.id.btnContact);

        rlCancel = findViewById(R.id.rlCancel);
        rlComplete = findViewById(R.id.rlComplete);
        rlPay = findViewById(R.id.rlPay);
        rlTimeOrder = findViewById(R.id.rlTimeOrder);
        rlCode = findViewById(R.id.rlCode);

        tvStatusPending = findViewById(R.id.tvStatusPending);
        tvStatusDelivery = findViewById(R.id.tvStatusDelivery);
        tvStatusComplete = findViewById(R.id.tvStatusComplete);
        tvStatusCancel = findViewById(R.id.tvStatusCancel);
        btnCancel = findViewById(R.id.btnCancel);
        btnProcess = findViewById(R.id.btnProcess);
        lnBottom = findViewById(R.id.lnBottom);
        ivBack = findViewById(R.id.ivBack);

    }

    /**
     * receive orderId from OrderAdapter, item from notify
     */
    private void initAction() {
        tvViewShop.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnContact.setOnClickListener(this);

        detailOrders = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("id");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvListOrder.setLayoutManager(layoutManager);

    }

    private void getDetailOrder() {
        String urlOrder = ORDER + "/" + orderId;
        ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlOrder, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject orders = jsonObject.getJSONObject("order");
                        Gson gson = new Gson();
                        DetailOrder detailOrder = gson.fromJson(String.valueOf(orders), DetailOrder.class);
                        getInfor(detailOrder);
                        detailOrders.add(detailOrder);
                        mItemOrderAdapter = new ItemOrderAdapter(detailOrder.getProducts(), OrderInformationActivity.this);
                        rvListOrder.setAdapter(mItemOrderAdapter);
                        ProgressBarDialog.getInstance(OrderInformationActivity.this).closeDialog();

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
        storeName = detailOrder.getName_store();
        tvNameShop.setText(storeName);
        phoneStore= detailOrder.getPhone_store();
        tvAddress.setText(detailOrder.getLocation());
        tvPhoneNumber.setText(detailOrder.getCustomerPhone() + "");
        tvUserName.setText(detailOrder.getCustomerName());
        tvTotalPrice.setText(ValidateForm.getDecimalFormattedString(detailOrder.getTotalPrice() + ""));
        //display time in process order
        tvCodeOrder.setText(detailOrder.getId());
        tvTimeOrder.setText(detailOrder.getCreatedAt());
        tvPay.setText(detailOrder.getUpdatedAt());
        tvComplete.setText(detailOrder.getUpdatedAt());
        tvCancel.setText(detailOrder.getUpdatedAt());
        storeId = detailOrder.getStore_id();
        if (detailOrder.getStatus() == 0) {
            rlCancel.setVisibility(View.VISIBLE);
            tvStatusCancel.setVisibility(View.VISIBLE);
        } else if (detailOrder.getStatus() == 1) {
            tvStatusPending.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnProcess.setVisibility(View.VISIBLE);
            lnBottom.setVisibility(View.VISIBLE);


        } else if (detailOrder.getStatus() == 2) {
            tvStatusDelivery.setVisibility(View.VISIBLE);

        } else {
            rlComplete.setVisibility(View.VISIBLE);
            rlPay.setVisibility(View.VISIBLE);
            tvStatusComplete.setVisibility(View.VISIBLE);
        }

    }

    public void showDialogConfirm(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderInformationActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelOrder();
                    }
                });

        builder1.setNegativeButton(
                "Hủy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void cancelOrder() {
        String urlOrder = ORDER + "/" + orderId;
        JSONObject user = new JSONObject();
        try {
            user.put(STATUS, 0);
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlOrder, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("success", response + "");
                ConstantData.showToast("Cập nhật thành công", R.drawable.ic_mark, OrderInformationActivity.this, getWindow().getDecorView().findViewById(android.R.id.content));
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(OrderInformationActivity.this));
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                showDialogConfirm("Hủy đơn hàng?");
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvViewShop:
                openViewShop();
                break;
            case R.id.btnContact:
                sendMessage();
                break;
        }

    }

    private void sendMessage() {
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("phoneOfStore", phoneStore);
        bundle.putString("storeName", storeName);
        intent.putExtras(bundle);
       startActivity(intent);
    }

    private void openViewShop() {
        Intent intent = new Intent(this, ViewShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID_PRODUCT, storeId);
        bundle.putString(STORE_NAME_PRODUCT, storeName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}