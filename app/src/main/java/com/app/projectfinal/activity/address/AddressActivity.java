package com.app.projectfinal.activity.address;

import static com.app.projectfinal.utils.Constant.ADDRESS;
import static com.app.projectfinal.utils.Constant.CATEGORY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ViewByCategoryAdapter;
import com.app.projectfinal.adapter.address.ListAddressAdapter;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.model.address.Province;
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

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lnAddress;
    private RecyclerView rvAddress;
    public List<AddressUser> addressUserList;
    private ListAddressAdapter listAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        initAction();
        getAllMyAddress();
    }

    private void initAction() {
        lnAddress.setOnClickListener(this::onClick);
        addressUserList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAddress.setLayoutManager(layoutManager);
    }

    private void initView() {
        lnAddress = findViewById(R.id.lnAddress);
        rvAddress = findViewById(R.id.rvAddress);
    }

    private void openAddAddressScreen() {
        startActivity(new Intent(this, AddAddressActivity.class));
    }

    private void getAllMyAddress() {
        ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
        String url = ADDRESS + "?userId=" + ConstantData.getUserId(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        AddressUser addressUser = gson.fromJson(String.valueOf(object), AddressUser.class);
                        addressUserList.add(addressUser);
                        listAddressAdapter = new ListAddressAdapter(AddressActivity.this, addressUserList);
                        rvAddress.setAdapter(listAddressAdapter);

                    }
                    ProgressBarDialog.getInstance(AddressActivity.this).closeDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddressActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    ProgressBarDialog.getInstance(AddressActivity.this).closeDialog();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(AddressActivity.this
                ).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(AddressActivity.this.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnAddress:
                openAddAddressScreen();
                break;
        }

    }
}