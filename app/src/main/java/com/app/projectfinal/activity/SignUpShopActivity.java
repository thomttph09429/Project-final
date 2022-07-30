package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.projectfinal.R;

public class SignUpShopActivity extends AppCompatActivity {
    private TextView tvCountNameStore;
    private EditText edtEnterStoreName, edtLinkFace, edtEnterAddress;
    private Button btnSignUpShop,btnAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);
        initView();
        checkWordCount();
        signUpShop();
    }

    private void signUpShop() {
        btnSignUpShop.setOnClickListener(v -> {

        });
    }

    private void checkWordCount() {
        edtEnterStoreName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int character = charSequence.length();
                tvCountNameStore.setText(character + "/130");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:30/07/2022
     * </pre>
     */
    private void initView() {
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        edtEnterAddress = findViewById(R.id.edtEnterAddress);
        edtLinkFace = findViewById(R.id.edtLinkFace);
        edtEnterStoreName = findViewById(R.id.edtEnterStoreName);
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        btnSignUpShop = findViewById(R.id.btnSignUpShop);
        btnAddImage=findViewById(R.id.btnAddImage);

    }
}