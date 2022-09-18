package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.LOGIN;
import static com.app.projectfinal.utils.Constant.PHONE;
import static com.app.projectfinal.utils.Constant.TOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
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
import com.app.projectfinal.R;
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_register, tvFogetPass;
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
        tvFogetPass.setOnClickListener(v -> {
            Toast.makeText(this, "Liên hệ quản trị viên để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + "truongthithom1999@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với NSV");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear...,");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * check if you are logged in
     * <pre>
     *     author:ThomTT
     *     date:31/07/2022
     *     TODO
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
        btn_login = findViewById(R.id.btn_login);
        edt_acc = findViewById(R.id.edt_acc);
        edt_pass = findViewById(R.id.edt_pass);
        parentLayout = findViewById(android.R.id.content);
        tvFogetPass = findViewById(R.id.tvFogetPass);

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
                        String token = strRes.getString("token");
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(TOKEN, token);
                        Log.e("token", token);
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

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONObject errors = data.getJSONObject("error");
                    String message = errors.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                ProgressBarDialog.getInstance(LoginActivity.this).closeDialog();


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
     *
     * @param idUser
     * @param userName
     * @param phoneNumber
     */
    private void pushInformationToFirebase(String idUser, String userName, String phoneNumber) {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneNumber);
//        if (!idUser.equals(reference)) {
//            HashMap<String, Object> mUser = new HashMap<>();
//            mUser.put("userName", userName);
//            mUser.put("id", idUser);
//            mUser.put("phone_number", phoneNumber);
//            reference.setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        ProgressBarDialog.getInstance(LoginActivity.this).closeDialog();
//                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(PHONE, phoneNumber);
//                        Toast.makeText(LoginActivity.this, "" + "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            });
//
//        }
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child("userName").setValue(userName);
                reference.child("id").setValue(idUser);
                reference.child("phone_number").setValue(phoneNumber);
                reference.child("avatar").setValue("https://firebasestorage.googleapis.com/v0/b/final-project-4edd1.appspot.com/o/Posts%2F1661063588056.jpg?alt=media&token=809bf0c0-5167-43db-b511-fc2acb32ca4a");
                reference.child("name_store").setValue("Người dùng");

                ProgressBarDialog.getInstance(LoginActivity.this).closeDialog();
                SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(PHONE, phoneNumber);
                Toast.makeText(LoginActivity.this, "" + "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}