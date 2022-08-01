package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.PHONE_NUMBER;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ProfileSettingActivity extends AppCompatActivity {
    private TextView tvUserLogin,tvNumberLogin, tvUpdateAvatar;
    private Uri uriImage;
    private ImageView ivAvtUser;
    private String linkImageUrlFirebase;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        initView();
        getInfo();
        updateAvatar();

    }

    private void updateAvatar() {
        tvUpdateAvatar.setOnClickListener(v->{
            if (tvUpdateAvatar.getText().equals("Sửa")){
                openFileChose();

            }else if (tvUpdateAvatar.getText().equals("Cập nhật")){
                uploadImage();
            }


        });
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
        tvUserLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE));
        tvNumberLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE_NUMBER));

    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void initView() {
        tvUserLogin=findViewById(R.id.tvUserLogin);
        tvNumberLogin=findViewById(R.id.tvNumberLogin);
        tvUpdateAvatar=findViewById(R.id.tvUpdateAvatar);
        ivAvtUser=findViewById(R.id.ivAvtUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImage = data.getData();
            Glide.with(this).load(uriImage).centerCrop().into(ivAvtUser);
            if (uriImage!=null){
                tvUpdateAvatar.setText("Cập nhật");
            }

        }
    }
}