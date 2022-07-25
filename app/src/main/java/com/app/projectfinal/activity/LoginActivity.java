package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.LOGIN;

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
import com.app.projectfinal.utils.ProgressBarDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        initView();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edt_acc.getText().toString().trim();
                String passWord = edt_pass.getText().toString().trim();
                signInWithServer(userName, passWord);
//                clickSignInWithFỉrebase(userName, passWord);

            }
        });
        changeScreenRegister();
        loginWithGoogle();
    }

    private void changeScreenRegister() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void initView() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login_with_google = (AppCompatButton) findViewById(R.id.btn_login_with_google);
        btn_login = findViewById(R.id.btn_login);
        edt_acc = findViewById(R.id.edt_acc);
        edt_pass = findViewById(R.id.edt_pass);

    }


private  void validateLogin(){

}
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

                Toast.makeText(LoginActivity.this, "" + "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", ""+response.toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();

            }

        }) ;
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);


    }

    private void clickSignInWithFỉrebase(String userName,String passWord) {
                    fAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, com.app.projectfinal.activity.MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

            }





    public void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
            }
        });
    }

    private void loginWithGoogle(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(LoginActivity.this, gso);
        btn_login_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } catch (ApiException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}