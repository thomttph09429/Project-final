package com.app.projectfinal.activity.address;

import static com.app.projectfinal.utils.Constant.ADDRESS;
import static com.app.projectfinal.utils.Constant.DISTRICT_NAME;
import static com.app.projectfinal.utils.Constant.PHONE_NUMBER;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;
import static com.app.projectfinal.utils.Constant.WARD_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rlAddress;
    private TextView tvAddress;
    private String districtName, provinceName, wardName;
    private Bundle bundle = null;
    private AppCompatButton btnSaveAddress;
    private EditText edtEnterAddress;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();
        initAction();
        exit();
        getAddress();
        saveAddress();
    }

    private void saveAddress() {
        btnSaveAddress.setOnClickListener(v -> {
            if (tvAddress.getText().toString().equals("Tỉnh/Thành phố, Quận/Huyện, Phường/Xã") || edtEnterAddress.getText().toString().equals("")) {
                showToast("Bạn chưa nhập địa chỉ", R.drawable.ic_priority);
            } else {


                JSONObject address = new JSONObject();
                ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
                String userName = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE);
                String phone = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE_NUMBER);
                String addressDetail = edtEnterAddress.getText().toString().trim();
                String province = tvAddress.getText().toString();

                try {

                    address.put("storeId", "");
                    address.put("customerName", userName);
                    address.put("phone", phone);
                    address.put("location", province + " " + addressDetail);
                    JSONObject data = new JSONObject();
                    data.put("address", address);
                    JSONObject product = new JSONObject();
                    product.put("data", data);
                    showToast("Thêm thành công", R.drawable.ic_mark);
                    finish();
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

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                        return headers;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
            }
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
        ivBack = findViewById(R.id.ivBack);

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

    private void exit() {
        ivBack.setOnClickListener(v -> {
            finish();
        });
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


    private void showToast(String text, int src) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(this.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}