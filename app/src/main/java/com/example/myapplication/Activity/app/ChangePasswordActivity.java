package com.example.myapplication.Activity.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPass, newPass;
    private Button btnChange, btnBack;
    private FirebaseAuth auth;

    private FirebaseUser user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_in_app);

        oldPass = (EditText) findViewById(R.id.oldPass);
        newPass = (EditText) findViewById(R.id.newPass);
        btnChange = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = oldPass.getText().toString().trim();

                String newPassword = newPass.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

                Log.d("ChangePass", user.getEmail() + " " + oldPassword);

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplication(), "Change password successfully!", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplication(), "Error, please try again!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        } else {
                            if (TextUtils.isEmpty(oldPassword)) {
                                Toast.makeText(getApplication(), "Enter your old password", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Toast.makeText(getApplication(), "Please check your old password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });

                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "Error, please try again!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }

}