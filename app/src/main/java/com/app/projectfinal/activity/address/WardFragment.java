package com.app.projectfinal.activity.address;


import static com.app.projectfinal.utils.Constant.CODE_DISTRICT;
import static com.app.projectfinal.utils.Constant.DISTRICT_NAME;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;
import static com.app.projectfinal.utils.Constant.WARD_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.address.WardAdapter;
import com.app.projectfinal.model.address.Wards;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WardFragment extends DialogFragment {
    private View view;
    private WardAdapter wardAdapter;
    private RecyclerView rvWard;
    private List<Wards> wardsList;
    private int codeWard;
    private String provinceNames, districtName, wardName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_ward, container);
            initView();
            initAction();
            receiveCodeProvince();
            getDistrict();


        }
        return view;
    }

    private void initAction() {
        wardsList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvWard.setLayoutManager(manager);

    }

    private void receiveCodeProvince() {
        Bundle bundle = getArguments();
        codeWard = bundle.getInt(CODE_DISTRICT);
        provinceNames = bundle.getString(PROVINCE_NAME);
        districtName = bundle.getString(DISTRICT_NAME);
        wardName = bundle.getString(WARD_NAME);
    }

    private void initView() {
        rvWard = view.findViewById(R.id.rvWard);

    }

    private void getDistrict() {

        String url = "https://provinces.open-api.vn/api/d/" + codeWard + "?depth=2";
        JsonObjectRequest
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray data = jsonObject.getJSONArray("wards");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject district = data.getJSONObject(i);
                        String name = district.getString("name");
                        int code = district.getInt("code");
                        wardsList.add(new Wards(name, code));
                    }

                    wardAdapter = new WardAdapter(getContext(), wardsList, provinceNames, districtName);
                    rvWard.setAdapter(wardAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);


    }
}