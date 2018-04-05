package com.reinis.hafen;

/**
 * Created by gagarin on 02.06.17.
 */

//todo SOUND DONE

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;


class Sound implements Serializable {

    // TAG for logs
    private static final String TAG = "SoundClass";

    //Random number generator
    private Random RND= new Random();

    /*
  Declare Variables
   */
    private int ID_sound;
    private double x_value;
    private double y_value;
    private double radius;
    private String name;
    private String author;
    private String description;
    private String file_path;
    private String circle_ID;
    private int view_ID;
    private int color;
    private boolean visibility;
    private boolean controls;
    private double min_speed;
    private double max_speed;
    private double[][] approach_dir;
    private Integer[][] AND_OR;
    private Integer[] NOT;
    private String[][] AND_OR_string;
    private String[] NOT_string;
    private int repeat;
    private double[] delay;
    private Context context;
    private TextView view;


    // STATES
    private boolean is_visible;
    private Integer times_played;
    private boolean playing;
    private boolean in_distance;
    private float volume; // maybe not needed
    private Location location;
    private boolean loaded;
    private boolean focused;
    private double user_direction;
    private double user_distance;
    private long delay_end_time;
    private MediaPlayer media_player;
    private CircleOptions circleOptions;
    private boolean view_in_distance;



    //CHECKS!
    private boolean check_AND_OR;
    private boolean check_NOT;
    private boolean checkDelay;
    private boolean check_turn;
    private boolean check_speed;


//todo export all the boolean values except visibility,controls, to Model boolean[] checks.

/*
    Declare Constructors
     */


    Sound(Context app_context, int ID_sound, double x_value, double y_value, double radius, String name, String author, String description, int repeat, double[] delay, String file_path, int color, boolean visibility, boolean controls, double min_speed,double max_speed, double[][] approach_dir, String[][] AND_OR, String[] NOT)
    {
        this.ID_sound=ID_sound;
        this.x_value=x_value;
        this.y_value=y_value;
        this.radius=radius;
        this.name=name;
        this.author=author;
        this.description=description;
        this.repeat=repeat;
        this.delay=delay;
        this.file_path=file_path;
        this.playing=false;
        this.color = color ;
        this.visibility=visibility;
        this.is_visible=false;
        this.controls=controls;
        this.min_speed=min_speed;
        this.max_speed=max_speed;
        this.approach_dir=approach_dir;
        this.AND_OR_string=AND_OR;
        this.NOT_string=NOT;
        location.setLatitude(x_value);
        location.setLongitude(y_value);
        this.delay=delay;
        this.delay_end_time=System.currentTimeMillis();
        this.context=app_context;
        this.in_distance=false;
        this.user_distance=99999;
        circleOptions = new CircleOptions()
            .center(new LatLng(x_value, y_value))
            .radius(radius)
            .fillColor(color)
            .strokeWidth(2)
            .clickable(visibility);
        this.view = new TextView(app_context);
        view.setText(name);
        view_in_distance=false;

    }

    public Sound()
    {    }


    /*
    Getter and Setter Methods
     */

    int getID_sound() {
        return ID_sound;
    }

    public void setID_sound(int ID_sound) {
        this.ID_sound = ID_sound;
    }

    double getX_value() {
        return x_value;
    }

    public void setX_value(double x_value) {
        this.x_value = x_value;
    }

    double getY_value() {
        return y_value;
    }

    public void setY_value(double y_value) {
        this.y_value = y_value;
    }

    double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    String getCircle_ID() {
        return circle_ID;
    }

    void setCircle_ID(String circle_ID) {
        this.circle_ID = circle_ID;
    }

    boolean getPlaying() { return playing; }

    void setPlaying(boolean playing) {
        this.playing = playing;
    }

    boolean getLoaded() {
        return loaded;
    }

    void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    void setFocused(boolean focused) {
        this.focused = focused;
    }

    int getView_ID() {
        return view_ID;
    }

