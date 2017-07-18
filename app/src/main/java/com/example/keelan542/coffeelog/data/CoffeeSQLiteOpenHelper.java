package com.example.keelan542.coffeelog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

/**
 * Created by keelan542 on 18/07/2017.
 */

public class CoffeeSQLiteOpenHelper extends SQLiteOpenHelper {

    // Name and version of the database
    public static final String DATABASE_NAME = "coffee.db";
    public static final int DATABASE_VERSION = 1;

    public CoffeeSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create string to hold SQL statement to create coffee_log table
        String CREATE_COFFEE_LOG_TABLE = "Create Table " + CoffeeEntry.TABLE_NAME + " ("
                + CoffeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CoffeeEntry.COLUMN_LOG_METHOD + " TEXT NOT NULL, "
                + CoffeeEntry.COLUMN_LOG_COFFEE_AMOUNT + " TEXT NOT NULL, "
                + CoffeeEntry.COLUMN_LOG_WATER_AMOUNT + " TEXT NOT NULL, "
                + CoffeeEntry.COLUMN_LOG_TIME + " TEXT NOT NULL, "
                + CoffeeEntry.COLUMN_LOG_EXTRACTION + " INTEGER NOT NULL, "
                + CoffeeEntry.COLUMN_LOG_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_COFFEE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
