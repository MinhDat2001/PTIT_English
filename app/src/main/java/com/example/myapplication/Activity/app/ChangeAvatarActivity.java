package com.example.myapplication.Activity.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ChangeAvatarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText email;
    ImageView avatar;
    Button choose, set, back;
    ProgressBar progressBar;
    Uri imageUri = null;
    FirebaseStorage storage;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);




        storage = FirebaseStorage.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("ChangeAvatarActivity", "onCreate: " + user.getEmail());

        email = findViewById(R.id.profile_email);
        avatar = findViewById(R.id.profile_image);
        set = findViewById(R.id.set);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);


        email.setText(user.getEmail());

        getAvatar();


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), PICK_IMAGE_REQUEST);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                imageUri = uri;
                Glide.with(ChangeAvatarActivity.this).load(uri).circleCrop().into(avatar);
                String userName = email.getText().toString();
                uploadProfile(userName);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        String fileName = UUID.randomUUID().toString();

        StorageReference storageRef = storage.getReference().child("avatars/" + fileName);

        UploadTask uploadTask = storageRef.putFile(imageUri);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String avatarUrl = uri.toString();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(avatarUrl)).build();

                            if (user != null) {
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangeAvatarActivity.this, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        } else {
                                            Toast.makeText(ChangeAvatarActivity.this, "Cập nhật avatar thất bại", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangeAvatarActivity.this, "Lỗi khi tải hình ảnh lên Firebase Storage", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void uploadProfile(String name) {
        progressBar.setVisibility(View.VISIBLE);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangeAvatarActivity.this, "Cập nhật tên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangeAvatarActivity.this, "Cập nhật tên không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAvatar() {
        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                String img = user.getPhotoUrl().toString();
                if (!img.equals("")) {
                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(ChangeAvatarActivity.this).load(img).apply(requestOptions).circleCrop().into(avatar);
                }
            }
        }
    }

}
