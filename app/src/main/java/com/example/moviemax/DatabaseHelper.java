package com.example.moviemax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.example.moviemax.MainActivity.loginEmail;
import static com.example.moviemax.MainActivity.loginId;
import static com.example.moviemax.MainActivity.loginPassword;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "MovieMaxDatabase.db";

    private static final String TABLE_ShowInList = "ShowInList";
    private static final String TABLE_ShowList = "ShowList";
    private static final String TABLE_User = "User";

    private static final String CREATE_TABLE_ShowInList = "CREATE TABLE ShowInList(ID INTEGER primary key autoincrement, showId INTEGER , showListId INTEGER)";
    private static final String CREATE_TABLE_ShowList = "CREATE TABLE ShowList(showListId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, userId TEXT)";
    private static final String CREATE_TABLE_User = "CREATE TABLE User (ID INTEGER primary key autoincrement, firstName TEXT, lastName TEXT, age INTEGER, email TEXT, password TEXT)";


    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_User);
        db.execSQL(CREATE_TABLE_ShowInList);
        db.execSQL(CREATE_TABLE_ShowList);

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

    //Registeren van user
    public boolean insertUser(String name, String lastName, int age, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", name);
        contentValues.put("lastName", lastName);
        contentValues.put("age", age);
        contentValues.put("email", email);
        contentValues.put("password", password);
        db.insert("User", null, contentValues);
        return true;
    }

    //Checken of user gegevens kloppen
    public boolean checkIfLoggedIn(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select ID from User where email = '" + email + "' and password = '" + password + "'", null );
        if (res.getCount() > 0) {

            Log.w("CHECK HIER", "" + res.getColumnCount());
            return true;
        } else {
            return false;
        }
    }

    public String getUserId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select ID from User where email = '" + loginEmail + "' and password = '" + loginPassword + "'", null );
        res.moveToFirst();
        return res.getString(res.getColumnIndex("ID"));
    }

    public boolean createShowList(String userId, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("userId", userId);
        db.insert("ShowList", null, contentValues);
        return true;
    }

    public ArrayList<ShowList> getShowListUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ShowList> tempAL = new ArrayList<>();
        ShowList showList;

        Cursor res =  db.rawQuery("select * from ShowList where userId = '" + loginId + "'", null );
        String showId = "";

        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                showId = res.getString(res.getColumnIndex("showListId"));
                showList = new ShowList(res.getString(res.getColumnIndex("name")));
                Cursor showinList =  db.rawQuery("select * from ShowInList where showListId = '" + showId + "'", null );
                if (showinList.moveToFirst()) {
                    while (!showinList.isAfterLast()) {

                        showList.setShowNumber(showinList.getInt(showinList.getColumnIndex("showId")));
                        showinList.moveToNext();
                    }
                }

                showinList.close();
                tempAL.add(showList);
                res.moveToNext();
            }
        }

        return tempAL;
    }

    public boolean checkIfUserHasList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from ShowList where userId = '" + loginId + "'", null );
        if (res.getCount() > 0) {
            return false;
        }
        return true;
    }


}
