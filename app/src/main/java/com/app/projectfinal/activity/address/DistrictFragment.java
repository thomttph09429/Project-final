package com.app.projectfinal.activity.address;


import static com.app.projectfinal.utils.Constant.CODE_PROVINCE;
import static com.app.projectfinal.utils.Constant.PROVINCE;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.address.DistrictAdapter;
import com.app.projectfinal.adapter.address.ProvinceAdapter;
import com.app.projectfinal.model.address.Districts;
import com.app.projectfinal.model.address.Province;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistrictFragment extends DialogFragment {
    private View view;
    private DistrictAdapter districtAdapter;
    private RecyclerView rvDistrict;
    private List<Districts> districtsList;
    private int codeProvince;
    private  String provinceName;
    private ImageView ivBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_district, container);
            initView();
            initAction();
            exit();

            receiveCodeProvince();
            getDistrict();

        }
        return view;
    }

    private void initAction() {
        districtsList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvDistrict.setLayoutManager(manager);

    }

    private void receiveCodeProvince() {
        Bundle bundle = getArguments();
        codeProvince = bundle.getInt(CODE_PROVINCE);
        provinceName = bundle.getString(PROVINCE_NAME);
        Log.e("code", codeProvince + "");

    }

    private void initView() {
        rvDistrict = view.findViewById(R.id.rvDistrict);
        ivBack = view.findViewById(R.id.ivBack);

    }
    private void exit(){
        ivBack.setOnClickListener(v->{
            dismiss();
        });
    }
    private void getDistrict() {

        String url = "https://provinces.open-api.vn/api/p/" + codeProvince + "?depth=2";
        JsonObjectRequest
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray data = jsonObject.getJSONArray("districts");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject district = data.getJSONObject(i);
                        String name =district.getString("name");
                        int code = district.getInt("code");
                        districtsList.add(new Districts(name, code));
                    }

                    districtAdapter = new DistrictAdapter(getContext(), districtsList, provinceName);
                    rvDistrict.setAdapter(districtAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}