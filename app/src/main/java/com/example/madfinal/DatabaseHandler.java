package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.content.Context;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int Database_version =21;  //THE DATABASE DOES NOT UPDATES AUTOMATICALLY, ONLY THROUGH CHANGING THE VERSION OF DATABASE

    private static final String Database_name = "coffee.db";

    private static final String Table1= "products";
    private static final String Table2= "tables";

    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Database_name, null, Database_version);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String products = "CREATE TABLE "+Table1+"(product_id INTEGER PRIMARY KEY, product_title TEXT NOT NULL, product_price REAL)";
       String tables = "CREATE TABLE "+Table2+"(table_id INTEGER PRIMARY KEY, table_status INTEGER)";
        db.execSQL(products);
        db.execSQL(tables);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table1);
        db.execSQL("DROP TABLE IF EXISTS "+Table2);
        onCreate(db);
    }


   public void insert(String id, String title, String price, SQLiteDatabase database) {
        ContentValues values1 = new ContentValues();
        values1.put("product_id", id);
        values1.put("product_title", title);
        values1.put("product_price", price);
        this.database.insert(Table1, null, values1);
        }

    public void insert(String table_id, String status, SQLiteDatabase database) {
        ContentValues values2 = new ContentValues();
        values2.put("table_id", table_id);
        values2.put("table_status", status);
        this.database.insert(Table2, null, values2);

    }

    public  Cursor getAllData(){
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(" SELECT * FROM " + Table2, null);
        return cursor;


    }
    public  Cursor getAllData1() {
        database = this.getReadableDatabase();
        Cursor cursor1 = database.rawQuery(" SELECT * FROM " + Table1, null);
        return cursor1;

    }
}

