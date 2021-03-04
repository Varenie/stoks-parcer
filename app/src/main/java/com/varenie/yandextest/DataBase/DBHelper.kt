package com.varenie.yandextest.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "Favourites.db"
        val DB_VERSION = 1
        val TABLE_NAME = "Favourites"
        val COLUMN_TICKER = "Ticker"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TICKER TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}