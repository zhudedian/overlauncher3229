package com.ider.overlauncher.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ider-eric on 2016/11/23.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String NAME = "launcher_local_config.db";
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists configs(_id Integer primary key autoincrement," +
                "tag varchar(5), status Integer, pkgname text, label text, md5 text, summary text, description text, " +
                "iconurl text, apkurl text, vercode Integer, force boolean, miniimage text, image text)");
        sqLiteDatabase.execSQL("create table if not exists downloads(_id Integer primary key autoincrement," +
                "pkgname text)");
        sqLiteDatabase.execSQL("create table if not exists TitleEntry(_id Integer primary key autoincrement," +
                "pageid varchar(5),name text,action text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
