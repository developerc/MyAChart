package ru.saperov.myachart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saperov on 20.03.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "stock_table";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_CNT = "cnt";
    public static final String COLUMN_PRICE = "price";
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + COLUMN_STOCK  + " text not null, "
            + COLUMN_CNT + " text, "
            + COLUMN_PRICE + " text "
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }

    public void addStock(StockWrapper stockWrapper){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STOCK, stockWrapper.getStock());
        values.put(COLUMN_CNT, stockWrapper.getCnt());
        values.put(COLUMN_PRICE, stockWrapper.getPrice());
        // Вставляем строку в таблицу
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public List<StockWrapper> getAllStock(){
        ArrayList<StockWrapper> stockList = new ArrayList<StockWrapper>();
        //выбираем всю таблицу
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Проходим по всем строкам и добавляем в список
        if (cursor.moveToFirst()) {
            do {
                StockWrapper stockWrapper = new StockWrapper();
                stockWrapper.setID(Integer.parseInt(cursor.getString(0)));
                stockWrapper.setStock(cursor.getString(1));
                stockWrapper.setCnt(cursor.getString(2));
                stockWrapper.setPrice(cursor.getString(3));
                stockList.add(stockWrapper);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return stockList;
    }

    //удалить все акции
    public void delAllStock(){
        SQLiteDatabase db = this.getWritableDatabase();
        String delQuery = "DELETE FROM " + DATABASE_TABLE +";";
        db.execSQL(delQuery);
        db.close();
    }

    //удалить акции одной фирмы
    public void delSymbolStock(StockWrapper stockWrapper){
        SQLiteDatabase db = this.getWritableDatabase();
       // String delQuery = "DELETE FROM " + DATABASE_TABLE + "WHERE "+";";
       // db.execSQL(delQuery);
        db.delete(DATABASE_TABLE, COLUMN_STOCK + " = ? ", new String[]{String.valueOf(stockWrapper.getStock())});
        db.close();
    }

    public float getIdStock(StockWrapper stockWrapper){
        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor cursor = db.query(DATABASE_TABLE, );
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{COLUMN_ID, COLUMN_STOCK,COLUMN_CNT,COLUMN_PRICE}, COLUMN_STOCK + " = ? ", new String[]{String.valueOf(stockWrapper.getStock())}, null,null,null);
        cursor.moveToFirst();
        int cnt = Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_CNT)));
       // float price = Float.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
        float sum = cnt * MainActivity.price.floatValue();
        return sum;
    }

    //удалить таблицу и создать
    public void upgradeStock() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delQuery = "DROP TABLE  " + DATABASE_TABLE+";";
        // Удаляем старую таблицу и создаём новую
        db.execSQL(delQuery);
        // Создаём новую таблицу
        onCreate(db);
    }

    //получить число акций
    public int getStockCount(){
        String countQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }
}
