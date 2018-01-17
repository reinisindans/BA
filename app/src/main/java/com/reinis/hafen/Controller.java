package com.reinis.hafen;

import android.location.Location;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by gagarin on 17.01.18.
 */

public class Controller {



    //constructor placeholder

    Controller() {
    }



    //todo: getDistances() loadPlayers() checkRadius() check_visibility() check_play_turn()
    //todo check_time() check_speed() check_direction() check_controls() adjust_volume()
    //todo get_played() check_repeat()


    private void getDistances() {

        if (location != null && sounds != null) {

            for (int i = 0; i < sounds.size(); i++) {
                double lat_sound = sounds.get(i).getX_value();
                double lon_sound = sounds.get(i).getY_value();
                float[] dist = new float[2];
                Location.distanceBetween(location.latitude, location.longitude, lat_sound, lon_sound, dist);
                distances[i] = dist[0];
                Log.d("Calculating Distances", "Distance " + i + " = " + dist[0]);
            }
        }


    }


    ////////// Adjusting the Media Player Array based on Load Radius, to free up resources///////////


    private void loadPlayers() {

        Log.d("Creating Media Players", "Start");
        if (distances != null) {
            for (int i = 0; i < distances.length; i++) {
                Log.d("Creating Media Players", "Searching...");

                if (distances[i] < load_radius && !sounds.get(i).getLoaded()) {
                    Log.d("Creating Media Players", "Preparing to load "+ sounds.get(i).getName());
                    // run async Media player prepare!
                    String path = sounds.get(i).getFile_path();
                    Log.d("Creating Media Players", "path recovered: "+sounds.get(i).getFile_path());
                    MediaPlayer player = MediaPlayer.create(this, getResources().getIdentifier(path,
                            "raw", getPackageName()));
                    Log.d("Creating Media Players", "Media player No. " + i + " SET");
                    player.setLooping(false);
                    media_players[i] = player;

                    sounds.get(i).setLoaded(true);

                }


                if (distances[i] > load_radius && sounds.get(i).getLoaded()) {
                    media_players[i].release();
                    media_players[i] = null;

                    sounds.get(i).setLoaded(false);

                    Log.d("Creating Media Players", "Media player No. " + i + " REMOVED");
                }

            }
        }

    }


    // todo rewrite focus code!!!!  Separate setting focus switch in Model from setting the font size of Views
    // todo 1) determine, set 'focus' attribute in Sounds Array. 2) Method to change the font sizes in Sounds_in_distance Array
    public void check_text_focus() {

        getPlaying();

        for (int i=0;i < sounds.size();i++) {
            sounds.get(i).setFocused(false);


        }

        if (soundsInDistance!=null) {
            // if only one sound in distance!
            if (soundsInDistance.size() == 1) {
                Log.d("Setting focus","Focus on only sound in radius");
                soundsInDistance.get(0).setTextSize(30);

                //set the focused switch in sounds array
                for (int i=0; i<sounds.size();i++) {
                    if (sounds.get(i).getView_ID()==0) {
                        sounds.get(i).setFocused(true);

                        // Setting seek bar!
                        Log.d("Setting seekBar","Media player no "+i);

                        if (media_players[i]!=null) {
                            seekBar.setMax(media_players[i].getDuration());
                            track_duration.setText(milliSecondsToTime(media_players[i].getDuration()));
                        }
                    }
                }
            }


            // if several sounds in distance
            else {
                for (int i = 0; i < soundsInDistance.size(); i++) {

                    // set all text views to default text size
                    soundsInDistance.get(i).setTextSize(12);

                }

                // check if Playing, set focus!
                if (playing !=-1) {



                    Log.d("Setting focus","Searching for sound being played");
                    Log.d("Setting focus","playing = " +playing);
                    Log.d("Playing No "," No "+playing);
                    int k= sounds.get(playing).getView_ID();
                    sounds.get(playing).setFocused(true);
                    soundsInDistance.get(k).setTextSize(30);

                    Log.d("Playing sound"," "+sounds.get(playing).getName());
                    seekBar.setMax(media_players[playing].getDuration());
                    track_duration.setText(milliSecondsToTime(media_players[playing].getDuration()));

                }

            }
        }
        if(playing==-1) {

            double dist=get_closest_distance();
            Log.d("Setting focus","Focus on closest" +dist);
            for (int i=0;i<distances.length;i++) {

                if (dist==distances[i]) {
                    Log.d("Setting focus","found nearest distance" +dist);

                    for (int k = 0; k < soundsInDistance.size(); k++) {
                        Log.d("Setting focus","Searching for correct View_ID " +soundsInDistance.get(k).getText());
                        if (sounds.get(i).getView_ID()==k) {
                            Log.d("Setting focus","FOUND correct View_ID " +soundsInDistance.get(k).getText());
                            sounds.get(i).setFocused(true);
                            soundsInDistance.get(k).setTextSize(30);

                            if (media_players[i]!=null){
                                seekBar.setMax(media_players[i].getDuration());
                                track_duration.setText(milliSecondsToTime(media_players[i].getDuration()));
                                track_position.setText(milliSecondsToTime(media_players[i].getCurrentPosition()));
                                break;
                            }

                        }

                    }

                }
            }


        }
    }


    private double get_closest_distance() {

        //
        double min=10000;

        for (double distance : distances) {

            if (distance < min) {
                min = distance;
            }

        }

        return min;
    }


    private void set_View_IDs(){

        for (int i=0;i<sounds.size();i++){
            sounds.get(i).setView_ID(-1);
        }

        if (soundsInDistance!=null) {
            for (int i=0;i< soundsInDistance.size();i++) {
                for (int k=0;k<sounds.size();k++) {
                    if (sounds.get(k).getName().equals(soundsInDistance.get(i).getText())) {
                        //not needed!!!
                        sounds.get(k).setView_ID(i);
                        Log.d("Setting the View_ID","Sound FOUND " +sounds.get(k).getName()+" index: "+i);
                    }

                }

            }

        }

    }


    private void getPlaying(){

        playing=-1;

        for (int i=0;i<sounds.size();i++){


            if (media_players[i]!=null) {

                if (media_players[i].isPlaying()) {

                    playing=i;

                    Log.d("We are playing:"," No: " +i);
                }

            }

        }

    }





}
