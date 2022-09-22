package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.CHANGE_PASS;
import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.STATUS;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.order.shopOrder.OrderShopInformationActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePassFragment extends DialogFragment {
    private View view;
    private EditText edtPass, edtRePass;
    private AppCompatButton btnChangePass;
private ImageView ivBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_change_pass, container);
            initView();
            ivBack.setOnClickListener(v->{
                dismiss();
            });
            changePass();
        }
        return view;
    }

    private void changePass() {
        btnChangePass.setOnClickListener(v -> {
            if (!ValidateForm.validatePassword(edtPass.getText().toString().trim())) {
                Toast.makeText(getContext(), "Mật khẩu từ 8 đến 16 ký tự, ít nhât 1 ký tự hoa, 1 ký tự số, không có khoảng trắng", Toast.LENGTH_SHORT).show();

            } else {
                if (!ValidateForm.checkRePassWord(edtPass.getText().toString().trim(), edtRePass.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Nhập khẩu nhật lại chưa đúng!", Toast.LENGTH_SHORT).show();

                } else {
                    updatePassWord();
                }
            }
        });
    }

    private void initView() {
        edtPass = view.findViewById(R.id.edtPass);
        edtRePass = view.findViewById(R.id.edtRePass);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        ivBack = view.findViewById(R.id.ivBack);

    }

    private void updatePassWord() {
        JSONObject user = new JSONObject();
        ProgressBarDialog.getInstance(getContext()).showDialog("Đợi một lát",getContext());
        try {
            user.put("id", ConstantData.getUserId(getContext()));
            user.put("password", edtPass.getText().toString().trim());
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, CHANGE_PASS, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("success", response + "");
                ConstantData.showToast("Cập nhật thành công", R.drawable.ic_mark, getContext(), view);
                ProgressBarDialog.getInstance(getContext()).closeDialog();
                dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(getContext()).closeDialog();

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

}