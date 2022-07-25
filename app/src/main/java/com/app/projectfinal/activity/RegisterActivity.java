package com.app.projectfinal.activity;

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
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {


    AppCompatButton btn_register;
    TextView tv_login;
    TextInputEditText edt_phone;
    TextInputEditText edt_acc;
    TextInputEditText edt_pass;
    TextInputEditText edt_re_pass;

    //firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        initView();
        changeScreenLogin();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_pass.getText().toString().trim().equals(edt_re_pass.getText().toString())){
                    registerServer(Objects.requireNonNull(edt_phone.getText()).toString().trim(), Objects.requireNonNull(edt_pass.getText()).toString().trim(), Objects.requireNonNull(edt_acc.getText()).toString().trim()) ;
//                    registerFirebase(Objects.requireNonNull(edt_phone.getText()).toString(), Objects.requireNonNull(edt_pass.getText()).toString());
                }else {
                    Toast.makeText(RegisterActivity.this, "" +"Nhập lại mật khẩu", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    private void sendMessageEmail(String email){
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.app.projectfinal",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
        fAuth.sendSignInLinkToEmail(email, actionCodeSettings);
    }

    private void changeScreenLogin(){
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void initView(){
        btn_register = (AppCompatButton) findViewById(R.id.btn_register);
        edt_phone = (TextInputEditText) findViewById(R.id.edt_phone);
        edt_acc = (TextInputEditText) findViewById(R.id.edt_acc);
        edt_pass = (TextInputEditText) findViewById(R.id.edt_pass);
        edt_re_pass = (TextInputEditText) findViewById(R.id.edt_re_pass);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }
     private  void  validateRegister(){

     }
    private void registerServer(final String phone, final String pass, String name){
        JSONObject user = new JSONObject();
        try {
            user.put("phone", phone);
            user.put("password", pass);
            user.put("userName", name);

            JSONObject data = new JSONObject();
            data.put("user", user);
            JSONObject datas = new JSONObject();
            datas.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.REGISTER, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RegisterActivity.this, "" + "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    public boolean checkField(EditText textField){
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (textField.getText().toString().isEmpty()){
            textField.setError("Không được để trống");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private void registerFirebase(String username, String password){
        fAuth.createUserWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = fAuth.getCurrentUser();
                DocumentReference df = fStore.collection("Users").document(user.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", username);
                userInfo.put("password", password);
                df.set(userInfo);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}