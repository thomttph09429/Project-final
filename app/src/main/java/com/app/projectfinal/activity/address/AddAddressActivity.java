package com.app.projectfinal.activity.address;

import static com.app.projectfinal.utils.Constant.ADDRESS;
import static com.app.projectfinal.utils.Constant.CODE_PROVINCE;
import static com.app.projectfinal.utils.Constant.DISTRICT_NAME;
import static com.app.projectfinal.utils.Constant.LOGIN;
import static com.app.projectfinal.utils.Constant.PHONE_NUMBER;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;
import static com.app.projectfinal.utils.Constant.WARD_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.LoginActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rlAddress;
    private TextView tvAddress;
    private String districtName, provinceName, wardName;
    private Bundle bundle = null;
    private AppCompatButton btnSaveAddress;
    private EditText edtEnterAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();
        initAction();
        getAddress();
        saveAddress();
    }

    private void saveAddress() {
        btnSaveAddress.setOnClickListener(v -> {
            JSONObject address = new JSONObject();
            ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
            String userName = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE);
            String phone = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE_NUMBER);
            String addressDetail =edtEnterAddress.getText().toString().trim();
            String province= tvAddress.getText().toString();
            try {
                address.put("type", "");
                address.put("default", "");
                address.put("storeId", "");
                address.put("customerName", userName);
                address.put("phone", phone);
                address.put("location", province+ addressDetail);
                JSONObject data = new JSONObject();
                data.put("address", address);
                JSONObject product = new JSONObject();
                product.put("data", data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADDRESS, address, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ProgressBarDialog.getInstance(AddAddressActivity.this).closeDialog();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddAddressActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    ProgressBarDialog.getInstance(AddAddressActivity.this).closeDialog();


                }

            });
            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);

        });
    }

    private void initAction() {
        rlAddress.setOnClickListener(this);
    }

    private void initView() {
        rlAddress = findViewById(R.id.rlAddress);
        tvAddress = findViewById(R.id.tvAddress);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        edtEnterAddress = findViewById(R.id.edtEnterAddress);
    }

    private void getAddress() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            districtName = bundle.getString(DISTRICT_NAME);
            provinceName = bundle.getString(PROVINCE_NAME);
            wardName = bundle.getString(WARD_NAME);
            tvAddress.setText(provinceName + ", " + districtName + ", " + wardName);

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlAddress:
                ProvinceFragment addressFragment = new ProvinceFragment();
                addressFragment.show(getSupportFragmentManager(), "getSupportFragmentManager");
                break;
        }
    }


}