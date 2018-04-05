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


// todo put to MODEl
    public void adjust_volume(MediaPlayer[] media_players, Sound[] sounds){
        for (int i=0;i<sounds.length;i++){
            if (sounds[i].getIn_distance()){
                float volume= (float)sounds[i].getVolume();
                media_players[i].setVolume(volume,volume);
            }
        }
    }

    // todo put in MODEL
    public boolean[] check_controls(Sound[] sounds){
        boolean[] control_check=new boolean[sounds.length];

        for (int i=0;i<sounds.length;i++){
            control_check[i]=sounds[i].getControls();
        }
        return control_check;
    }

    // todo put in SOUNDS+ MODEL
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

    // todo put in MODEL
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

    public boolean determine_play(boolean[] check_radius, boolean[] check_play_turn,boolean[] check_speed, boolean[] check_direction, boolean[] check_repeat, boolean[] check_controls){
        boolean play= false;


        return play;
    }

}
