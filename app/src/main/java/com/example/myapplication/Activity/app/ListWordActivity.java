package com.example.myapplication.Activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.WordAdapter;
import com.example.myapplication.model.DatabaseAccess;
import com.example.myapplication.model.Word;

import java.util.List;

public class ListWordActivity extends AppCompatActivity {
    ListView lavWord;
    WordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_word);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String topic = bundle.getString("id", "");
            LoadData(topic);
        }
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
}
