package com.reinis.hafen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

public class loadDatabaseTranslator {

        private Cursor dbCursor;
        private SQLiteDatabase database;
        String TAG = "load saved database";

        private ArrayList<Integer> ID_list =new ArrayList<>();
        private ArrayList<Integer> timesPlayed_list= new ArrayList<>();
        private ArrayList<Long> delayEndTime_list= new ArrayList<>();
        private ArrayList<Boolean> manualStart_list= new ArrayList<>();
        private ArrayList<Boolean> playbackStarted_list=new ArrayList<>();
        private ArrayList<Integer> playerPosition_list= new ArrayList<>();


        //constructor
    loadDatabaseTranslator(saveDatabaseHelper dbWriter) {
    try {
        database = dbWriter.openDataBase();

        Log.d("Getting Saves!", " Database read!" + database.getPath());


        dbCursor = database.rawQuery("SELECT * FROM saved_state;",
                null);

        Log.d("Cursor Indexing", "Preparing");
        dbCursor.moveToFirst();

        ///! here continue coding!!!!!!!!
        int id_index = dbCursor.getColumnIndex("id");
        int timesPlayed_index = dbCursor.getColumnIndex("times_played");
        int delay_index = dbCursor.getColumnIndex("delay_end_time");
        int manualStart_index = dbCursor.getColumnIndex("manual_start");
        int playbackStarted_index = dbCursor.getColumnIndex("started_playback");
        int position_index = dbCursor.getColumnIndex("player_position");

        Log.d(TAG, "Indexing done");

        while (!dbCursor.isAfterLast()) {
            //Log.d("Reading Cursor", "Row " + dbCursor.getString(name_index)+" <-------------------------------");
            int id = dbCursor.getInt(id_index);
            Log.d(TAG, "loadDatabaseTranslator: ID got =" +id);
            int timesPlayed=dbCursor.getInt(timesPlayed_index);
            long delayEndTime=(long)dbCursor.getInt(delay_index);
            Log.d(TAG, "loadDatabaseTranslator: delay end time got");
            boolean manualStart;

            if (dbCursor.getInt(manualStart_index)==1){
                manualStart=true;
            }
            else {
                manualStart=false;
            }
            Log.d(TAG, "loadDatabaseTranslator: manual start got: "+manualStart);

            boolean playbackStarted;
            if (dbCursor.getInt(playbackStarted_index)==1){
                playbackStarted=true;
            }
            else {
                playbackStarted=false;
            }
            Log.d(TAG, "loadDatabaseTranslator: playbackStarted got: "+playbackStarted);

            int playerPosition= dbCursor.getInt(position_index);

            //add values to value arrays

            ID_list.add(id);
            timesPlayed_list.add(timesPlayed);
            delayEndTime_list.add(delayEndTime);
            manualStart_list.add(manualStart);
            playbackStarted_list.add(playbackStarted);
            playerPosition_list.add(playerPosition);
            Log.d(TAG, "loadDatabaseTranslator: values added to list: "+ id);

            dbCursor.moveToNext();

        }




    }

    finally {
        if (database != null) {
            dbWriter.close();
            dbCursor.close();
            }
        }
    }

    public ArrayList<Integer> getID_list() {
        return ID_list;
    }

    public ArrayList<Integer> getTimesPlayed_list() {
        return timesPlayed_list;
    }

    public ArrayList<Long> getDelayEndTime_list() {
        return delayEndTime_list;
    }

    public ArrayList<Boolean> getManualStart_list() {
        return manualStart_list;
    }

    public ArrayList<Boolean> getPlaybackStarted_list() {
        return playbackStarted_list;
    }

    public ArrayList<Integer> getPlayerPosition_list() {
        return playerPosition_list;
    }
}
