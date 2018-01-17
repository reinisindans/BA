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
    //todo don't forget to check the correct removal of view_ids!!

    // Constructors

    Model(Sound[] sounds){
        this.sounds=sounds;   //all the sounds
        this.media_players=new MediaPlayer[sounds.length];
        this.distances= new double[sounds.length];
        this.playing=-1;
        // calculate the centroid coordinates of the coverage area
        if (this.location==null) {
            double X=0;
            double Y=0;

            for (int i=0;i<sounds.length;i++) {
                X=X+sounds[i].getX_value();
                Y=Y+sounds[i].getY_value();
            }
            X=X/(sounds.length);
            Y=Y/(sounds.length);

            this.location= new LatLng(X,Y);


        }

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

    public void setMedia_player(int index,MediaPlayer player){
        this.media_players[index]=player;
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

}
