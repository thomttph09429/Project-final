package com.app.projectfinal.activity.address;

import static com.app.projectfinal.utils.Constant.ADDRESS;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.address.ChooseAddressAdapter;
import com.app.projectfinal.adapter.address.ListAddressAdapter;
import com.app.projectfinal.listener.ListenerChooseAddress;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChangeAddressFragment extends DialogFragment {
    private View view;
    private RecyclerView rvAddress;
    public List<AddressUser> addressUserList;
    private ChooseAddressAdapter chooseAddressAdapter;
    private ListenerChooseAddress mListenerChooseAddress;
    private OnInputListener mOnInputListener;
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
            view = inflater.inflate(R.layout.fragment_change_address, container);
            initView();
            initAction();
            exit();
            chooseAddress();
            getAllMyAddress();
            mListenerChooseAddress = new ListenerChooseAddress() {
                @Override
                public void onItemClick(String location, String id) {
                    mOnInputListener.sendInfoAddress(location, id);
                    dismiss();
                }
            };
        }
        return view;
    }



    private void initAction() {
        addressUserList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvAddress.setLayoutManager(layoutManager);
    }

    private void initView() {
        rvAddress = view.findViewById(R.id.rvAddress);
        ivBack = view.findViewById(R.id.ivBack);

    }

    private void exit(){
        ivBack.setOnClickListener(v->{
            dismiss();
        });
    }
    private void chooseAddress() {
    }
    private void getAllMyAddress() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đang tải", getContext());
        String url = ADDRESS + "?userId=" + ConstantData.getUserId(getContext().getApplicationContext());
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
                        chooseAddressAdapter = new ChooseAddressAdapter(getContext(), addressUserList, mListenerChooseAddress);
                        rvAddress.setAdapter(chooseAddressAdapter);
                        ProgressBarDialog.getInstance(getContext()).closeDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    ProgressBarDialog.getInstance(getContext()).closeDialog();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONObject errors = data.getJSONObject("error");
                    String message = errors.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }                ProgressBarDialog.getInstance(getContext()
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

    public interface OnInputListener {
        void sendInfoAddress(String location, String id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {

        }
    }
}