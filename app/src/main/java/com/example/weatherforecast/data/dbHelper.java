package com.example.weatherforecast.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.weatherforecast.data.dbContract.WeatherEntry;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {

    /** Имя Базы данных */
    private static final String DATABASE_NAME = "weather.db";

    /** Версия базы данных. */
    private static final int DATABASE_VERSION = 1;

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_DATA_BASE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + "("
                + WeatherEntry._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + WeatherEntry.COLUMN_TEMP + " INTEGER NOT NULL, "
                + WeatherEntry.COLUMN_PRESSURE + " INTEGER NOT NULL, "
                + WeatherEntry.COLUMN_CLOUDS + " INTEGER NOT NULL "
                + WeatherEntry.COLUMN_WIND + " INTEGER NOT NULL, "
                + WeatherEntry.COLUMN_HUMIDITY + " INTEGER NOT NULL, "
                + WeatherEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_ICON + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_DATA_BASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
