package com.app.projectfinal.activity.address;

import static com.app.projectfinal.utils.Constant.PROVINCE;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.address.ProvinceAdapter;
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


public class ProvinceFragment extends DialogFragment {

    private View view;
    private ProvinceAdapter provinceAdapter;
    private RecyclerView rvProvince;
    private List<Province> provinces;
    private ImageView ivBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_province, container);
            initView();
            exit();
            initAction();
            getProvince();


        }
        return view;
    }

    private void initAction() {
        provinces = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvProvince.setLayoutManager(manager);

    }

    private void initView() {
        rvProvince = view.findViewById(R.id.rvProvince);
        ivBack = view.findViewById(R.id.ivBack);

    }
    private void exit(){
        ivBack.setOnClickListener(v->{
            dismiss();
        });
    }

    private void getProvince() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đang tải", getContext());


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, PROVINCE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Province province = gson.fromJson(String.valueOf(jsonObject), Province.class);
                        provinces.add(province);
                        ProgressBarDialog.getInstance(getContext()).closeDialog();
                    }

                    provinceAdapter = new ProvinceAdapter(getContext(), provinces);
                    rvProvince.setAdapter(provinceAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonArrayRequest);


    }

}