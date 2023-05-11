package com.example.myapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseAccess {
    private static DatabaseAccess instance;
    private final SQLiteAssetHelper openHelper;
    private SQLiteDatabase db;
    private Cursor c = null;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (this.db != null) this.db.close();
    }

    public List<Topic> GetAllTopics() {
        c = db.rawQuery("SELECT * from Topics", new String[]{});
        List<Topic> list = new ArrayList<>();
        while (c.moveToNext()) {
            String id = String.valueOf(c.getInt(0));
            String name = c.getString(1);
            int date = c.getInt(2);
            list.add(new Topic(id, name, date));
        }
        return list;
    }

    public List<Word> GetTopic(String topic) {
        c = db.rawQuery("SELECT * from Words WHERE Topic =" + topic, new String[]{});
        List<Word> list = new ArrayList<>();
        while (c.moveToNext()) {
            Word word = new Word();
            String id = String.valueOf(c.getInt(0));
            String vocabulary = c.getString(1);
            String meaning = c.getString(4);
            String status = c.getString(9);
            word.setId(id);
            word.setVocabulary(vocabulary);
            word.setMeaning(meaning);
            word.setStatus(status);
            list.add(word);
        }
        return list;
    }

    public void SaveDate(String id) {
        ContentValues contentValues = new ContentValues();
        Date date = new Date();
        contentValues.put("Date", date.getTime());
        db.update("Topics", contentValues, "ID" + "=" + id, null);
        Log.e(id, String.valueOf(date.getTime()));
    }


    public String getLastLearnedTopic() {
        c = db.rawQuery("SELECT * from Topics ORDER BY Date DESC", new String[]{});
        if (c.moveToNext()) {
            String id = String.valueOf(c.getInt(0));
            return id;
        }
        return null;
    }

    public List<Word> getWord(String input) {
        c = db.rawQuery("SELECT * FROM Words WHERE instr(Vocabulary,'" + input + "') > 0", new String[]{});
        List<Word> list = new ArrayList<>();
        while (c.moveToNext()) {
            Word word = new Word();
            String id = String.valueOf(c.getInt(0));
            String vocabulary = c.getString(1);
            String type = c.getString(2);
            String voc = c.getString(3);
            String meaning = c.getString(4);
            String explain = c.getString(5);
            String exam = c.getString(6);
            String exam_tran = c.getString(7);
            String topic = c.getString(8);
            String status = String.valueOf(c.getInt(9));

            word.setId(id);
            word.setVocabulary(vocabulary);
            word.setMeaning(meaning);
            word.setType(type);
            word.setVocalization(voc);
            word.setExplanation(explain);
            word.setExample(exam);
            word.setExample_translation(exam_tran);
            word.setTopic(topic);
            word.setStatus(status);

            list.add(word);
        }
        return list;
    }

    public void addToRememberList(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Status", 3000);
        db.update("Words", contentValues, "ID" + "=" + id, null);
    }

    public void removeFromRememberList(String id) {
        ContentValues contentValues = new ContentValues();
        int randomStt = (int) (Math.random() * 2999 + 1);
        contentValues.put("Status", randomStt);
        db.update("Words", contentValues, "ID" + "=" + id, null);
    }

    public List<Word> getDailyWord() {
        c = db.rawQuery("SELECT * from Words ORDER BY Status DESC LIMIT 20", new String[]{});
        List<Word> list = new ArrayList<>();
        while (c.moveToNext()) {
            Word word = new Word();
            String id = String.valueOf(c.getInt(0));
            String vocabulary = c.getString(1);
            String meaning = c.getString(4);
            String status = c.getString(9);
            word.setId(id);
            word.setVocabulary(vocabulary);
            word.setMeaning(meaning);
            word.setStatus(status);
            list.add(word);
        }
        return list;
    }
}
