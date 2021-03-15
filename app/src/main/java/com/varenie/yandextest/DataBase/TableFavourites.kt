package com.varenie.yandextest.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.varenie.yandextest.DataBase.DBHelper.Companion.COLUMN_TICKER
import com.varenie.yandextest.DataBase.DBHelper.Companion.TABLE_NAME

// класс для взаимодействия с БД
class TableFavourites(context: Context) {
    val dbHelper = DBHelper(context)
    val db = dbHelper.writableDatabase

    lateinit var cursor: Cursor

    fun getTickers(): String {
        cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TICKER),
                null, null, null, null, null)

        var response = ""

        if (cursor.count == 0)
            return response

        while (cursor.isAfterLast) {
            response = "$response,${cursor.getString(1)}"
        }

        return response
    }

    fun addTicker(ticker: String){
        val values = ContentValues().apply {
            put(COLUMN_TICKER, ticker)
        }

        db.insert(TABLE_NAME, null, values)
    }

    fun deleteTicker(ticker: String){
        db.delete(TABLE_NAME, "$COLUMN_TICKER = ?", arrayOf(ticker))
    }

    fun deleteTickers(){
        db.delete(TABLE_NAME, "", arrayOf())
        cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TICKER),
                null, null, null, null, null)

        Log.e("PROVEROCHKA", cursor.count.toString())
    }
}