package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOTAL_ORDER;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.order.myOrder.MyOrderActivity;
import com.app.projectfinal.order.shopOrder.ShopOrderActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyShopActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStoreName, tvViewShop, tvHistory;
    private LinearLayout lnStartSell,lnStatistical, lnContact, lnMyProduct, lnShopSetting, lnWaitConfirm, lnDelivery, lnComplete, lnCancel, lnViewShop;
    public static String storeName, avatar;
    public static String storeId;
    private RelativeLayout rlTotalPending, rlTotalProcess;
    private TextView tvTotalPending, tvTotalProcess;
    private CircleImageView ivAvatar;
    private ImageView ivBack, ivChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        initView();
        initAction();
        getInfoShop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderQuantity();

    }

    private void initAction() {
        lnStartSell.setOnClickListener(this);
        lnMyProduct.setOnClickListener(this);
        lnShopSetting.setOnClickListener(this);
        tvViewShop.setOnClickListener(this);
        lnWaitConfirm.setOnClickListener(this);
        lnDelivery.setOnClickListener(this);
        lnComplete.setOnClickListener(this);
        lnCancel.setOnClickListener(this);
        lnViewShop.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        lnContact.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        lnStatistical.setOnClickListener(this);

    }


    private void initView() {
        tvStoreName = findViewById(R.id.tvStoreName);
        lnStartSell = findViewById(R.id.lnStartSell);
        lnMyProduct = findViewById(R.id.lnMyProduct);
        lnShopSetting = findViewById(R.id.lnShopSetting);
        tvViewShop = findViewById(R.id.tvViewShop);
        lnWaitConfirm = findViewById(R.id.lnWaitConfirm);
        lnDelivery = findViewById(R.id.lnDelivery);
        lnComplete = findViewById(R.id.lnComplete);
        lnCancel = findViewById(R.id.lnCancel);
        lnViewShop = findViewById(R.id.lnViewShop);
        tvHistory = findViewById(R.id.tvHistory);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivBack = findViewById(R.id.ivBack);

        rlTotalPending = findViewById(R.id.rlTotalPending);
        rlTotalProcess = findViewById(R.id.rlTotalProcess);
        tvTotalProcess = findViewById(R.id.tvTotalProcess);
        tvTotalPending = findViewById(R.id.tvTotalPending);
        lnContact = findViewById(R.id.lnContact);
        ivChat = findViewById(R.id.ivChat);
        lnStatistical = findViewById(R.id.lnStatistical);


    }


    private void startSell() {
        Intent intent = new Intent(this, AddProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID_PRODUCT, storeId);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    private void openMyProductScreen() {
        Intent intent = new Intent(this, MyProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID_PRODUCT, storeId);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    private void openShopSettingScreen() {
        Intent intent = new Intent(this, ProfileShopActivity.class);
        startActivity(intent);
    }

    /**
     * receive info of shop and get detail store
     * <pre>
     *     author:ThomTT1
     *     date:18/06/2022
     * </pre>
     */
    private void getInfoShop() {
        Bundle bundle = getIntent().getExtras();
        storeId = bundle.getString(STORE_ID_PRODUCT);
        String urlProducts = ADD_STORES + "/" + storeId;
        ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("store");
                        storeName = data.getString(STORE_NAME_PRODUCT);
                        avatar = data.getString("image1");
                        tvStoreName.setText(storeName);
                        Glide.with(MyShopActivity.this).load(avatar).centerCrop().error(R.drawable.avatar_empty).into(ivAvatar);

                        ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MyShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(MyShopActivity.this).getRequestQueue().add(jsonObjectRequest);
    }


    private void getOrderQuantity() {
        String urlProducts = TOTAL_ORDER + "/" + "?storeId=" + storeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        int cancelOrder = data.getInt("cancelOrder");
                        int newOrder = data.getInt("newOrder");
                        int processingOrder = data.getInt("processingOrder");
                        int finishOrder = data.getInt("finishOrder");


                        //delivery
                        if (processingOrder != 0) {
                            rlTotalProcess.setVisibility(View.VISIBLE);
                            tvTotalProcess.setText(String.valueOf(processingOrder));
                        } else {
                            rlTotalProcess.setVisibility(View.GONE);

                        }

                        //pending
                        if (newOrder != 0) {
                            rlTotalPending.setVisibility(View.VISIBLE);
                            tvTotalPending.setText(String.valueOf(newOrder));
                        } else {
                            rlTotalPending.setVisibility(View.GONE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MyShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(MyShopActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(MyShopActivity.this.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(MyShopActivity.this).getRequestQueue().add(jsonObjectRequest);


    }


    private void openViewShopScreen() {
        Intent intent = new Intent(this, ViewShopActivity.class);
        startActivity(intent);
    }


    private void waitForConfirmation() {
        Intent intent = new Intent(this, ShopOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deliveryOrder() {
        Intent intent = new Intent(this, ShopOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void completeOrder() {
        Intent intent = new Intent(this, ShopOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 2);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    private void cancelOrder() {
        Intent intent = new Intent(this, ShopOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 3);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void contact() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "truongthithom1999@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với NSV");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear...,");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }



    private void openChatScreen() {
        startActivity( new Intent(this, ListChatActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnStartSell:
                startSell();
                break;
            case R.id.lnMyProduct:
                openMyProductScreen();
                break;
            case R.id.lnShopSetting:
                openShopSettingScreen();
                break;
            case R.id.lnViewShop:
                openViewShopScreen();
                break;
            case R.id.lnWaitConfirm:
                waitForConfirmation();
                break;
            case R.id.lnDelivery:
                deliveryOrder();
                break;
            case R.id.lnComplete:
                completeOrder();
                break;
            case R.id.lnCancel:
                cancelOrder();
                break;
            case R.id.tvHistory:
                waitForConfirmation();
                break;
            case R.id.lnContact:
                contact();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivChat:
                openChatScreen();
                break;
            case R.id.lnStatistical:
                startActivity(new Intent(this, StatisticalActivity.class));
                break;

        }
    }




}