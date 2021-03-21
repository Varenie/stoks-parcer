package com.varenie.yandextest.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperStocks(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "Favourites.db"
        const val DB_VERSION = 3
        const val TABLE_NAME = "Favourites"
        const val COLUMN_TICKER = "Ticker"
        const val COLUMN_FULL_NAME = "CompanyName"
        const val COLUMN_PRICE = "Price"
        const val COLUMN_CHANGE = "Change"
        const val COLUMN_CHANGE_PERCENTAGE = "ChangePercentage"
        const val COLUMN_IS_FAVORITE = "isFavorite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TICKER TEXT," +
                "$COLUMN_FULL_NAME TEXT," +
                "$COLUMN_PRICE REAL," +
                "$COLUMN_CHANGE REAL," +
                "$COLUMN_CHANGE_PERCENTAGE TEXT," +
                "$COLUMN_IS_FAVORITE TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}