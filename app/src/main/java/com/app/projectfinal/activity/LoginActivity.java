package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.LOGIN;
import static com.google.common.base.StandardSystemProperty.USER_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.projectfinal.R;
import com.app.projectfinal.data.MySharedPreferences;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_register;
    private AppCompatButton btn_login_with_google;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private Button btn_login;
    private EditText edt_pass, edt_acc;
    private View parentLayout;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mySharedPreferences = new MySharedPreferences(this);
        initView();
        clickLoginWithServer();
        changeScreenRegister();
    }

    /**
     * click button "login" and validate form login
     * <pre>
     *     author: ThomTT
     *     date: 30/07/2022
     * </pre>
     */
    private void clickLoginWithServer() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edt_acc.getText().toString().trim();
                String passWord = edt_pass.getText().toString().trim();
                if (userName.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Nhập tên đăng nhập hoặc số điện thoại", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!ValidateForm.validatePassword(passWord)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Mật khẩu chứa ít nhất 8 ký tự", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    signInWithServer(userName, passWord);

                }

            }
        });
    }

    /**
     * navigate register screen
     * <pre>
     *     author:ThomTT
     *     date:30/07/2022
     * </pre>
     */
    private void changeScreenRegister() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date: 30/07/2022
     * </pre>
     */
    private void initView() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login_with_google = (AppCompatButton) findViewById(R.id.btn_login_with_google);
        btn_login = findViewById(R.id.btn_login);
        edt_acc = findViewById(R.id.edt_acc);
        edt_pass = findViewById(R.id.edt_pass);
        parentLayout = findViewById(android.R.id.content);

    }


    /**
     * Call API sign in with server
     * <pre>
     *     author:ThomTT1
     *     date:24/07/2022
     * </pre>
     *
     * @param name
     * @param passWord
     */
    private void signInWithServer(String name, String passWord) {
        JSONObject user = new JSONObject();

        try {
            user.put("userNamePhone", name);
            user.put("password", passWord);
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mySharedPreferences.putStringValue(String.valueOf(USER_NAME), name);
                Toast.makeText(LoginActivity.this, "" + "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", "" + response.toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();

            }

        });
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);


    }

}