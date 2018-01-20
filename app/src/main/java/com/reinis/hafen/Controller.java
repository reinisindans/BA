package com.reinis.hafen;

import android.content.Context;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gagarin on 17.01.18.
 */

public class Controller {



    //constructor placeholder

    Controller() {
    }

    //todo check_time()


    private double[] getDistances(LatLng location, ArrayList<Sound> sounds) {
        double[] distances= new double[sounds.size()];
        if (location != null) {


            for (int i = 0; i < sounds.size(); i++) {
                double lat_sound = sounds.get(i).getX_value();
                double lon_sound = sounds.get(i).getY_value();
                float[] dist = new float[2];
                Location.distanceBetween(location.latitude, location.longitude, lat_sound, lon_sound, dist);
                distances[i] = dist[0];
                Log.d("Calculating Distances", "Distance " + i + " = " + dist[0]);
            }
        }
          return distances;

    }


    ////////// Adjusting the Media Player Array based on Load Radius, to free up resources///////////

        //todo only crate the boolean loaded/unloaded array!! Loading itself takes place in MapsActivity
    private boolean[] findloadPlayers(double[] distances, double load_radius) {
        Log.d("checking load", "Start");

        boolean[] loaded= new boolean[distances.length];

            for (int i = 0; i < distances.length; i++) {
                Log.d("checking load", "Searching...");

                if (distances[i] < load_radius) {

                    Log.d("checking load", "Media player No. " + i + " true");
                loaded[i]=true;
                }


                if (distances[i] > load_radius) {
                    Log.d("checking load", "Media player No. " + i + " false");
                    loaded[i]=false;
                }


        }
        return loaded;
    }




    public void set_focus(Model model) {
        // reverting all focus settings to false
        for (int i=0;i < model.getSounds().length;i++) {
            model.getSounds()[i].setFocused(false);
        }

        if (model.getSoundsInDistance()!=null) {
            // if only one sound in distance!
            if (model.getSoundsInDistance().size() == 1) {
                Log.d("Setting focus","Focus on only sound in radius");
                //set the focused switch in sounds array
                for (int i=0; i<model.getSounds().length;i++) {
                    if (model.getSounds()[i].getView_ID()==0) {
                        model.getSounds()[i].setFocused(true);
                    }
                }
            }


            // if several sounds in distance
            else {
                // check if Playing, set focus!
                if (model.getPlaying()!=-1) {
                    Log.d("Setting focus","Searching for sound being played");
                    Log.d("Setting focus","playing = " +model.getPlaying());
                    int k= model.getSounds()[model.getPlaying()].getView_ID();
                    model.getSounds()[model.getPlaying()].setFocused(true);
                    Log.d("Playing sound"," "+model.getSounds()[model.getPlaying()].getName());
                }

                else {
                    // have to determine which sound is closest!
                    int index=(int)get_closest_distance(model.getDistances())[1];

                    Log.d("Setting focus","Focus on closest: " +model.getSounds()[index].getName());

                    model.getSounds()[index].setFocused(true);
                    }
                }
            }
        }



    private double[] get_closest_distance(double[] distances) {


        double[] set=new double[2];
        set[0]=1000;



        for (int i=0;i<distances.length;i++) {

            if (distances[i] < set[0]) {
                set[0] = distances[i];
                set[1]=i;

            }

        }

        return set;
    }


    private void set_View_ID(Sound[] sounds, TextView soundInDistance, int i){

                for (int k=0;k<sounds.length;k++) {
                    if (sounds[k].getName().equals(soundInDistance.getText())) {
                        //not needed!!!
                        sounds[k].setView_ID(i);
                        Log.d("Setting the View_ID","Sound FOUND " +sounds[k].getName()+" index: "+i);
                        break;
                    }
           }
      }

    private void getPlaying(Model model){
        model.setPlaying(-1);
        for (int i=0;i<model.getSounds().length;i++){
            if (model.getMedia_player(i)!=null) {

                if (model.getMedia_player(i).isPlaying()) {

                    model.setPlaying(-1);

                    Log.d("We are playing:"," No: " +i);
                    break;
                }
            }
        }
    }


    public void checkRadius(Model model, Context context, Vibrator v) {
        if (model.getDistances() != null) {
            for (int i = 0; i < model.getDistances().length; i++) {

                // newly inside radius= not yet registered as such
                if (model.getDistances()[i] < model.getSounds()[i].getRadius() && !model.getSounds()[i].getIn_distance()) {

                    model.getSounds()[i].setIn_distance(true);
                    Log.d("Searching for sounds", " New sound in distance found!");
                    // add to TextView array
                    TextView view = new TextView(context);

                    // NOT adding onClickListener for textViews!!!!!!!!!
                    //todo add onClickListener for textViews when adding the views to views_in_radius

                    view.setText(model.getSounds()[i].getName());
                    model.getSoundsInDistance().add(view);

                    Log.d("Searching for sounds", " Text view added" + model.getSounds()[i].getName() + " index: " + model.getSoundsInDistance().indexOf(view));
                    // HERE get the # (i) of corresponding sound, add to model.view_ids[i]- rewrite the set_View_ids
                    set_View_ID(model.getSounds(), view, model.getSoundsInDistance().indexOf(view));

                    //vibrate!
                    Log.d("Vibrating", "Enjoy!");

                    vibrate(v);


                }

                // NOT in radius, but registered as such -> leaving the sound circle
                if (model.getDistances()[i] > model.getSounds()[i].getRadius() && model.getSounds()[i].getIn_distance()) {
                    // set inDistance==false
                    model.getSounds()[i].setIn_distance(false);
                    // remove from TextView array
                    int id = model.getSounds()[i].getView_ID();

                    model.getSoundsInDistance().remove(id);
                    model.getSounds()[i].setView_ID(-1);
                    Log.d("Removin' View", " View " + id + " removed");


                    if (model.getSoundsInDistance() != null) {
                        for (int k = 0; k < model.getSoundsInDistance().size(); k++) {
                            set_View_ID(model.getSounds(), model.getSoundsInDistance().get(k), k);
                        }
                    }

                }
            }
        }
    }

