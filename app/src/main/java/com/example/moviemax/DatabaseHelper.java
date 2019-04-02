package com.example.moviemax;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "MovieMaxDatabase.db";

    private static final String TABLE_ShowInList = "ShowInList";
    private static final String TABLE_ShowList = "ShowList";
    private static final String TABLE_User = "User";

    private static final String KEY_ID = "ID";

    private static final String ShowInList_COL1 = "showID";
    private static final String ShowInList_COL2 = "showListID";

    private static final String ShowList_COL1 = "showListID";
    private static final String ShowList_COL2 = "name";

    private static final String User_COL1 = "userId";
    private static final String Usert_COL2 = "firstName";

    private static final String CREATE_TABLE_ShowInList = "CREATE TABLE "+ TABLE_ShowInList + "(" + KEY_ID +"ID INTEGER PRIMARY KEY AUTOINCREMENT,"+ ShowInList_COL1 + "INTEGER,"+ ShowInList_COL2 +" INTEGER)";
    private static final String CREATE_TABLE_ShowList = "CREATE TABLE "+ TABLE_ShowList + "(" + ShowList_COL1 + "ID INTEGER PRIMARY KEY," + ShowList_COL2 +" TEXT)";
    private static final String CREATE_TABLE_User = "create table User (id integer primary key, firstName text, lastName text, age int, email text, password text)";


    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ShowInList);
        db.execSQL(CREATE_TABLE_ShowList);
        db.execSQL(CREATE_TABLE_User);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ShowList);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ShowInList);
        db.execSQL("DROP TABLE IF EXISTS User");
        //Create new tables
        onCreate(db);
    }

    public long createShowList(String ShowListName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ShowList_COL2, ShowListName);

        long ShowList_id = db.insert(TABLE_ShowList, null, contentValues);

        Log.d(TAG, "createShowList: Adding" + ShowListName + " to "+ TABLE_ShowList);

        return ShowList_id;
    }

    public long addShow(int ShowList_id, Show film){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ShowInList_COL1, film.getId());
        contentValues.put(ShowInList_COL2, ShowList_id);

        long ShowInList_id = db.insert(TABLE_ShowInList,null, contentValues);

        Log.d(TAG, "addShow: Adding" + film + " to "+TABLE_ShowInList);

        return ShowInList_id;
    }


}
