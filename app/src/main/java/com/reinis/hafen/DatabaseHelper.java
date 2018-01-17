package com.reinis.hafen;

/**
 * Created by gagarin on 02.06.17.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    //todo change Database Name?
    private static String DB_NAME = "sound.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    void createDataBase() throws IOException {
        String DB_PATH_PREFIX = "/data/data/";
        String DB_PATH_SUFFIX = "/databases/";
        DB_PATH = DB_PATH_PREFIX + myContext.getPackageName()
                + DB_PATH_SUFFIX + DB_NAME;
        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;
        if (dbExist) {
            Log.d("Database exists", "Database Exists");
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e) {
            }
        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e) {
            }
        }
    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            checkDB.close();
            return true;
        } catch (SQLiteException e) {
            return false;
        }
    }
    private void copyDataBase() throws IOException {
        InputStream assetsDB = myContext.getAssets().open(DB_NAME);
        File directory = new File(DB_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
        OutputStream dbOut = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        Log.d("Coying Database!!"," Copying..");
        while ((length = assetsDB.read(buffer)) > 0) {
            dbOut.write(buffer, 0, length);
        }
        dbOut.flush();
        dbOut.close();
    }
    public void openDataBase() throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }
    SQLiteDatabase getDataBase() throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase;
    }
    @Override
    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