    private void vibrate(Vibrator v){
        long[] pattern=new long[]{0,250,100,250,500,250,100,250};
        v.vibrate(pattern,-1);
    }


    public void change_text_sizes(ArrayList<TextView> soundsInDistance, Sound[] sounds){
    for (int i=0;i<sounds.length;i++){
        if (sounds[i].getFocused()){
            soundsInDistance.get(sounds[i].getView_ID()).setTextSize(30);
             }
        else {
            soundsInDistance.get(sounds[i].getView_ID()).setTextSize(12);
             }
         }
    }
    // check if the sound circle should be visible!!!!!!!!
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

    public boolean[] check_play_turn_preconditions(Sound[] sounds){
        Log.d("play turn?", "check_play_turn_preconditions: ");
        boolean[] play_turn_check=new boolean[sounds.length];

        for(int i=0;i<sounds.length;i++){
            // NO visibility!!!
            if (!sounds[i].getVisibility()){
                play_turn_check[i]=false;
            }
            // should be visible if other conditions are met
            else {
                // There are items in NOT array and at least one of them has been played!
                if(sounds[i].getNOT()!=null && sounds[i].getCheck_NOT()) {
                    play_turn_check[i] = false;
                }
                // There are items in NOT array, none have been played // OR No sounds NOT
                else if ((sounds[i].getNOT()!=null && !sounds[i].getCheck_NOT()) || sounds[i].getNOT()==null){
                    // NO sounds in AND_OR
                    if (sounds[i].getAND_OR()==null){
                        play_turn_check[i]=true;
                    }
                    //At least some of AND_OR conditions have been met
                    else if (sounds[i].getCheck_AND_OR()){
                        play_turn_check[i]=true;
                    }
                    // NONE of the AND_OR conditions have been met
                    else if (!sounds[i].getCheck_AND_OR()){
                        play_turn_check[i]=false;
                    }
                    else {
                        Log.d("play_turn_preconditions", "THIS SHOULD NOT APPEAR, checking: " +sounds[i].getName());
                    }
                }
            }

        }
        return play_turn_check;
    }

    public boolean[] check_speed(Sound[] sounds, double current_speed){
        Log.d("Checking speeds", "check_speed: ");
        boolean[] speed_check=new boolean[sounds.length];

        for (int i=0;i<sounds.length;i++){

            if (sounds[i].getMax_speed()>current_speed && current_speed>=sounds[i].getMin_speed()){
                speed_check[i]=true;
            }
            else{
                speed_check[i]=false;
            }
        }

        return speed_check;
    }

    public boolean[] check_time(Sound[] sounds){
        boolean[] time_check=new boolean[sounds.length];
        // todo in future versions!!!
        return time_check;
    }

    public void adjust_volume(MediaPlayer[] media_players, Sound[] sounds){
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getIn_distance()){
                float volume= (float)sounds[i].getVolume();
                media_players[i].setVolume(volume,volume);
            }
        }
    }

    public boolean[] check_controls(Sound[] sounds){
        boolean[] control_check=new boolean[sounds.length];

        for (int i=0;i<sounds.length;i++){
            control_check[i]=sounds[i].getControls();
        }
        return control_check;
    }

    public boolean[] check_repeat(Sound[] sounds){
        Log.d("repeat check", " : ");
        boolean[] repeat_check=new boolean[sounds.length];
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getRepeat()==-1){
                repeat_check[i]=true;
            }
            else if (sounds[i].getRepeat()-sounds[i].getTimes_played()==0){
                repeat_check[i]=false;
            }
            else {
                repeat_check[i]=true;
            }
        }
        return repeat_check;
    }

    public boolean[] check_direction(Sound[] sounds){
        Log.d("Check direction", " : ");
        boolean[] direction_check=new boolean[sounds.length];

        for (int i=0;i<sounds.length;i++){
            // check against user direction
            direction_check[i]=false;

            for (int k=0;k<sounds[i].getApproach_dir().length;k++){
                //if the second directional parameter is smaller than first= +360 (2*PI)
                double user_dir=sounds[i].getUser_direction();
                double[] app_dir_new=sounds[i].getApproach_dir()[k];
                if (sounds[i].getApproach_dir()[k][1]<=sounds[i].getApproach_dir()[k][0]){
                    if (user_dir<app_dir_new[0]){
                        //adding 360 also to User direction, if it is between 0 and
                        user_dir=user_dir+360;
                    }
                    app_dir_new[1]=sounds[i].getApproach_dir()[k][1]+360;
                }
                if (user_dir>app_dir_new[0] && user_dir < app_dir_new[1]){
                    direction_check[i]=true;
                }
            }
        }

        return direction_check;
    }

}
