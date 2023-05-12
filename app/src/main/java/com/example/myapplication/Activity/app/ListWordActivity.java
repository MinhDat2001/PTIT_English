package com.example.myapplication.Activity.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Activity.login.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.WordAdapter;
import com.example.myapplication.model.DatabaseAccess;
import com.example.myapplication.model.Word;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

public class ListWordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView lavWord;
    WordAdapter adapter;

    private TextView userEmail, userType;
    private ImageView userAvatar;
    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu navigationMenu;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;
    private String email = "";
    private String avatar = "";
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_word);
        getCurrentUserAndEmail();

        Log.d("Email-main", "onCreate: " + email);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("289927865993-pkdhs2d7iobl16bpoi1plvn4083o9ebg.apps.googleusercontent.com").requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ListWordActivity.this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String topic = bundle.getString("id", "");
            LoadData(topic);
        }

        userEmail = navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        userAvatar = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        userType = navigationView.getHeaderView(0).findViewById(R.id.profile_type);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PTIT-English");

        navigationMenu = navigationView.getMenu();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(ListWordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    email = user.getEmail();
                    if (getUserName() != null) userEmail.setText(getUserName());
                    else userEmail.setText(email);

                    List<? extends UserInfo> providerData = user.getProviderData();
                    for (UserInfo userInfo : providerData) {
                        String providerId = userInfo.getProviderId();
                        if (providerId.equals("firebase")) {
                            type = "Firebase";
                            enableOrDisableMenu(true);
                            getAvatar();
                        } else if (providerId.equals("google.com")) {
                            type = "Google.com";
                            enableOrDisableMenu(false);
                            getAvatar();
                        } else if (providerId.equals("facebook.com")) {
                            type = "Facebook.com";
                            enableOrDisableMenu(false);
                            getAvatar();
                        }
                    }
                    userType.setText(type);
                }
            }
        };
    }

    private void LoadData(String topic) {
        lavWord = findViewById(R.id.listWord);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        List<Word> list = databaseAccess.GetTopic(topic);
        adapter = new WordAdapter(ListWordActivity.this, R.layout.word_item, list);
        lavWord.setAdapter(adapter);
        // databaseAccess.close();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrentUserAndEmail();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
        } else if (requestCode == 2) {
            Toast.makeText(ListWordActivity.this, "Thanks for your response", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 3) {
            getAvatar();
            email = user.getEmail();
            if (getUserName() != null) userEmail.setText(getUserName());
            else userEmail.setText(email);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_logout:
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                signOut();
                finish();
                break;
            case R.id.nav_changePass:
                intent = new Intent(ListWordActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_changeAvatar:
                intent = new Intent(ListWordActivity.this, ChangeAvatarActivity.class);
                startActivityForResult(intent, 3);
                break;
            case R.id.nav_about:
                showAboutDialog();
                break;
            case R.id.nav_feedback:
                String recipientEmail = "dangminhdat@gmail.com";
                String subject = "Feedback for PTIT-English";
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + recipientEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(ListWordActivity.this, "No email application found!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCurrentUserAndEmail() {
        email = "";
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            if (email != null) {
                List<? extends UserInfo> providerData = user.getProviderData();
                for (UserInfo userInfo : providerData) {
                    String providerId = userInfo.getProviderId();
                    if (providerId.equals("firebase")) {
                        email += ".firebase";
                    } else if (providerId.equals("google.com")) {
                        email += ".google";
                    } else if (providerId.equals("facebook.com")) {
                        email += ".facebook";
                    }
                }
            }
        }
    }

    private void getAvatar() {
        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                avatar = user.getPhotoUrl().toString();
                if (!avatar.equals("")) {
                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(ListWordActivity.this).load(avatar).apply(requestOptions).circleCrop().into(userAvatar);
                }
            }
        }
    }

    private String getUserName() {
        if (user != null) return user.getDisplayName();
        return null;
    }

    public void enableOrDisableMenu(boolean check) {
        MenuItem item1 = navigationMenu.findItem(R.id.nav_changePass);
        item1.setEnabled(check).setVisible(check);
        MenuItem item3 = navigationMenu.findItem(R.id.nav_changeAvatar);
        item3.setEnabled(check).setVisible(check);
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Google Sign Out completed
            }
        });
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.alertdialog_about, null);
        builder.setView(customView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
