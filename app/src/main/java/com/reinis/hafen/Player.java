package com.reinis.hafen;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gagarin on 15.08.17.
 */

public class Player extends Service{

    // Variables
    MediaPlayer[] mediaPlayers;
    String[] filepath;

    public void setPlayers(MediaPlayer[] players){
        mediaPlayers=players;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String[] filepath= (String[]) intent.getSerializableExtra("filepath");
        int size= filepath.length;
        Bundle mediaPlayerBundle= intent.getExtras();
        this.filepath =filepath;
        mediaPlayers= new MediaPlayer[size];
        return START_STICKY;



    }

    private void play(int player_ID){
        mediaPlayers[player_ID].start();

    }

    private void pause(int player_ID){
        mediaPlayers[player_ID].pause();
    }

    private void load(int player_ID){
        String path = filepath[player_ID];
        Log.d("Creating Media Players", "path recovered: "+filepath[player_ID]);
        MediaPlayer player = MediaPlayer.create(this, getResources().getIdentifier(path,
                "raw", getPackageName()));
        mediaPlayers[player_ID]=player;

    }

    private void unload(int player_ID){
        mediaPlayers[player_ID].release();
        mediaPlayers[player_ID] = null;
    }

}

