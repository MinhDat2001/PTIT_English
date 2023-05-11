package com.example.myapplication.Activity.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Fragment.QuestionTypeAFragment;
import com.example.myapplication.R;
import com.example.myapplication.model.DatabaseAccess;
import com.example.myapplication.model.Word;

import java.util.List;

public class LessonActivity extends AppCompatActivity {

    protected int index = 0;
    protected List<Word> list;
    protected String currentAnswer = "";
    protected ConstraintLayout ctlPanel;
    protected TextView tatResult;
    protected TextView txtCorrectAnswer;
    protected Button btnCheck;
    protected ProgressBar prgTask;
    private String topic;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        CreateQuestion();
    }

    protected void getView() {
        setContentView(R.layout.activity_lesson);
        btnCheck = findViewById(R.id.buttonCheck);
        tatResult = findViewById(R.id.txtRsQuestion);
        txtCorrectAnswer = findViewById(R.id.txtCorrectAnswer);
        ctlPanel = findViewById(R.id.ctrPanel);
        prgTask = findViewById(R.id.task_progress_bar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            topic = bundle.getString("id", "");
            LoadData();
        }
    }

    protected void LoadData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        list = databaseAccess.GetTopic(topic);
        databaseAccess.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void CreateQuestion() {
        String question = "";
        int random1 = (int) (Math.random() * 4 + 1);
        int random2 = 0, random3 = 0, random4 = 0;
        while (random2 == index) {
            random2 = (int) (Math.random() * 20);
        }
        while (random3 == index || random3 == random2) {
            random3 = (int) (Math.random() * 20);
        }
        while (random4 == index || random4 == random2 | random4 == random3) {
            random4 = (int) (Math.random() * 20);
        }
        String a = "";
        String b = "";
        String c = "";
        String d = "";

        int randomType = (int) (Math.random() * 3 + 1);
        if (randomType != 1) {
            question = list.get(index).getVocabulary();
            currentAnswer = list.get(index).getMeaning();
            switch (random1) {
                case 1:
                    a = list.get(index).getMeaning();
                    b = list.get(random2).getMeaning();
                    c = list.get(random3).getMeaning();
                    d = list.get(random4).getMeaning();
                    break;
                case 2:
                    b = list.get(index).getMeaning();
                    a = list.get(random2).getMeaning();
                    c = list.get(random3).getMeaning();
                    d = list.get(random4).getMeaning();
                    break;
                case 3:
                    c = list.get(index).getMeaning();
                    b = list.get(random2).getMeaning();
                    a = list.get(random3).getMeaning();
                    d = list.get(random4).getMeaning();
                    break;
                case 4:
                    d = list.get(index).getMeaning();
                    b = list.get(random2).getMeaning();
                    c = list.get(random3).getMeaning();
                    a = list.get(random4).getMeaning();
                    break;
            }
        } else {
            question = list.get(index).getMeaning();
            currentAnswer = list.get(index).getVocabulary();
            switch (random1) {
                case 1:
                    a = list.get(index).getVocabulary();
                    b = list.get(random2).getVocabulary();
                    c = list.get(random3).getVocabulary();
                    d = list.get(random4).getVocabulary();
                    break;
                case 2:
                    b = list.get(index).getVocabulary();
                    a = list.get(random2).getVocabulary();
                    c = list.get(random3).getVocabulary();
                    d = list.get(random4).getVocabulary();
                    break;
                case 3:
                    c = list.get(index).getVocabulary();
                    b = list.get(random2).getVocabulary();
                    a = list.get(random3).getVocabulary();
                    d = list.get(random4).getVocabulary();
                    break;
                case 4:
                    d = list.get(index).getVocabulary();
                    b = list.get(random2).getVocabulary();
                    c = list.get(random3).getVocabulary();
                    a = list.get(random4).getVocabulary();
                    break;
            }
        }

        QuestionTypeAFragment fragment = (QuestionTypeAFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        assert fragment != null;
        fragment.setQuestion(question, a, b, c, d, randomType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    public void onCheck(View view) {
        if (btnCheck.getText().toString().equals("Kiểm tra")) {
            btnCheck.setText("TIẾP TỤC");
            QuestionTypeAFragment fragment = (QuestionTypeAFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
            String answer = fragment.getAnswer();
            if (answer.equals(currentAnswer)) {
                tatResult.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.correct));
                ctlPanel.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_correct));
                tatResult.setText("Chính xác");
                index++;
            } else {
                ctlPanel.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_incorrect));
                tatResult.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.incorrect));
                btnCheck.setBackgroundResource(R.drawable.incorrect_button);
                tatResult.setText("Không chính xác");
                txtCorrectAnswer.setText("Đáp án đúng là: " + currentAnswer);
                Word tmp = list.get(index);
                list.remove(index);
                list.add(tmp);
            }
        } else {
            btnCheck.setText("Kiểm tra");
            btnCheck.setBackgroundResource(R.drawable.correct_button);
            tatResult.setText(null);
            txtCorrectAnswer.setText(null);
            ctlPanel.setBackgroundColor(Color.argb(0, 0, 0, 0));
            if (index == 20) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                databaseAccess.SaveDate(topic);
                databaseAccess.close();
                Intent intent = new Intent(LessonActivity.this, TopicActivity.class);
                startActivity(intent);
            } else {
                prgTask.setProgress(index);
                CreateQuestion();
            }
        }
    }
}
