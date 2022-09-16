package com.app.projectfinal.fragment;

import static com.app.projectfinal.activity.MainActivity.status;
import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.activity.MainActivity.total;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOTAL_ORDER;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.app.projectfinal.activity.CartActivity;
import com.app.projectfinal.activity.ListChatActivity;
import com.app.projectfinal.activity.MyShopActivity;
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.activity.SignUpShopActivity;
import com.app.projectfinal.model.UserDetail;
import com.app.projectfinal.order.myOrder.MyOrderActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout lnShareApp, lnStartSell, lnSetting, lnWait, lnFinish, lnDelivery, lnCancel, lnContact;
    private View view;
    private TextView tvMyShop, tvWhenNotSignUp, tvUserName, tvHistory;
    private String isSignUp;
    private TextView tvTotalPending, tvTotalProcess, tvTotalDelivery, tvTotalCancel;
    private RelativeLayout rlTotalPending, rlTotalProcess, rlTotalDelivery, rlTotalCancel;
    private ImageView ivMessage, ivCart;
    private CircleImageView ivAvatar;
    public static UserDetail userDetail;

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
        getOrderQuantity();


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
        ivCart.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        lnContact.setOnClickListener(this);
        lnShareApp.setOnClickListener(this);

    }


    /**
     * get user name of user
     */
    private void getInformation() {
        tvUserName.setText(userDetail.getUserName());
        Glide.with(getContext()).load(userDetail.getImage1()).error(R.drawable.avatar_empty).into(ivAvatar);

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
        if (total != 0) {


            if (status == 1) {
                tvWhenNotSignUp.setVisibility(View.GONE);
                tvMyShop.setVisibility(View.VISIBLE);
                lnStartSell.setVisibility(View.GONE);
            } else if (status == 0) {
                tvWhenNotSignUp.setVisibility(View.VISIBLE);
                tvMyShop.setVisibility(View.GONE);
                lnStartSell.setVisibility(View.VISIBLE);
                tvWhenNotSignUp.setText("Đang đợi phê duyệt");
                lnStartSell.setClickable(false);

            } else {
                tvWhenNotSignUp.setVisibility(View.VISIBLE);
                tvMyShop.setVisibility(View.GONE);
                lnStartSell.setVisibility(View.VISIBLE);

            }
        } else {

        }

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

    private void getInfoUser() {
        String url = UPDATE_USER + "/" + ConstantData.getUserId(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONObject data = jsonObject.getJSONObject("user");
                    Gson gson = new Gson();
                    userDetail = gson.fromJson(String.valueOf(data), UserDetail.class);
                    getInformation();
                    ProgressBarDialog.getInstance(getContext()).closeDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    ProgressBarDialog.getInstance(getContext()).closeDialog();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(getContext()
                ).closeDialog();

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

    private void getOrderQuantity() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đợi 1 lát", getContext());
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
                        getInfoUser();

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
        ivMessage = view.findViewById(R.id.ivMessage);
        ivCart = view.findViewById(R.id.ivCart);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        lnContact = view.findViewById(R.id.lnContact);
        lnShareApp = view.findViewById(R.id.lnShareApp);


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
            case R.id.ivCart:
                clickCart();
                break;
            case R.id.ivMessage:
                clickMessage();
                break;
            case R.id.lnContact:
                contact();
                break;
            case R.id.lnShareApp:
                shareApp();
                break;
            default:
        }

    }

    private void shareApp() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1np3WhFkIHMcoqftCsHWLP6vzOUh0wlLg?usp=sharing"));
        startActivity(browserIntent);
    }

    private void contact() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "truongthithom1999@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với NSV");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear...,");

        try {
            getContext().startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickMessage() {
        Intent intent = new Intent(getActivity(), ListChatActivity.class);
        startActivity(intent);


    }

    private void clickCart() {
        Intent intent = new Intent(getActivity(), CartActivity.class);
        startActivity(intent);
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