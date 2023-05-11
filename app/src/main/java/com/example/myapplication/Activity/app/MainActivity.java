package com.example.myapplication.Activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.FragmentSearchFragment;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentSearchFragment fragmentSearchFragment = new FragmentSearchFragment();
        fragmentTransaction.replace(R.id.framecontent, fragmentSearchFragment);
        fragmentTransaction.commit();
    }

    public void onTopic(View view) {
        Intent intent = new Intent(MainActivity.this, TopicActivity.class);
        startActivity(intent);

    }

    public void onSearch2(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentSearchFragment fragmentSearchFragment = new FragmentSearchFragment();
        fragmentTransaction.replace(R.id.framecontent, fragmentSearchFragment);
        fragmentTransaction.commit();
    }

    public void onHomeWork(View view) {
        Intent intent = new Intent(MainActivity.this, HomeWorkActivity.class);
        startActivity(intent);
    }

    public void onDailyWord(View view) {
        Intent intent = new Intent(MainActivity.this, DailyWordActivity.class);
        startActivity(intent);
    }


}
