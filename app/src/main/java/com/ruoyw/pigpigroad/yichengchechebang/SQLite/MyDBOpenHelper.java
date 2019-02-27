package com.ruoyw.pigpigroad.yichengchechebang.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PIGROAD on 2018/3/12.
 */

public class MyDBOpenHelper extends SQLiteOpenHelper {

    public MyDBOpenHelper(Context context) {
        super(context, "setting.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="CREATE TABLE usersettings(widgetname varchar(10) PRIMARY KEY,states Boolean)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql="DROP TABLE IF EXISTS usersettings";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}
