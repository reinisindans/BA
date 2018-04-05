package com.reinis.hafen;

import android.location.Location;
import android.location.LocationManager;
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
    private MediaPlayer[] media_players;
    private ArrayList<Circle> circleList;
    private User user;
    private int playing_with_controls=-1;
    private ArrayList<TextView> soundsInDistance;
    private boolean[][] checks;
    final private double player_load_radius=200;
    private String TAG= "model";



    /*todo rearrange the structure to include a boolean[] checks=new boolean[sounds.length][??],
      todo put the results of all 'check' methods there. Put 'check' methods under Model! determine how many of them are there
     */
    // Constructors

    Model(Sound[] sounds) {
        this.sounds = sounds;   //all the sounds

        // create the user
        this.user= new User(sounds.length);
        // calculate the centroid coordinates of the coverage area
        if (user.getLocation() == null) {
            double X = 0;
            double Y = 0;

            for (int i = 0; i < sounds.length; i++) {
                X = X + sounds[i].getX_value();
                Y = Y + sounds[i].getY_value();
            }
            X = X / (sounds.length);
            Y = Y / (sounds.length);
            Location new_loc= new Location(LocationManager.GPS_PROVIDER);
            new_loc.setLatitude(X);
            new_loc.setLongitude(Y);
            this.user.setLocation(new_loc);


        }

        translateAND_OR(this.sounds);
        translateNOT(this.sounds);
        circleList= new ArrayList<>();
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

    /*public void setSpeed(double speed) {
        this.speed = speed;
    }
    */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(ArrayList<Circle> circleList) {
        this.circleList = circleList;
    }

    public int getPlaying_with_controls() {
        return playing_with_controls;
    }

    // todo put in MODEL
    public void set_focus() {
        // reverting all focus settings to false
        for (int i=0;i < sounds.length;i++) {
            sounds[i].setFocused(false);
        }

        if (soundsInDistance!=null) {
            // if only one sound in distance!
            if (soundsInDistance.size() == 1) {
                Log.d("Setting focus","Focus on only sound in radius");
                //set the focused switch in sounds array
                for (int i=0; i<sounds.length;i++) {
                    if (sounds[i].getView_ID()==0) {
                        sounds[i].setFocused(true);
                    }
                }
            }


            // if several sounds in distance
            else {
                // check if Playing, set focus!
                if (playing_with_controls!=-1) {
                    Log.d("Setting focus","Searching for sound being played");
                    Log.d("Setting focus","playing = " +playing_with_controls);
                    int k= sounds[playing_with_controls].getView_ID();
                    sounds[playing_with_controls].setFocused(true);
                    Log.d("Playing sound"," "+sounds[playing_with_controls].getName());
                }

                else {
                    // have to determine which sound is closest! all sounds!!
                    int index=(int)get_closest_distance()[1];

                    Log.d("Setting focus","Focus on closest: " +sounds[index].getName());

                    sounds[index].setFocused(true);
                }
            }
        }
    }
    // todo put in MODEL
    private double[] get_closest_distance() {

        // array= {distance,ID}
        double[] set = new double[2];
        set[0] = 10000;

            for (int i = 0; i < sounds.length; i++) {
                // only set focus on visible sounds
                if (sounds[i].getUser_distance() < set[0] && sounds[i].getVisibility()) {
                    set[0] = sounds[i].getUser_distance();
                    set[1] = i;



            }
        }
        return set;
    }

    private void setPlaying_with_controls(){
        playing_with_controls=-1;
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getMedia_player()!=null) {

                if (sounds[i].getMedia_player().isPlaying() && sounds[i].getControls()) {

                    playing_with_controls=i;

                    Log.d("We are playing:"," No: " +i+" --> "+sounds[i].getName());
                    break;
                }
            }
        }
    }

    public void setSoundsInDistanceView(){

        for (Sound s:sounds){
            //if sound in radius and visible-> but not yet added to view ArrayList
            if (s.isIn_distance() && s.getControls() && !s.isView_in_distance()){
                s.setView_in_distance(true);
                soundsInDistance.add(s.getView());
                Log.d(TAG, " View " + s.getName() + " added");
            }

            // if not in radius but in the textView array!
            else if(!s.isIn_distance() && s.isView_in_distance()){
                s.setView_in_distance(false);
                soundsInDistance.remove(s.getView());
                Log.d(TAG, " View " + s.getName() + " removed");
            }
        }
    }

    public void change_text_sizes(){
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].isFocused()){
                soundsInDistance.get(sounds[i].getView_ID()).setTextSize(30);
            }
            else {
                soundsInDistance.get(sounds[i].getView_ID()).setTextSize(12);
            }
        }
    }

    // check if the sound circle should be visible!!!!!!!!
    // todo put in MODEL!
    private boolean[] check_visibility(Sound[] sounds, boolean[] turn_precondition_values){
        Log.d("check_visibility()", "Starting: ");
        boolean[] visibility_check=new boolean[sounds.length];

        for(int i=0;i<sounds.length;i++){
            // NO visibility by default!
            if (!sounds[i].getVisibility()){
                visibility_check[i]=false;
            }
            // should be visible if the preconditions about previously played tracks are met
            else {
                visibility_check[i]= turn_precondition_values[i];
            }

        }

        return visibility_check;
    }


    //todo: check player loading! maybe save the already started players.-or- save the play position!
}