    void setView_ID(int view_ID) {
        this.view_ID = view_ID;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean getControls() {
        return controls;
    }

    public void setControls(boolean controls) {
        this.controls = controls;
    }

    public double getMin_speed() {
        return min_speed;
    }

    public double getMax_speed() {
        return max_speed;
    }

    public double[][] getApproach_dir() {
        return approach_dir;
    }

    public void setApproach_dir(double[][] approach_dir) {
        this.approach_dir = approach_dir;
    }

    public Integer[][] getAND_OR() {
        return AND_OR;
    }

    public void setAND_OR(Integer[][] AND_OR) {
        this.AND_OR = AND_OR;
    }

    public Integer[] getNOT() {
        return NOT;
    }

    public void setNOT(Integer[] NOT) {
        this.NOT = NOT;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isIn_distance() {
        return in_distance;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isFocused() {
        return focused;
    }

    public Integer getTimes_played() {
        return times_played;
    }


    public String[][] getAND_OR_string() {
        return AND_OR_string;
    }

    public String[] getNOT_string() {
        return NOT_string;
    }

    public void setTimes_played(Integer times_played) {
        this.times_played = times_played;
    }

    public boolean getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(boolean is_visible) {
        this.is_visible = is_visible;
    }

    public boolean isView_in_distance() {
        return view_in_distance;
    }

    public void setView_in_distance(boolean view_in_distance) {
        this.view_in_distance = view_in_distance;
    }

    public TextView getView() {
        return view;
    }

    public boolean getCheck_AND_OR() {
        return check_AND_OR;
    }

    public void setCheck_AND_OR(boolean check_AND_OR) {
        this.check_AND_OR = check_AND_OR;
    }

    public boolean getCheck_NOT() {
        return check_NOT;
    }

    public void setCheck_NOT(boolean check_NOT) {
        this.check_NOT = check_NOT;
    }

    public double getVolume() {
        return volume;
    }

    public boolean isTurn() {
        return check_turn;
    }

    public void setTurn(boolean turn) {
        this.check_turn = turn;
    }

    public double getUser_direction() {
        return user_direction;
    }

    public void setUser_direction(double user_direction) {
        this.user_direction = user_direction;
    }
    public void calculate_user_direction(Location user_location){
        user_direction=location.bearingTo(user_location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public boolean get_played(){
        boolean played=false;
        if (times_played!=null){
            played=true;
        }
        return  played;
    }


    public void checkDelay(){

        Log.d(TAG, "checkDelay: Current time: " +System.currentTimeMillis());

        if(System.currentTimeMillis()>=delay_end_time) {
            Log.d(TAG, "checkDelay: delay time reached= "+delay_end_time+", "+name);
            checkDelay=true;
        }
        else {
            Log.d(TAG, "checkDelay: end time not reached= "+delay_end_time+", "+name);
        }
    }

    // todo! set EndTime ! TO be called when sound is finished playing!!!

    public void calculate_end_time(){
        Log.d(TAG, "calculate_end_time: delay is set to: "+delay);
        if (delay.length==1){
            // only single digit, constant delay!
            Log.d(TAG, "calculate_end_time: single digit delay");
            delay_end_time= System.currentTimeMillis()+(long)delay[0]*1000;
        }
        else if (delay.length==2){
            // use random number to calculate the time for end of delay
            Log.d(TAG, "calculate_end_time: minmax delay");
            long number = (long)delay[0]+((long)((RND.nextDouble())*(delay[1]-delay[0])));
            delay_end_time= System.currentTimeMillis()+number*1000;
            Log.d(TAG, "calculate_end_time: === "+ delay_end_time);
        }
        else {
            Log.d(TAG, "calculate_end_time: ERROR, too many values in delay_array!");
        }
    }
    public double getUser_distance(){
        return user_distance;
    }

    public void calculate_user_distance(Location user_location){
        if (user_location!=null) {
            user_distance = user_location.distanceTo(location);
        }
    }

    public MediaPlayer getMedia_player() {
        return media_player;
    }


    public void loadMedia_player(double load_radius,Context context){
        boolean load_check= false;
        Log.d("checking load", "Searching...");

        if (user_distance < load_radius) {

            Log.d("checking load", "Media player load for " + name + " true");
            load_check=true;
        }

        // if should be loaded, and player IS loaded - do nothing
        if (load_check && (media_player != null)){
                }

        // if should NOT be loaded, and player is NOT loaded - do nothing
        else if (!load_check && media_player == null){
                }

        // if should be loaded, but player is NOT loaded!  - load player, set loaded indicator
        else if (load_check && (media_player == null)) {
            Log.d("Creating Media Players", "Preparing to load "+ name);
            // run async Media player prepare!
            MediaPlayer player = MediaPlayer.create(context, context.getResources().getIdentifier(file_path,
                    "raw", context.getPackageName()));
            Log.d("Creating Media Players", "path recovered: "+file_path);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                // on completion calculate the end time for playback delay! Set playback counter!
                public void onCompletion(MediaPlayer mp) {
                    Log.d("Creating Media Players", "Playback ended: resetting end_time in sound: "+ name);
                    calculate_end_time();
                    times_played++;
                    // todo change the play/pause buttons, views, circle color
                }
            });
            Log.d("Creating Media Players", "Media player for " + name + " =SET");
            loaded=true;
        }

        // if should NOT be loaded, but player IS loaded!  - unload player, set loaded indicator
        else if (!load_check && media_player!=null ) {
            media_player.release();
            loaded=false;
            Log.d("Creating Media Players", "Media player for " + name + " REMOVED");
        }
    }

    public CircleOptions getCircleOptions() {
        return circleOptions;
    }


    public void set_volume(double vol_mod_ind){
        Log.d("Adjusting volumes", ": ");
            if (in_distance){
                double vol_mod = Math.pow(((radius-user_distance) / radius), 1 / vol_mod_ind);
                volume=(float)(1 - (Math.log(100 - (vol_mod_ind * 100)) / Math.log(100)));
                // todo! set ap biaural volume controls!!!!!!
                media_player.setVolume(volume, volume);
                }
            else {
            }
        }

    public void checkRadius(Vibrator v) {

                // newly inside radius= not yet registered as such
                if (user_distance < radius && !in_distance) {

                    in_distance=true;
                    Log.d("Searching for sounds", " New sound in distance found!");

                    //todo add onClickListener for textViews when adding the views to views_in_radius

                    //vibrate!!   Decide if should vibrate!!!
                    if (controls) {
                        Log.d("Vibrating", "Enjoy!");
                        vibrate(v);
                    }
                }

                // NOT in radius, but registered as such -> leaving the sound circle
                if (user_distance > radius && in_distance) {
                    // set inDistance==false
                    in_distance=false;
                }
    }

    public void check_speed(double user_speed){
        Log.d("Checking speeds", "check_speed: ");

            if (max_speed>user_speed && user_speed>=min_speed){
                check_speed=true;
            }
            else{
                check_speed=false;
            }
    }

    private void vibrate(Vibrator v){
        long[] pattern=new long[]{0,250,100,250,500,250,100,250};
        v.vibrate(pattern,-1);
    }


    public boolean[] check_time(Sound[] sounds){
        boolean[] time_check=new boolean[sounds.length];
        // todo Check for playing time in future versions!!!
        return time_check;
    }

}
