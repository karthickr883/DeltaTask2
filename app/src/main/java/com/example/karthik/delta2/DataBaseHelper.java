package com.example.karthik.delta2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import static com.example.karthik.delta2.R.id.date;
import static com.example.karthik.delta2.R.id.time;
import static java.sql.Types.BLOB;
import static java.sql.Types.NULL;

/**
 * Created by karthik on 03-06-2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    static final String Table_name = "myTable";
    private static final String dataBase = "myDatabase.db";
    private static final String Col1 = "_id";
    private static final String Col2 = "Team1";
    private static final String Col3 = "Team2";
    private static final String Col4 = "Venue";
    private static final String Col5 = "Date";
    private static final String Col6 = "Time";
    private static final String Col7 = "Imgid1";
    private static final String Col8 = "Imgid2";

    DataBaseHelper(Context context) {
        super(context, dataBase, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create = "CREATE TABLE " + Table_name + "(" + Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT ," + Col2 + " VARCHAR(255) ," + Col3 + " VARCHAR(255) ," + Col4 + " VARCHAR(255) ," + Col5 + " VARCHAR(255) ," + Col6 + " VARCHAR(255) ," + Col7 + " BlOB ," + Col8 + " BLOB);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete = "DROP TABLE IF TABLE EXISTS" + Table_name;
        db.execSQL(delete);
    }

    public void addData(String team1, String team2, String venue, String date, String time, Bitmap img1, Bitmap img2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        if (team1 == "")
            contentvalues.putNull(Col2);
        if (team2 == "")
            contentvalues.putNull(Col3);
        if (venue == "")
            contentvalues.putNull(Col4);
        if (date == "")
            contentvalues.putNull(Col5);
        if (time == "")
            contentvalues.putNull(Col6);
        if (img1 == null)
            contentvalues.putNull(Col7);
        if (img2 == null)
            contentvalues.putNull(Col8);
        byte[] byteArray1 = new byte[0];
        if (img1 != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray1 = stream.toByteArray();
        }
        byte[] byteArray2 = new byte[0];
        if (img2 != null) {
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            img2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            byteArray2 = stream2.toByteArray();
        }

        contentvalues.put(Col2, team1);
        contentvalues.put(Col3, team2);
        contentvalues.put(Col4, venue);
        contentvalues.put(Col5, date);
        contentvalues.put(Col6, time);
        contentvalues.put(Col7, byteArray1);
        contentvalues.put(Col8, byteArray2);
        db.insert(Table_name, null, contentvalues);
    }

    public Cursor getdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(" Select * from " + Table_name + "", null);
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Cursor getUpdateData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + Table_name + " WHERE " + Col1 + " = ? ", new String[]{String.valueOf(id)}, null);
        return data;
    }

    public void updateData(int id, String team1, String team2, String venue, String date, String time, Bitmap img1, Bitmap img2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        byte[] byteArray1 = new byte[0];
        if (img1 != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray1 = stream.toByteArray();
        }
        byte[] byteArray2 = new byte[0];
        if (img2 != null) {
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            img2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            byteArray2 = stream2.toByteArray();
        }
        contentvalues.put(Col1, id);
        contentvalues.put(Col2, team1);
        contentvalues.put(Col3, team2);
        contentvalues.put(Col4, venue);
        contentvalues.put(Col5, date);
        contentvalues.put(Col6, time);
        contentvalues.put(Col7, byteArray1);
        contentvalues.put(Col8, byteArray2);
        db.update(Table_name, contentvalues, Col1 + " = ? ", new String[]{String.valueOf(id)});
    }

    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_name, Col1 + " = " + String.valueOf(id), null);
    }

    public Cursor getFixture(String team1) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery(" Select * from " + Table_name + " Where " + Col2 + " = ? OR " +Col3+ " = ? ", new String[]{team1,team1},null);
        return data;
    }
    public Cursor getFixture2(String team2){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(" Select * from " + Table_name + " Where " + Col3 + " = ? OR " +Col2+ " = ?", new String[]{team2,team2},null);
        return data;
    }
}

