package com.reinis.hafen;

import android.location.Location;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by root on 01.01.18.
 */


/**
 * The Model Class acts as the main data storage, operative memory, to be used by all Functions
 */
public class Model {


    private Sound[] sounds;
    // Populating the Sounds list and creating the MediaPlayers Array!
    private MediaPlayer[] media_players;
    private double[] distances;
    private int playing;
    private LatLng location;
    private ArrayList<TextView> soundsInDistance;
    private double speed;
    private boolean[][] checks;



    /*todo rearrange the structure to include a boolean[] checks=new boolean[sounds.length][??],
      todo put the results of all 'check' methods there. Put 'check' methods under Model! determine how many of them are there
     */
    // Constructors

    Model(Sound[] sounds) {
        this.sounds = sounds;   //all the sounds
        this.media_players = new MediaPlayer[sounds.length];
        this.distances = new double[sounds.length];
        this.playing = -1;
        // calculate the centroid coordinates of the coverage area
        if (this.location == null) {
            double X = 0;
            double Y = 0;

            for (int i = 0; i < sounds.length; i++) {
                X = X + sounds[i].getX_value();
                Y = Y + sounds[i].getY_value();
            }
            X = X / (sounds.length);
            Y = Y / (sounds.length);

            this.location = new LatLng(X, Y);


        }

        translateAND_OR(this.sounds);
        translateNOT(this.sounds);
    }


    public Sound[] getSounds() {
        return sounds;
    }

    public void setSounds(Sound[] sounds) {
        this.sounds = sounds;
    }


    public MediaPlayer[] getMedia_players() {
        return media_players;
    }

    public MediaPlayer getMedia_player(int index) {
        return media_players[index];
    }

    public void setMedia_players(MediaPlayer[] media_players) {
        this.media_players = media_players;
    }

    public void setMedia_player(int index, MediaPlayer player) {
        this.media_players[index] = player;
    }

    public double[] getDistances() {
        return distances;
    }

    public void setDistances(double[] distances) {
        this.distances = distances;
    }

    public int getPlaying() {
        return playing;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public ArrayList<TextView> getSoundsInDistance() {
        return soundsInDistance;
    }

    public void setSoundsInDistance(ArrayList<TextView> soundsInDistance) {
        this.soundsInDistance = soundsInDistance;
    }

    public void addToSoundInDistance(TextView soundInDistance) {
        this.soundsInDistance.add(soundInDistance);
    }

    private ArrayList<Integer> get_played() {
        ArrayList<Integer> played = new ArrayList<>();

        for (int i = 0; i < sounds.length; i++) {
            if (sounds[i].getTimes_played() > 0) {
                played.add(i);
            }
        }
        return played;
    }

    /**
     * This method turns the Array of Names to be used in SQL conditionals into corressponding sound indexes
     *
     * @param sounds
     */
    private void translateNOT(Sound[] sounds) {

        for (int i = 0; i < sounds.length; i++) {
            if (sounds[i].getNOT_string() == null) {
                sounds[i].setNOT(null);
            } else {
                Integer[] NOT = new Integer[sounds[i].getNOT_string().length];
                for (int k = 0; k < sounds[i].getNOT_string().length; k++) {
                    for (int j = 0; j < sounds.length; j++) {
                        if (sounds[j].getName().equals(sounds[i].getNOT()[k])) {
                            //index of the same name found
                            NOT[i] = j;
                        }
                    }
                }
                sounds[i].setNOT(NOT);
            }
        }
    }


    private void translateAND_OR(Sound[] sounds) {
        for (int i = 0; i < sounds.length; i++) {
            //if null
            Log.d("Translating AND_OR", "translateAND_OR: start,  " + sounds[i].getName());
            if (sounds[i].getAND_OR_string() == null) {
                Log.d("Translating AND_OR", sounds[i].getName() + " : AND_OR array is NULL");
                sounds[i].setAND_OR(null);
            }
            // if full
            else {
                Log.d("Translating AND_OR", sounds[i].getName() + " : AND_OR array is NOT null");
                Integer[][] AND_OR = new Integer[sounds[i].getAND_OR_string().length][];
                for (int k = 0; 0 < sounds[k].getAND_OR_string().length; k++) {
                    for (int j = 0; k < sounds[i].getAND_OR_string()[k].length; k++) {
                        Log.d("Translating AND_OR", sounds[i].getName() + " : adding: " + sounds[i].getAND_OR_string()[k][j]);
                        for (int l = 0; l < sounds.length; l++) {
                            if (sounds[l].getName().equals(sounds[i].getAND_OR_string()[k][j])) {

                                AND_OR[k][j] = l;
                            }
                        }
                    }
                }
                sounds[i].setAND_OR(AND_OR);
            }
        }
    }

    private boolean check_NOT(Integer[] NOT, Sound[] sounds) {
        Log.d("", "check_NOT: ");
        boolean check = false;
        for (int i = 0; i < NOT.length; i++) {
            check = sounds[NOT[i]].get_played();
        }
        return check;
    }

    private boolean check_AND_OR(Integer[][] AND_OR, Sound[] sounds) {
        Log.d("", "check_AND_OR: ");
        boolean check = true;
        boolean[] rows = new boolean[AND_OR.length];
        for (int i = 0; i < AND_OR.length; i++) {
            boolean[] items = new boolean[AND_OR[i].length];
            for (int k = 0; k < AND_OR[i].length; k++) {
                items[i] = sounds[AND_OR[i][k]].get_played();
            }
            rows[i] = true;
            for (boolean item : items) {
                rows[i] = item && rows[i];
            }
        }
        for (boolean r : rows) {
            check = r && check;
        }

        return check;
    }

    public void set_AND_OR_NOT_checks() {

    for (int i = 0;i<sounds.length;i++){
        sounds[i].setCheck_AND_OR(check_AND_OR(sounds[i].getAND_OR(),sounds));
        sounds[i].setCheck_NOT(check_NOT(sounds[i].getNOT(),sounds));
      }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void set_volume(double vol_mod_ind){
        Log.d("Adjusting volumes", ": ");
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getIn_distance()){
            double vol_mod = Math.pow(((sounds[i].getRadius() - distances[i]) / sounds[i].getRadius()), 1 / vol_mod_ind);
            double volume=1 - (Math.log(100 - (vol_mod_ind * 100)) / Math.log(100));
            sounds[i].setVolume(volume);}
            else {
            }
        }
    }

    public void calculate_user_direction(){
        Log.d("User direction", "calculate_user_direction: ");

        for (int i=0;i<sounds.length;i++){
            double X_sound=sounds[i].getX_value();
            double Y_sound=sounds[i].getY_value();
            double X_user=location.latitude;
            double Y_user=location.longitude;
            double user_dir=Math.atan2(Y_user-Y_sound,X_user-X_sound)*(180/Math.PI);
            Log.d("User direction", " : "+user_dir);
            sounds[i].setUser_direction(user_dir);
        }

    }

}
