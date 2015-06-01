package com.chanxa.wnb.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chanxa.wnb.bean.Bank;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.bean.onLineShop.AskOptions;
import com.chanxa.wnb.utils.AssetsDatabaseManager;
import com.wtm.library.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by CHANXA on 2015/2/23.
 */
public class BankDao {
    private static final String dataBaseLocalPath = "bank.db";
    private Context context;
    private AssetsDatabaseManager assetsDatabaseManager;

    public BankDao(Context context) {
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

    public ArrayList<Bank> getBanks() throws UnsupportedEncodingException {
        ArrayList<Bank> bankArrayList = new ArrayList<>();
        SQLiteDatabase database = obtainDataBase();
        if (database == null) {
            return null;
        }
        Cursor cursor = database.rawQuery("select * from myBank ", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Bank bank = new Bank();
                String name = cursor.getString(cursor.getColumnIndex("bankName"));

                bank.setBankName(name);
                bankArrayList.add(bank);
            }
        }
        cursor.close();
        AssetsDatabaseManager.closeAllDatabase();
        return bankArrayList;
    }

    public String quertBank(int checkCode) throws UnsupportedEncodingException {
        String name = null;
        //ArrayList<Bank> bankArrayList = new ArrayList<>();
        SQLiteDatabase database = obtainDataBase();
        if (database == null) {
            return null;
        }
        Cursor cursor = database.rawQuery("select a.bankName from myBank a,myCheckBank b where a.bankId=b.bankId and b.bankNum = " + checkCode, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                //Bank bank = new Bank();
                name = cursor.getString(cursor.getColumnIndex("bankName"));
                //bank.setBankName(name);
                //bankArrayList.add(bank);
            }
        }
        cursor.close();
        AssetsDatabaseManager.closeAllDatabase();
        return name;
    }

}
