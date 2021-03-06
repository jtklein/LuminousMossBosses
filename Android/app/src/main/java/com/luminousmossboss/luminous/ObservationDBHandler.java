/**
 * Created by andrey on 2/11/15.
 */
package com.luminousmossboss.luminous;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ObservationDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "Silenedb";

    // Contacts table name
    private static final String TABLE_OBSERVATIONS = "observations";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_PHOTO_PATH = "photo_path";
    public static final String KEY_LATITUDE = "latidude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_IS_SILENE = "is_silene";
    public static final String KEY_TIME_TAKEN = "time_taken";
    public static final String KEY_GPS_ACCURACY = "gps_accuracy";
    public static final String KEY_SYNCED_STATUS = "has_been_synced";
    public static final String KEY_PROCESSED_STATUS = "has_been_processed";
    public ObservationDBHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OBSERVATION_TABLE = "CREATE TABLE " + TABLE_OBSERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"  + KEY_PHOTO_PATH + " TEXT,"
                + KEY_LATITUDE + " TEXT,"  + KEY_LONGITUDE + " TEXT,"+ KEY_IS_SILENE + " INTEGER," + KEY_TIME_TAKEN + " TEXT," + KEY_GPS_ACCURACY + " REAL," +
                KEY_SYNCED_STATUS + " INTEGER," + KEY_PROCESSED_STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_OBSERVATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);

        // Create tables again
        onCreate(db);

    }

    void addObservation(HashMap<String,String> map) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            values.put(key,map.get(key));
            it.remove(); // avoids a ConcurrentModificationException
        }


        // Inserting Row
        db.insert(TABLE_OBSERVATIONS, null, values);
        db.close(); // Closing database connection
    }


    public int getObservationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_OBSERVATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public Cursor getAllObservation() {
        String selectQuery = "SELECT  * FROM " + TABLE_OBSERVATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getObservationBySyncStatus(int has_been_synced) {
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS +
                " WHERE has_been_synced =" + has_been_synced;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getObservationByProcessedStatus(int has_been_processed) {
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS +
                " WHERE has_been_processed =" + has_been_processed;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getObservationById(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS +
                " WHERE id =" + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getObservationByFilePath(String filePath){
       String selectQuery ="SELECT * FROM " + TABLE_OBSERVATIONS +
               " WHERE " + KEY_PHOTO_PATH + "=" + "'" + filePath + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int updateIsSilene(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SILENE, String.valueOf(1));
        return db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    public int updateProcessed(int id, boolean status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROCESSED_STATUS, String.valueOf(1));
        return db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }


    public int updateSyncedStatus(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SYNCED_STATUS, String.valueOf(1));
        return db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int deleteObservation(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OBSERVATIONS, KEY_ID + "=" + id , null);
    }

}
