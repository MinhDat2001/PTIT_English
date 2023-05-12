package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;
import com.example.myapplication.model.DatabaseAccess;
import com.example.myapplication.model.Word;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SearchResultAdapter extends BaseAdapter {

    private final String IN_FAVORITE_LIST = "3000";
    private final Context context;
    private final int layout;
    private final List<Word> list;
//    private final TextToSpeech textToSpeech;
    private final DatabaseAccess databaseAccess;

    public SearchResultAdapter(Context context, int layout, List<Word> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
//        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                textToSpeech.setLanguage(Locale.US);
//            }
//        });
//        textToSpeech.setSpeechRate(0.6F);
//        textToSpeech.setPitch(0.8F);
        databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        convertView = layoutInflater.inflate(layout, null);

        TextView txtVoca = convertView.findViewById(R.id.textVocabu);
        TextView txtType = convertView.findViewById(R.id.textType);
        TextView txtVol = convertView.findViewById(R.id.textVol);
        TextView txtMean = convertView.findViewById(R.id.textMean);
        TextView txtExplane = convertView.findViewById(R.id.textExplan);
        TextView txtExam = convertView.findViewById(R.id.textExam);
        TextView txtExamTrans = convertView.findViewById(R.id.textExamTrans);
        ImageButton btnSpeaker = convertView.findViewById(R.id.imageButtonSpeak);
        final Word word = list.get(position);

        txtVoca.setText(word.getVocabulary());
        txtType.setText(word.getType());
        txtVol.setText(word.getVocalization());
        txtExplane.setText(word.getExplanation());
        txtMean.setText(word.getMeaning());
        txtExam.setText(word.getExample());
        txtExamTrans.setText(word.getExample_translation());

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                playSound(word.getVocabulary());
            }
        });
        final ImageButton btnFavorite = convertView.findViewById(R.id.buttonFavorite);

        if (word.getStatus().equals(IN_FAVORITE_LIST)) {
            btnFavorite.setImageResource(R.drawable.star_gold);
        } else btnFavorite.setImageResource(R.drawable.star_white);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (word.getStatus().equals(IN_FAVORITE_LIST)) {
                    btnFavorite.setImageResource(R.drawable.star_white);
                    word.setStatus("1");
                    databaseAccess.removeFromRememberList(word.getId());
                } else {
                    databaseAccess.addToRememberList(word.getId());
                    word.setStatus(IN_FAVORITE_LIST);
                    btnFavorite.setImageResource(R.drawable.star_gold);
                }
            }
        });

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playSound(String word) {
        String utteranceId = UUID.randomUUID().toString();
//        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
