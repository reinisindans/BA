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

    final private double player_load_radius=200;
    final private String TAG= "model";

    private Sound[] sounds;
    private ArrayList<Circle> circleList;
    private User user;
    private int playing_with_controls=-1;
    private ArrayList<TextView> soundsInDistance;


    //CHECKS
    private boolean[][] checks;
    private boolean[] check_turn;



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
        translateAND_OR();
        translateNOT();
        circleList= new ArrayList<>();
        check_turn=new boolean[sounds.length];
    }


    public Sound[] getSounds() {
        return sounds;
    }

    public ArrayList<TextView> getSoundsInDistance() {
        return soundsInDistance;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    public int getPlaying_with_controls() {
        return playing_with_controls;
    }

    private void translateNOT() {

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

    private void translateAND_OR() {
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

    // Checking the NOT arrays against sounds already played
    private void check_NOT() {
        Log.d(TAG, "check_NOT: starting");
        for (Sound s : sounds) {
            for (int i = 0; i < s.getNOT().length; i++) {
                boolean played=sounds[s.getNOT()[i]].get_played();
                if (played==true){
                    s.setCheck_NOT(false);
                    Log.d(TAG, "check_NOT: forbidden element played! checkNot set to False!");
                    break;
                }
            }

        }
    }

    private void check_AND_OR() {
        Log.d(TAG, "check_AND_OR: Starting");
        boolean check = true;
        for (Sound s : sounds) {
            Log.d(TAG, "check_AND_OR: "+s.getName());
            boolean[] rows = new boolean[s.getAND_OR().length];
            for (int i = 0; i < s.getAND_OR().length; i++) {
                boolean[] items = new boolean[s.getAND_OR()[i].length];
                for (int k = 0; k < s.getAND_OR()[i].length; k++) {
                    items[i] = sounds[s.getAND_OR()[i][k]].get_played();
                }
                rows[i] = true;
                for (boolean item : items) {
                    rows[i] = item && rows[i];
                }
            }
            for (boolean r : rows) {
                check = r && check;
                Log.d(TAG, "check_AND_OR: iterate rows: "+s.getName()+", "+check);
            }
            s.setCheck_AND_OR(check);
        }
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
    private void check_visibility(){
        Log.d("check_visibility()", "Starting: ");

        for(Sound s:sounds){
            // NO visibility by default!
            if (!s.getVisibility()){
                s.setIs_visible(false);
                Log.d("check_visibility()", "no Visibility ");
            }
            // should be visible if the preconditions about previously played tracks are met
            else {
                s.setIs_visible(s.isTurn());
                Log.d("check_visibility()", "depends on check_turn()");
            }

        }
    }


    // Check if it is the play-turn for the sounds
    public void check_turn(){
        Log.d(TAG, "check_play_turn_preconditions: ");

        for(Sound s:sounds){

                // There are items in NOT array and at least one of them has been played!
                if(s.getNOT()!=null && s.getCheck_NOT()) {
                    s.setTurn(false);
                }
                // There are items in NOT array, none have been played // OR No sounds NOT
                else if ((s.getNOT()!=null && !s.getCheck_NOT()) || s.getNOT()==null){
                    // NO sounds in AND_OR
                    if (s.getAND_OR()==null){
                        s.setTurn(true);
                    }
                    //At least some of AND_OR conditions have been met
                    else if (s.getCheck_AND_OR()){
                        s.setTurn(true);
                    }
                    // NONE of the AND_OR conditions have been met
                    else if (!s.getCheck_AND_OR()){
                        s.setTurn(false);
                    }
                    else {
                        Log.d("play_turn_preconditions", "THIS SHOULD NOT APPEAR, checking: " +s.getName());
                    }
                }
        }
    }
    //todo: check player loading! maybe save the already started players.-or- save the play position!
}
