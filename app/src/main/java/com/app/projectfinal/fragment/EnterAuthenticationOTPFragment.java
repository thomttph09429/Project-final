package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.SEND_NAME;
import static com.app.projectfinal.utils.Constant.SEND_PASS;
import static com.app.projectfinal.utils.Constant.SENT_PHONE_NUMBER;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.LoginActivity;
import com.app.projectfinal.activity.RegisterActivity;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class EnterAuthenticationOTPFragment extends DialogFragment {
    private View view;
    private EditText edtEnterOTP;
    private TextView tvResentOTP;
    private AppCompatButton btnConfirm;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String codeId;
    private String name, pass, phoneNumber;
    private String phone;
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
            view = inflater.inflate(R.layout.fragment_enter_authetication_o_t_p, container);
            initView();
            initAction();
            receivePhoneNumber();
            clickButtonSignUp();
            clickResent();
            clickBack();
        }
        return view;
    }

    private void initAction() {
        mAuth = FirebaseAuth.getInstance();


    }

    /**
     * init vỉew
     * <pre>
     *     author:ThomTT
     *     date:02/08/2022
     * </pre>
     */
    private void initView() {
        edtEnterOTP = view.findViewById(R.id.edtEnterOTP);
        tvResentOTP = view.findViewById(R.id.tvResentOTP);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        ivBack = view.findViewById(R.id.ivBack);

    }

    /**
     * receive phone number from RegisterActivity
     */
    private void receivePhoneNumber() {
        phoneNumber = getArguments().getString(SENT_PHONE_NUMBER);
        name = getArguments().getString(SEND_NAME);
        pass = getArguments().getString(SEND_PASS);
        phone = "+84" + phoneNumber;
        sendVerificationCode(phone);

    }

    /**
     * sign in with firebase
     * <pre>
     *     author:ThomTT
     *     date:02/08/2022
     * </pre>
     *
     * @param credential
     */
    private void signInWithCredential(PhoneAuthCredential credential) {
        //inside this method we are checking if the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ProgressBarDialog.getInstance(getContext()).closeDialog();
                            Toast.makeText(getContext(), "" + "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                        } else {
                            //if the code is not correct then we are displaying an error message to the user.
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            ProgressBarDialog.getInstance(getContext()).closeDialog();

                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        //this method is used for getting OTP on user phone number.

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,//first parameter is user's mobile number
                60,//second parameter is time limit for OTP verification which is 60 seconds in our case.
                TimeUnit.SECONDS,// third parameter is for initializing units for time period which is in seconds in our case.
                getActivity(),//this task will be excuted on Main thread.
                mCallBack//we are calling callback method when we recieve OTP for auto verification of user.
        );

    }

    //callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            //initializing our callbacks for on verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //below method is used when OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
            verificationId = s;
        }

        //this method is called when user recieve OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //below line is used for getting OTP code which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            //checking if the code is null or not.
            if (code != null) {

                edtEnterOTP.setText(code);
                //after setting this code to OTP edittext field we are calling our verifycode method.
                codeId = code;

            }

        }

        //thid method is called when firebase doesnot sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            //displaying error message with firebase exception.
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    };

    /**
     * Call API sign up with server
     * if sign in with server is success, send request to firebase
     * <pre>
     *     author:ThomTT
     *     date:24/07/2022
     * </pre>
     *
     * @param phone
     * @param pass
     * @param name
     */
    private void registerServer(final String phone, final String pass, String name) {
        JSONObject user = new JSONObject();
        try {
            user.put("phone", phone);
            user.put("password", pass);
            user.put("userName", ValidateForm.capitalizeFirst(name));

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
                if (response != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeId);
                    signInWithCredential(credential);

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
                }

                ProgressBarDialog.getInstance(getContext()).closeDialog();
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * press button confirm
     * check if code is valid or not, if code is valid, register with server
     * <pre>
     *     author:ThomTT1
     *     date:02/08/2022
     * </pre>
     */
    private void clickButtonSignUp() {

        btnConfirm.setOnClickListener(v -> {
            //below method is use to verify code from Firebase.
            //below line is used for getting getting credentials from our verification id and code.
            if (edtEnterOTP.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập mã!", Toast.LENGTH_LONG).show();
            } else if (!edtEnterOTP.getText().toString().trim().equals(codeId)) {
                Toast.makeText(getContext(), "Bạn đã nhập sai mã!", Toast.LENGTH_LONG).show();

            } else {
                ProgressBarDialog.getInstance(getContext()).showDialog("Đợi một lát", getContext());
                registerServer(phoneNumber, pass, ValidateForm.capitalizeFirst(name));
                Log.e("heheheeee", phoneNumber + "" + pass + "" + name + "");

            }


        });

    }

    /**
     * click resent code
     * <pre>
     *     author:ThomTT
     *     date:02/08/2022
     * </pre>
     */
    private void clickResent() {
        tvResentOTP.setOnClickListener(v -> {
            sendVerificationCode(phone);

        });

    }

    /**
     * click image back
     * <pre>
     *     author:ThomTT
     *     date:02/08/2022
     * </pre>
     */
    private void clickBack() {
        ivBack.setOnClickListener(v -> {
            dismiss();
        });
    }
}