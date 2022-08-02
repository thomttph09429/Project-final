package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.projectfinal.R;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        initView();
        initAction();
        isLogin();
        clickLoginWithServer();
        changeScreenRegister();
    }

    private void initAction() {

    }

    /**
     * check if you are logged in
     * <pre>
     *     author:ThomTT
     *     date:31/07/2022
     * </pre>
     */
    private void isLogin() {
        String isSave = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(Constant.USER_ID_SAVE);
        if (!isSave.isEmpty()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }

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
                if (!ValidateForm.isName(userName)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Tên đăng nhập từ 8 ký tự", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!ValidateForm.validatePassword(passWord)) {
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, "Mật khẩu từ 8 đến 16 ký tự, ít nhât 1 ký tự hoa, 1 ký tự số", Snackbar.LENGTH_LONG);
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
        ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);

        try {
            user.put("userNamePhone", ValidateForm.capitalizeFirst(name));
            user.put("password", passWord);
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject strRes = response.getJSONObject("data");
                        String idUser = strRes.getString("id");
                        String userName = strRes.getString("userName");
                        String phoneNumber = strRes.getString("phone");
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.PHONE_NUMBER, phoneNumber);
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.USER_ID_SAVE, idUser);
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.USER_NAME_SAVE, ValidateForm.capitalizeFirst(userName));
                        pushInformationToFirebase(idUser, userName, phoneNumber);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(LoginActivity.this).closeDialog();
                Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();

            }

        });
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);


    }

    /**
     * push information to firebase when login with server is success, if user id already exit, not push
     * <pre>
     *     author:ThomTT
     *     date:02/08/2022
     * </pre>
     * @param idUser
     * @param userName
     * @param phoneNumber
     */
    private void pushInformationToFirebase(String idUser, String userName, String phoneNumber) {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneNumber);
        if (!idUser.equals(reference)) {
            HashMap<String, Object> mUser = new HashMap<>();
            mUser.put("userName", userName);
            mUser.put("id", idUser);
            mUser.put("phone", phoneNumber);
            reference.setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ProgressBarDialog.getInstance(LoginActivity.this).closeDialog();
                        Toast.makeText(LoginActivity.this, "" + "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

}