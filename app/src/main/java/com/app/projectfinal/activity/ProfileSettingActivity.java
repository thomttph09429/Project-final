package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.DATE_OF_BIRTH;
import static com.app.projectfinal.utils.Constant.EMAIL;
import static com.app.projectfinal.utils.Constant.PHONE_NUMBER;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.AddressActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvUpdateAvatar;
    private Uri uriImage;
    private ImageView ivAvtUser;
    private String linkImageUrlFirebase, userId, dateOfBirth, email;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private LinearLayout lnAddress, lnNameLogin, lnNumberLogin, lnEmail, lnChangePass, lnPolicy, lnRules;
    private EditText edtUserLogin, edtNumberLogin, edtEmail, edtDateOfBirth;
    private AppCompatButton btnSaveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        initView();
        initAction();
        getInfo();

    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void initView() {
        edtUserLogin = findViewById(R.id.edtUserLogin);
        edtNumberLogin = findViewById(R.id.edtNumberLogin);
        edtEmail = findViewById(R.id.edtEmail);

        tvUpdateAvatar = findViewById(R.id.tvUpdateAvatar);
        ivAvtUser = findViewById(R.id.ivAvtUser);
        lnAddress = findViewById(R.id.lnAddress);
        lnNameLogin = findViewById(R.id.lnNameLogin);
        lnNumberLogin = findViewById(R.id.lnNumberLogin);
        lnEmail = findViewById(R.id.lnEmail);
        lnChangePass = findViewById(R.id.lnChangePass);
        lnPolicy = findViewById(R.id.lnPolicy);
        lnRules = findViewById(R.id.lnRules);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);

    }

    private void initAction() {
        tvUpdateAvatar.setOnClickListener(this);
        btnSaveInfo.setOnClickListener(this);
        lnAddress.setOnClickListener(this);
        userId = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_ID_SAVE);

    }

    private void updateAvatar() {
        if (tvUpdateAvatar.getText().equals("Sửa")) {
            openFileChose();

        } else if (tvUpdateAvatar.getText().equals("Cập nhật")) {
            uploadImage();
        }


    }

    private void openFileChose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadImage() {
        storageRef = FirebaseStorage.getInstance().getReference("Posts");
        if (uriImage != null) {
            ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uriImage));

            uploadTask = fileReference.putFile(uriImage);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        linkImageUrlFirebase = downloadUri.toString();
                        Log.e("miannn", linkImageUrlFirebase.toString());

                        ProgressBarDialog.getInstance(ProfileSettingActivity.this).closeDialog();
                        tvUpdateAvatar.setText("Sửa");

                    } else {
                        Toast.makeText(ProfileSettingActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else {
            Toast.makeText(ProfileSettingActivity.this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();

        }
    }

    private void getInfo() {
        edtUserLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE));
        edtNumberLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE_NUMBER));

    }


    private void updateInfo() {
        JSONObject user = new JSONObject();
        String url = UPDATE_USER + "/" + userId;
        try {
            dateOfBirth = edtDateOfBirth.getText().toString().trim();
            email = edtEmail.getText().toString().trim();
            user.put(DATE_OF_BIRTH, dateOfBirth);
            user.put(EMAIL, email);
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                showToast("Cập nhật thành công", R.drawable.ic_mark);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileSettingActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImage = data.getData();
            Glide.with(this).load(uriImage).centerCrop().into(ivAvtUser);
            if (uriImage != null) {
                tvUpdateAvatar.setText("Cập nhật");
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUpdateAvatar:
                updateAvatar();
                break;
            case R.id.btnSaveInfo:
                updateInfo();
                break;
            case R.id.lnAddress:
                openAddressScreen();
                break;

        }

    }

    private void openAddressScreen() {
        Intent intent= new Intent(this, AddressActivity.class);
        startActivity(intent);
    }

    private void showToast(String text, int src) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}