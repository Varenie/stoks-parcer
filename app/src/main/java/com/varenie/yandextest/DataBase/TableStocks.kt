package com.varenie.yandextest.DataBase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_CHANGE
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_CHANGE_PERCENTAGE
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_FULL_NAME
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_IS_FAVORITE
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_PRICE
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.COLUMN_TICKER
import com.varenie.yandextest.DataBase.DBHelperStocks.Companion.TABLE_NAME
import com.varenie.yandextest.DataClasses.Stocks

// класс для взаимодействия с БД
class TableStocks(context: Context) {
    private val dbHelper = DBHelperStocks(context)
    private val db = dbHelper.writableDatabase

    private var cursor = db.query(TABLE_NAME, null, null, null, null , null, null)

    //Создание индексов колонок, для более простого поиска нужного столбца при выводе данных
    private val indexTicker = cursor.getColumnIndex(COLUMN_TICKER)
    private val indexName = cursor.getColumnIndex(COLUMN_FULL_NAME)
    private val indexPrice = cursor.getColumnIndex(COLUMN_PRICE)
    private val indexChange = cursor.getColumnIndex(COLUMN_CHANGE)
    private val indexChangePercentage = cursor.getColumnIndex(COLUMN_CHANGE_PERCENTAGE)
    private val indexIsFavourite = cursor.getColumnIndex(COLUMN_IS_FAVORITE)

    fun getFavourites(): ArrayList<Stocks> {
//        cursor = db.query(TABLE_NAME, null,
//                COLUMN_IS_FAVORITE, arrayOf("TRUE"), null, null, null)
        cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IS_FAVORITE = ?", arrayOf("TRUE"))
        cursor.moveToFirst()
        val response = ArrayList<Stocks>()

        if (cursor.count == 0)
            return response

        while (!cursor.isAfterLast) {
            val stock = Stocks(
                    cursor.getString(indexTicker),
                    cursor.getString(indexName),
                    cursor.getFloat(indexPrice),
                    cursor.getFloat(indexChange),
                    cursor.getString(indexChangePercentage)
            )

            response.add(stock)
            cursor.moveToNext()
        }
        return response
    }

    fun getCasheStocks(): ArrayList<Stocks> {
//        cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TICKER, COLUMN_FULL_NAME, COLUMN_PRICE, COLUMN_CHANGE, COLUMN_CHANGE_PERCENTAGE),
//                COLUMN_IS_FAVORITE, arrayOf("FALSE"), null, null, null)
        cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IS_FAVORITE = ?", arrayOf("FALSE"))

        cursor.moveToFirst()
        val response = ArrayList<Stocks>()

        if (cursor.count == 0)
            return response

        while (!cursor.isAfterLast) {
            val stock = Stocks(
                    cursor.getString(indexTicker),
                    cursor.getString(indexName),
                    cursor.getFloat(indexPrice),
                    cursor.getFloat(indexChange),
                    cursor.getString(indexChangePercentage)
            )

            response.add(stock)
            cursor.moveToNext()
        }
        return response
    }

    fun getTickers(): String {
        cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TICKER),
                null, null, null, null, null)
        cursor.moveToFirst()

        var response = ""

        if (cursor.count == 0)
            return response

        while (!cursor.isAfterLast) {
            response = "${cursor.getString(0)},$response"
            cursor.moveToNext()
        }
        return response
    }

    fun addFavourite(stock: Stocks){
        val values = ContentValues().apply {
            put(COLUMN_TICKER, stock.symbol)
            put(COLUMN_FULL_NAME, stock.name)
            put(COLUMN_PRICE, stock.price)
            put(COLUMN_CHANGE, stock.change)
            put(COLUMN_CHANGE_PERCENTAGE, stock.percent)
            put(COLUMN_IS_FAVORITE, "TRUE")
        }

        db.insert(TABLE_NAME, null, values)
    }

    fun addCashe(stocks: ArrayList<Stocks>) {
        for (x in stocks) {
            val values = ContentValues().apply {
                put(COLUMN_TICKER, x.symbol)
                put(COLUMN_FULL_NAME, x.name)
                put(COLUMN_PRICE, x.price)
                put(COLUMN_CHANGE, x.change)
                put(COLUMN_CHANGE_PERCENTAGE, x.percent)
                put(COLUMN_IS_FAVORITE, "FALSE")
            }

            db.insert(TABLE_NAME, null, values)
        }

    }

    fun updateFavourite(stock: Stocks) {
        val values = ContentValues().apply {
            put(COLUMN_TICKER, stock.symbol)
            put(COLUMN_FULL_NAME, stock.name)
            put(COLUMN_PRICE, stock.price)
            put(COLUMN_CHANGE, stock.change)
            put(COLUMN_CHANGE_PERCENTAGE, stock.percent)
            put(COLUMN_IS_FAVORITE, "TRUE")
        }

        db.update(TABLE_NAME, values, "$COLUMN_TICKER = ? AND $COLUMN_IS_FAVORITE = ?", arrayOf(stock.symbol, "TRUE"))
    }

    fun showTable(){
        cursor = db.query(TABLE_NAME, arrayOf(COLUMN_TICKER),
            null, null, null, null, null)
        cursor.moveToFirst()

        Log.e("PROVEROCHKA", "syka ${cursor.count}")
        while (cursor.moveToNext()) {
            Log.e("PROVEROCHKA", "blya ${cursor.getString(1)}")
        }

    }

    fun deleteFavourite(ticker: String){
        db.delete(TABLE_NAME, "$COLUMN_TICKER = ? AND $COLUMN_IS_FAVORITE = ?", arrayOf(ticker, "TRUE"))
    }

    fun cleanDB(){
        db.delete(TABLE_NAME, "", arrayOf())
    }

    fun isFavourite(ticker: String): Boolean {
        cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_TICKER = ?" +
                "AND $COLUMN_IS_FAVORITE = ?", arrayOf(ticker, "TRUE"))

        return cursor.count != 0
    }

    fun cleanCashe() {
        db.delete(TABLE_NAME, "$COLUMN_IS_FAVORITE = ?", arrayOf("FALSE"))
    }
}