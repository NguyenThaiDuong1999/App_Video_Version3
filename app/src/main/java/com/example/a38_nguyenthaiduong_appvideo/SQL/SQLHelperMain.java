package com.example.a38_nguyenthaiduong_appvideo.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a38_nguyenthaiduong_appvideo.Object.History;
import com.example.a38_nguyenthaiduong_appvideo.Object.Video;

import java.util.ArrayList;

public class SQLHelperMain extends SQLiteOpenHelper {
    private static final String TAG = "SQLHelperMain";
    static final String DB_NAME = "Video.db";
    static final String DB_NAME_TABLE = "Video";
    static final int DB_VERSION = 5;

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelperMain(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreaTable = "CREATE TABLE Video ( " +
                "avatar Text," +
                "tenphim Text," +
                "url )";

        //Chạy câu lệnh tạo bảng video
        db.execSQL(queryCreaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + DB_NAME_TABLE);
            onCreate(db);
        }
    }

    public void insertVideo(History history) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put("avatar", history.getAvatar());
        contentValues.put("tenphim", history.getTenphim());
        contentValues.put("url", history.getUrl());

        sqLiteDatabase.insert(DB_NAME_TABLE, null, contentValues);
        closeDB();
    }

    public ArrayList<History> getAllItem(){
        History history;
        ArrayList<History> histories = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false,DB_NAME_TABLE,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            String tenphim = cursor.getString(cursor.getColumnIndex("tenphim"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            history = new History(avatar,tenphim,url);
            histories.add(history);
        }
        closeDB();
        return histories;
    }

    public int deleteItem(String tenphim){
        sqLiteDatabase= getWritableDatabase();
        return sqLiteDatabase.delete(DB_NAME_TABLE, "tenphim=?", new String[]{tenphim});
    }

    public boolean delAll() {
        int result;
        sqLiteDatabase = getWritableDatabase();
        result = sqLiteDatabase.delete(DB_NAME_TABLE, null, null);
        closeDB();
        if (result == 1) return true;
        else return false;
    }

    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
