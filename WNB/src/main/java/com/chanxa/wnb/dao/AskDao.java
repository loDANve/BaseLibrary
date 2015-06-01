package com.chanxa.wnb.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.bean.onLineShop.AskOptions;
import com.chanxa.wnb.utils.AssetsDatabaseManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/1/21.
 */
public class AskDao {
    private static final String dataBaseLocalPath = "question.db";
    private Context context;
    private AssetsDatabaseManager assetsDatabaseManager;

    public AskDao(Context context) {
        this.context = context;
        AssetsDatabaseManager.initManager(context);
        assetsDatabaseManager = AssetsDatabaseManager.getManager();
    }
    private SQLiteDatabase obtainDataBase() {
        if (context == null) {
            return null;
        }
        // 通过管理对象获取数据库
        return assetsDatabaseManager.getDatabase(dataBaseLocalPath);
    }

    public ArrayList<Ask> queryAsk(int resultNumber) throws UnsupportedEncodingException {
        ArrayList<Ask> askArrayList = new ArrayList<>();
        SQLiteDatabase database = obtainDataBase();
        if (database == null) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from question where 1=1 order by  RANDOM() limit " + resultNumber, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Ask ask = new Ask();
                String name = cursor.getString(cursor.getColumnIndex("question_name"));
                String answer = cursor.getString(cursor.getColumnIndex("answer"));
                String id = cursor.getString(cursor.getColumnIndex("question_id"));
                ask.setName(name);
                ask.setAnswer(answer);
                ask.setId(id);
                askArrayList.add(ask);
            }
        }
        cursor.close();
        quertAskOption(askArrayList);
        //database.close();
        AssetsDatabaseManager.closeAllDatabase();
        return askArrayList;
    }

    public void quertAskOption(ArrayList<Ask> askArrayList) throws UnsupportedEncodingException {
        if (equals(askArrayList == null || askArrayList.size() < 1)) {
            return;
        }
        SQLiteDatabase database = obtainDataBase();
        if (database == null) {
            return;
        }
        for (Ask ask : askArrayList) {
            Cursor cursor = database.rawQuery("select * from options where question_id=" + ask.getId() + " order by option_name Asc", null);
            ArrayList<AskOptions> askOptionses = null;
            if (cursor != null) {
                askOptionses = new ArrayList<>();
                while (cursor.moveToNext()) {
                    AskOptions askOptions = new AskOptions();
                    String name = cursor.getString(cursor.getColumnIndex("option_name"));
                    String content = cursor.getString(cursor.getColumnIndex("option_content"));
                    askOptions.setName(name);
                    askOptions.setContent(content);
                    askOptions.setQuestion_id(ask.getId());
                    askOptionses.add(askOptions);
                }
            }
            cursor.close();
            AssetsDatabaseManager.closeAllDatabase();
            ask.setAskOptionsArrayList(askOptionses);
        }
    }

}
