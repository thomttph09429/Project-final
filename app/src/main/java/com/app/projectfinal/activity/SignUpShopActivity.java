package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_STORE;
import static com.app.projectfinal.utils.Constant.IS_ACTIVE;
import static com.app.projectfinal.utils.Constant.LINK_SUPPORT_STORE;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.AddressActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpShopActivity extends AppCompatActivity {
    private TextView tvCountNameStore ,tvAddress;
    private EditText edtEnterStoreName, edtLinkFace, edtDesStore;
    private Button btnSignUpShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);
        initView();
        checkWordCount();
        addAddress();
        signUpShop();
    }

    /**
     * click button sign up
     * <pre>
     *     author:ThomTT1
     *     date:30/07/2022
     * </pre>
     */
    private void signUpShop() {
        btnSignUpShop.setOnClickListener(v -> {
            String storeName = edtEnterStoreName.getText().toString().trim();
            String linkFace = edtLinkFace.getText().toString().trim();
            String description = edtDesStore.getText().toString().trim();
            String userId = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_ID_SAVE);
            signUpToBecomeSeller(userId, storeName, description, linkFace);

        });
    }


    private void checkWordCount() {
        edtEnterStoreName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int character = charSequence.length();
                tvCountNameStore.setText(character + "/130");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:30/07/2022
     * </pre>
     */
    private void initView() {
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        tvAddress = findViewById(R.id.tvAddress);
        edtLinkFace = findViewById(R.id.edtLinkFace);
        edtEnterStoreName = findViewById(R.id.edtEnterStoreName);
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        btnSignUpShop = findViewById(R.id.btnSignUpShop);
        edtDesStore = findViewById(R.id.edtDesStore);

    }
    private  void addAddress(){
        tvAddress.setOnClickListener(v->{
            startActivity( new Intent(this, AddressActivity.class));
        });
    }

    /**
     * Call API sign up to become seller
     *
     * @param userId
     * @param storeName
     * @param description
     * @param linkSupport
     */
    private void signUpToBecomeSeller(String userId, String storeName, String description, String linkSupport) {
        ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);
        JSONObject user = new JSONObject();
        try {
            user.put(NAME_STORE, ValidateForm.capitalizeFirst(storeName));
            user.put(USER_ID, userId);
            user.put(DESCRIPTION_STORE, ValidateForm.capitalizeFirst(description));
            user.put(LINK_SUPPORT_STORE, linkSupport);
            JSONObject data = new JSONObject();
            data.put("user", user);
            JSONObject datas = new JSONObject();
            datas.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.ADD_STORES, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.e("response", response.toString());
                        JSONObject strRes = response.getJSONObject("data");
                        JSONObject str = strRes.getJSONObject("user");
                        String storeId = str.getString("id");
                        String storeName = str.getString("name");
                        String userId = str.getString("userId");

                        updateStore(storeId, userId);
                        Log.e("storeId", storeId);
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.STORE_ID, storeId);
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.STORE_NAME, storeName);

                        ProgressBarDialog.getInstance(SignUpShopActivity.this).closeDialog();
                        Toast.makeText(SignUpShopActivity.this, "" + "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpShopActivity.this, MyShopActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpShopActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    private void updateStore(String storeId, String userId) {
        JSONObject user = new JSONObject();
        String url = ADD_STORES + "/" + storeId;
        try {
            user.put("userId", userId);
            user.put("isActive", 1);
            JSONObject datas = new JSONObject();
            datas.put("data", user);
            Log.e("ma", user+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("huuuu", response+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpShopActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }
}