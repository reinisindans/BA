package com.reinis.hafen;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class saveDatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "save.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "saved_state";
    private static final String SAVE_TABLE_ID = "id"; // Integer
    private static final String SAVE_TABLE_TIMES_PLAYED = "times_played"; // Integer
    private static final String SAVE_TABLE_DELAY_END = "delay_end_time"; // long
    private static final String SAVE_TABLE_MANUAL_START = "manual_start"; // boolean
    private static final String SAVE_TABLE_STARTED = "started_playback"; // boolean
    private static final String SAVE_TABLE_PLAYER_POSITION = "player_position"; // int

    private static String TAG = "saveToDataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME ="save.db" ;// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    ContentValues values = new ContentValues();
    private static boolean tableCreated=false;

    saveDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;

    }

    public Context getmContext(){
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // if database does not exist, create the tables :
        if (!tableCreated) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    SAVE_TABLE_ID + " INTEGER PRIMARY KEY, " +
                    SAVE_TABLE_TIMES_PLAYED + "INTEGER" +
                    SAVE_TABLE_DELAY_END + " INTEGER, " +
                    SAVE_TABLE_MANUAL_START + " INTEGER, " +
                    SAVE_TABLE_STARTED + " INTEGER, " +
                    SAVE_TABLE_PLAYER_POSITION + "INTEGER)");
            tableCreated=true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSound(Sound s) {
        SQLiteDatabase db = getWritableDatabase();
        int timesPlayed = s.getTimes_played();
        long delayEnd = s.getDelay_end_time();

        int manual;
        if (s.isManualStartStop()) {
            manual=1;
        }
        else{
            manual=0;
        }

        int started;
        if (s.get_played()){
            started=1;
        }
        else {
            started=0;
        }

        int position=s.getPlayerPosition();


        ContentValues contentValues = new ContentValues();
        contentValues.put(SAVE_TABLE_TIMES_PLAYED, timesPlayed);
        contentValues.put(SAVE_TABLE_DELAY_END, delayEnd);
        contentValues.put(SAVE_TABLE_MANUAL_START, manual);
        contentValues.put(SAVE_TABLE_STARTED, started);
        contentValues.put(SAVE_TABLE_PLAYER_POSITION, position);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updatePerson(Sound s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int id=s.getID_sound();

        int timesPlayed = s.getTimes_played();
        long delayEnd = s.getDelay_end_time();

        int manual;
        if (s.isManualStartStop()) {
            manual=1;
        }
        else{
            manual=0;
        }

        int started;
        if (s.get_played()){
            started=1;
        }
        else {
            started=0;
        }

        int position=s.getPlayerPosition();

        contentValues.put(SAVE_TABLE_TIMES_PLAYED, timesPlayed);
        contentValues.put(SAVE_TABLE_DELAY_END, delayEnd);
        contentValues.put(SAVE_TABLE_MANUAL_START, manual);
        contentValues.put(SAVE_TABLE_STARTED, started);
        contentValues.put(SAVE_TABLE_PLAYER_POSITION, position);
        db.update(TABLE_NAME, contentValues, SAVE_TABLE_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    //Open the database, so we can query it
    public SQLiteDatabase openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        return mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        //return mDataBase != null;
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    public boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }
}
