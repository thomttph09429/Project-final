package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.ROLE;
import static com.app.projectfinal.utils.Constant.STORE_ID;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.TOKEN;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.app.projectfinal.activity.AddProductActivity;
import com.app.projectfinal.activity.MyShopActivity;
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.activity.SignUpShopActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.myOrder.MyOrderActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {
    private LinearLayout lnStartSell, lnSetting, lnWait;
    private View view;
    private TextView tvMyShop, tvWhenNotSignUp, tvUserName;
    private String isSignUp;
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
        openMyShop();
        getInformation();
        clickProfileSetting();
        isSignUpToBecomeSeller();
        waitForConfirmation();

        return view;
    }

    private void waitForConfirmation() {
        lnWait.setOnClickListener(v->{
            startActivity(new Intent(getContext(), MyOrderActivity.class));
        });
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
        tvMyShop.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyShopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(STORE_ID_PRODUCT, storeId);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    /**
     * check if the user is registered to sell
     * <pre>
     *     author:ThomTT1
     *     date:31/07/2022
     * </pre>
     */
    private void isSignUpToBecomeSeller() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đợi một lát",getContext());
        String userId = SharedPrefsSingleton.getInstance(getContext().getApplicationContext()).getStringValue(USER_ID_SAVE);
        String urlProducts = UPDATE_USER + "/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("user");
                        int role = data.getInt(ROLE);
                         storeId= data.getString(STORE_ID_PRODUCT);
                        if (role == 2 ) {
                            tvWhenNotSignUp.setVisibility(View.GONE);
                            tvMyShop.setVisibility(View.VISIBLE);
                            lnStartSell.setVisibility(View.GONE);
                            ProgressBarDialog.getInstance(getContext()).closeDialog();
                        }else {
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
        }){
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
        lnStartSell.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpShopActivity.class);
            startActivity(intent);

        });


    }

    /**
     * click setting profile
     */
    private void clickProfileSetting() {
        lnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
            startActivity(intent);
        });

    }

    private void initView() {
        tvMyShop = view.findViewById(R.id.tvMyShop);
        lnStartSell = view.findViewById(R.id.lnStartSell);
        tvWhenNotSignUp = view.findViewById(R.id.tvWhenNotSignUp);
        tvUserName = view.findViewById(R.id.tvUserName);
        lnSetting = view.findViewById(R.id.lnSetting);
        lnWait = view.findViewById(R.id.lnWait);

    }

    @Override
    public void onResume() {
        super.onResume();
        clickStartSell();
    }
}