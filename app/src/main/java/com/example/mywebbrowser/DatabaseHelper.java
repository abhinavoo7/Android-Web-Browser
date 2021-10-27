package com.example.mywebbrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;

    public static final String Database_name = "Bookmarks.db";
    public static final String Table_name = "Bookmarks";
    public static final String col_id = "Id";
    public static final String col_title = "Title";
    public static final String col_url = "Url";

    public static final String Table_Download = "Downloads";
    public static final String down_id = "Id_download";
    public static final String down_title = "Title";
    public static final String down_time = "Time";
    public static final String down_path = "Path";

    public static final String Table_History = "History";
    public static final String hist_id = "Id_hist";
    public static final String hist_title = "Title";
    public static final String hist_url = "Url";
    public static final String hist_time = "Time";

    public DatabaseHelper(Context context){
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_name + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Url Text)");
        db.execSQL("create table " + Table_Download + "(Id_download INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Time TEXT, Path TEXT)");
        db.execSQL("create table " + Table_History + "(Id_hist INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Url Text, Time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_Download);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_History);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String Tilte, String Url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_title, Tilte);
        contentValues.put(col_url, Url);
        Long result = db.insert(Table_name, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public boolean insertHist(String Tilte, String Url, String Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(hist_title, Tilte);
        contentValues.put(hist_url, Url);
        contentValues.put(hist_time, Time);
        Long result = db.insert(Table_History, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public ArrayList<HashMap<String, String>> showData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userlist = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + Table_name, null);
        while (cursor.moveToNext()){
            HashMap<String, String> user = new HashMap<>();
            user.put("Id", cursor.getString(cursor.getColumnIndex(col_id)));
            user.put("Title", cursor.getString(cursor.getColumnIndex(col_title)));
            user.put("Url", cursor.getString(cursor.getColumnIndex(col_url)));
            userlist.add(user);
        }
        return userlist;
    }

    public ArrayList<HashMap<String, String>> showHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userlist = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + Table_History, null);
        while (cursor.moveToNext()){
            HashMap<String, String> user = new HashMap<>();
            user.put("Id_hist", cursor.getString(cursor.getColumnIndex(hist_id)));
            user.put("Title", cursor.getString(cursor.getColumnIndex(hist_title)));
            user.put("Url", cursor.getString(cursor.getColumnIndex(hist_url)));
            user.put("Time", cursor.getString(cursor.getColumnIndex(hist_time)));
            userlist.add(user);
        }
        return userlist;
    }

    public boolean insertDownload(String Title, String Time, String Path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(down_title, Title);
        contentValues.put(down_time, Time);
        contentValues.put(down_path, Path);
        Long result = db.insert(Table_Download, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public ArrayList<HashMap<String, String>> showDownloads(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> downlist = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + Table_Download, null);
        while (cursor.moveToNext()){
            HashMap<String, String> user = new HashMap<>();
            user.put("Id_download", cursor.getString(cursor.getColumnIndex(down_id)));
            user.put("Title", cursor.getString(cursor.getColumnIndex(down_title)));
            user.put("Time", cursor.getString(cursor.getColumnIndex(down_time)));
            user.put("Path", cursor.getString(cursor.getColumnIndex(down_path)));
            downlist.add(user);

        }
        return downlist;
    }

    public boolean update(String id, String Title, String Url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_id, id);
        contentValues.put(col_title, Title);
        contentValues.put(col_url, Url);
        db.update(Table_name, null, "Id = ?", new String[]{id});
        return true;
    }

    public Integer delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_name, "Id = ?", new String[]{id});
    }

    public Integer deleteHistory(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_History, "Id_hist = ?", new String[]{id});
    }

    public void alter(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = ' " + Table_name + "'");
    }

    public void alterHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = ' " + Table_History + "'");
    }
}
