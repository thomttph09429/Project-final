package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.ROLE;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOTAL_ORDER;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.MyShopActivity;
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.activity.SignUpShopActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.order.myOrder.MyOrderActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout lnStartSell, lnSetting, lnWait, lnFinish, lnDelivery, lnCancel;
    private View view;
    private TextView tvMyShop, tvWhenNotSignUp, tvUserName,tvHistory;
    private String isSignUp;
    private TextView tvTotalPending, tvTotalProcess, tvTotalDelivery, tvTotalCancel;
    private RelativeLayout rlTotalPending, rlTotalProcess, rlTotalDelivery, rlTotalCancel;
    private String storeId;

    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        initAction();
        getInformation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isSignUpToBecomeSeller();

    }

    private void initAction() {
        lnWait.setOnClickListener(this);
        tvMyShop.setOnClickListener(this);
        lnStartSell.setOnClickListener(this);
        lnSetting.setOnClickListener(this);
        lnDelivery.setOnClickListener(this);
        lnFinish.setOnClickListener(this);
        lnCancel.setOnClickListener(this);
        tvHistory.setOnClickListener(this);

    }


    /**
     * get user name of user
     */
    private void getInformation() {
        String userName = SharedPrefsSingleton.getInstance(getActivity().getApplicationContext()).getStringValue(USER_NAME_SAVE);
        tvUserName.setText(userName.toString());

    }

    /**
     * pass storeId When click open my shop
     * <pre>
     *     author:ThomTT
     *     date:16/08/2022
     * </pre>
     */
    private void openMyShop() {
        Intent intent = new Intent(getActivity(), MyShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID_PRODUCT, storeId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * check if the user is registered to sell
     * <pre>
     *     author:ThomTT1
     *     date:31/07/2022
     * </pre>
     */
    private void isSignUpToBecomeSeller() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đợi một lát", getContext());
        String urlProducts = UPDATE_USER + "/" + ConstantData.getUserId(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        getOrderQuantity();
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("user");
                        int role = data.getInt(ROLE);
                        storeId = data.getString(STORE_ID_PRODUCT);
                        if (role == 2) {
                            tvWhenNotSignUp.setVisibility(View.GONE);
                            tvMyShop.setVisibility(View.VISIBLE);
                            lnStartSell.setVisibility(View.GONE);
                            ProgressBarDialog.getInstance(getContext()).closeDialog();
                        } else {
                            tvWhenNotSignUp.setVisibility(View.VISIBLE);
                            tvMyShop.setVisibility(View.GONE);
                            lnStartSell.setVisibility(View.VISIBLE);
                            ProgressBarDialog.getInstance(getContext()).closeDialog();

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

    private void clickStartSell() {
        Intent intent = new Intent(getActivity(), SignUpShopActivity.class);
        startActivity(intent);


    }

    /**
     * click setting profile
     */
    private void clickProfileSetting() {
        Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
        startActivity(intent);

    }

    private void getOrderQuantity() {
        String urlProducts = TOTAL_ORDER + "/" + "?userId=" + ConstantData.getUserId(getContext());
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
                        }

                        //pending
                        if (newOrder != 0) {
                            rlTotalPending.setVisibility(View.VISIBLE);
                            tvTotalPending.setText(String.valueOf(newOrder));
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
        tvMyShop = view.findViewById(R.id.tvMyShop);
        lnStartSell = view.findViewById(R.id.lnStartSell);
        tvWhenNotSignUp = view.findViewById(R.id.tvWhenNotSignUp);
        tvUserName = view.findViewById(R.id.tvUserName);
        lnSetting = view.findViewById(R.id.lnSetting);
        lnWait = view.findViewById(R.id.lnWait);
        lnDelivery = view.findViewById(R.id.lnDelivery);
        lnFinish = view.findViewById(R.id.lnFinish);
        lnCancel = view.findViewById(R.id.lnCancel);
        tvTotalPending = view.findViewById(R.id.tvTotalPending);
        tvTotalProcess = view.findViewById(R.id.tvTotalProcess);
        tvTotalDelivery = view.findViewById(R.id.tvTotalDelivery);
        tvTotalCancel = view.findViewById(R.id.tvTotalCancel);
        rlTotalPending = view.findViewById(R.id.rlTotalPending);
        rlTotalProcess = view.findViewById(R.id.rlTotalProcess);
        rlTotalDelivery = view.findViewById(R.id.rlTotalDelivery);
        rlTotalCancel = view.findViewById(R.id.rlTotalCancel);
        tvHistory = view.findViewById(R.id.tvHistory);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMyShop:
                openMyShop();
                break;
            case R.id.lnSetting:
                clickProfileSetting();
                break;
            case R.id.lnStartSell:
                clickStartSell();
                break;
            case R.id.lnWait:
                waitForConfirmation();
                break;
            case R.id.lnDelivery:
                deliveryOrder();
                break;
            case R.id.lnFinish:
                completeOrder();
                break;
            case R.id.lnCancel:
                cancelOrder();
                break;
            case R.id.tvHistory:
                waitForConfirmation();
                break;
            default:
        }

    }

    private void waitForConfirmation() {
        Intent intent = new Intent(getContext(), MyOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deliveryOrder() {
        Intent intent = new Intent(getContext(), MyOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void completeOrder() {
        Intent intent = new Intent(getContext(), MyOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 2);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    private void cancelOrder() {
        Intent intent = new Intent(getContext(), MyOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 3);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}