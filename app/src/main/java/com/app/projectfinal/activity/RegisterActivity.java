package com.app.projectfinal.activity;


import static com.app.projectfinal.utils.Constant.SEND_NAME;
import static com.app.projectfinal.utils.Constant.SEND_PASS;
import static com.app.projectfinal.utils.Constant.SENT_PHONE_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.projectfinal.R;
import com.app.projectfinal.bottom.ChooseCartFragment;
import com.app.projectfinal.fragment.EnterAuthenticationOTPFragment;
import com.app.projectfinal.fragment.ListCategoryDialogFragment;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private AppCompatButton btn_register;
    private TextView tv_login;
    private TextInputEditText edt_phone, edt_acc, edt_pass, edt_re_pass;
    //firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private View parentLayout;
    private boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        initView();
        changeScreenLogin();
        clickRegisterWithServer();

    }

    /**
     * click button "sign up" and validate form sign up
     * <pre>
     *     author:ThomTT1
     *     date:24/07/2022
     * </pre>
     */
    private void clickRegisterWithServer() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_acc.getText().toString().trim();
                String phone = edt_phone.getText().toString().trim();
                String pass = edt_pass.getText().toString().trim();
                String rePass = edt_re_pass.getText().toString().trim();
                if (!ValidateForm.isName(name)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Tên đăng nhập từ 8 ký tự", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!ValidateForm.isPhoneNumber(phone)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Hãy nhập số điện thoại 10 số", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!ValidateForm.validatePassword(pass)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Mật khẩu từ 8 đến 16 ký tự, ít nhât 1 ký tự hoa, 1 ký tự số và không có khoảng trắng", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!ValidateForm.checkRePassWord(pass, rePass)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Nhập lại mật khẩu", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
//                    registerServer(phone, pass, name);

                    Bundle bundle = new Bundle();
                    bundle.putString(SENT_PHONE_NUMBER, edt_phone.getText().toString().trim());
                    bundle.putString(SEND_NAME, edt_acc.getText().toString().trim());
                    bundle.putString(SEND_PASS, edt_pass.getText().toString().trim());
                    EnterAuthenticationOTPFragment send = new EnterAuthenticationOTPFragment();
                    send.setArguments(bundle);
                    send.show(getSupportFragmentManager(), "EnterAuthenticationOTPFragment");

                }


            }
        });
    }


    /**
     * navigate login screen
     * <pre>
     *     author:ThomTT1
     *     date:24/07/2022
     * </pre>
     */
    private void changeScreenLogin() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date: 24/07/2022
     * </pre>
     */
    private void initView() {
        btn_register = findViewById(R.id.btn_register);
        edt_phone = findViewById(R.id.edt_phone);
        edt_acc = findViewById(R.id.edt_acc);
        edt_pass = findViewById(R.id.edt_pass);
        edt_re_pass = findViewById(R.id.edt_re_pass);
        tv_login = findViewById(R.id.tv_login);
        parentLayout = findViewById(android.R.id.content);
    }


}