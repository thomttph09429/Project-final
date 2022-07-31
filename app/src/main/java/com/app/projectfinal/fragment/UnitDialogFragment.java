package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.GET_UNIT;
import static com.app.projectfinal.utils.Constant.ID_UNIT;
import static com.app.projectfinal.utils.Constant.NAME_UNIT;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.UnitAdapter;
import com.app.projectfinal.listener.ListenerSendUnit;
import com.app.projectfinal.listener.ListenerUnit;
import com.app.projectfinal.model.Unit;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UnitDialogFragment extends DialogFragment {
    private RecyclerView rvUnit;
    private UnitAdapter unitAdapter;
    private List<Unit> unitList;
    private ListenerUnit mListenerUnit;
    private ListenerSendUnit mListenerSendUnit;
    private Button btnChooseUnit;
    private View view;
    private String name, unitId;

    public UnitDialogFragment(ListenerSendUnit mListenerSendUnit) {
        this.mListenerSendUnit = mListenerSendUnit;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_unit, container);

        }
        initView();
        getUnits();
        clickChooseUnit();
        mListenerUnit = new ListenerUnit() {
            @Override
            public void onItemClick(String unitName, String id) {
                btnChooseUnit.setEnabled(true);
                name= unitName;
                unitId= id;

            }
        };
        return view;
    }

    private void clickChooseUnit() {
        btnChooseUnit.setOnClickListener(v -> {
            mListenerSendUnit.onClickSave(name,unitId );

            dismiss();

        });

    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:31/07/2022
     * </pre>
     */
    private void initView() {
        rvUnit = view.findViewById(R.id.rvUnit);
        btnChooseUnit = view.findViewById(R.id.btnChooseUnit);
    }

    /**
     * Call api get unit list
     * <pre>
     *     author:ThomTT
     *     date:31/07/2022
     * </pre>
     */
    private void getUnits() {
        unitList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvUnit.setLayoutManager(layoutManager);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_UNIT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("units");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String unitName = object.getString(NAME_UNIT);
                        String unitId = object.getString(ID_UNIT);
                        unitList.add(new Unit(unitName, unitId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
                unitAdapter = new UnitAdapter(unitList, getContext(), mListenerUnit);
                rvUnit.setAdapter(unitAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
    }
}