package com.example.myapplication.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Locale;
import java.util.UUID;

public class QuestionTypeAFragment extends Fragment {
    TextView txtQuestion;
    RadioGroup rdgAnswer;
    RadioButton rdChoiceA;
    RadioButton rdChoiceB;
    RadioButton rdChoiceC;
    RadioButton rdChoiceD;
    ImageButton btnSpeaker;
    TextView txtTitle;
    String curentAnswer;
    String soundName = "";

    TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question1, container, false);
        txtQuestion = view.findViewById(R.id.txtQuestion);
        rdgAnswer = view.findViewById(R.id.groupRadioAnswer);
        rdChoiceA = view.findViewById(R.id.radioButtonA);
        rdChoiceB = view.findViewById(R.id.radioButtonB);
        rdChoiceC = view.findViewById(R.id.radioButtonC);
        rdChoiceD = view.findViewById(R.id.radioButtonD);
        txtTitle = view.findViewById(R.id.textView2);
        btnSpeaker = view.findViewById(R.id.imageButtonSpeaker);

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
        textToSpeech.setSpeechRate(0.6F);
        textToSpeech.setPitch(0.8F);

        rdgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonA:
                        curentAnswer = rdChoiceA.getText().toString();
                        break;
                    case R.id.radioButtonB:
                        curentAnswer = rdChoiceB.getText().toString();
                        break;
                    case R.id.radioButtonC:
                        curentAnswer = rdChoiceC.getText().toString();
                        break;
                    case R.id.radioButtonD:
                        curentAnswer = rdChoiceD.getText().toString();
                        break;
                }
            }
        });

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                playSound(soundName);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setQuestion(String answer, String a, String b, String c, String d, int type) {
        rdChoiceA.setText(a);
        rdChoiceA.setChecked(false);
        rdChoiceB.setText(b);
        rdChoiceB.setChecked(false);
        rdChoiceC.setText(c);
        rdChoiceC.setChecked(false);
        rdChoiceD.setText(d);
        rdChoiceD.setChecked(false);
        if (type == 1) {
            txtQuestion.setText(answer);
            txtTitle.setText("Chọn bản dịch đúng");
            btnSpeaker.setVisibility(View.INVISIBLE);
        } else {
            soundName = answer;
            btnSpeaker.setVisibility(View.VISIBLE);
            if (type == 3) {
                txtQuestion.setText("");
                playSound(answer);
                txtTitle.setText("Chọn từ nghe được");
            } else {
                txtQuestion.setText(answer);
                txtTitle.setText("Chọn bản dịch đúng");
            }
        }
    }

    public String getAnswer() {
        return curentAnswer;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playSound(String word) {
        String utteranceId = UUID.randomUUID().toString();
        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
