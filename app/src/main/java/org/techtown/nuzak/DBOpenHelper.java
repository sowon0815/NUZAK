package org.techtown.nuzak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.createDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.createDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.mCtx = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public void insertColumn(String title, String text , String image, String keyword, int level){
        String sql = "insert into " + DataBases.createDB._TABLENAME0 + "(title, text, image, keyword, level) values ('" + title + "', '" + text + "', '" + image + "', '" + keyword + "', " + level + ");";

        mDB.execSQL(sql);
    }

    public Story getStoryData(int id){
        String sql = "select * from " + DataBases.createDB._TABLENAME0 + "  where _id = " + id;

        Story storyData = new Story();

        Cursor mCur = mDB.rawQuery(sql, null);
        mCur.moveToFirst();
        storyData.setId(mCur.getInt(0));
        storyData.setTitle(mCur.getString(1));
        storyData.setText(mCur.getString(2));
        storyData.setImage(mCur.getString(3));
        storyData.setKeyword(mCur.getString(4));
        storyData.setLevel(mCur.getInt(5));

        return storyData;
    }

}
