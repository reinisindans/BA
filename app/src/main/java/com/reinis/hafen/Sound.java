package com.reinis.hafen;

/**
 * Created by gagarin on 02.06.17.
 */

//todo SOUND DONE

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private TextView view;
    private double[][] time_range;
    private boolean biaural;
    private double vol_mod_ind;
    private double load_radius;



    // STATES
    private Integer times_played;
    private float[] volume= new float[2];
    private Location location=new Location(LocationManager.GPS_PROVIDER);
    private boolean loaded;
    private boolean focused;
    private double userToSoundDirection;
    private double soundToUserDirection;
    private double user_distance;
    private double user_speed;
    private double user_bearing;
    private long delay_end_time;
    private MediaPlayer media_player;
    private CircleOptions circleOptions;
    private Circle circle;
    private boolean view_in_distance;
    private Context context;
    private boolean manualStartStop;
    private boolean playbackStarted;
    private int playerPosition;
    private boolean playing;
    private boolean in_distance;
    private boolean is_visible;


    //CHECKS!
    private boolean check_AND_OR;
    private boolean check_NOT;
    private boolean checkDelay;
    private boolean check_turn;
    private boolean check_speed;
    private boolean check_repeat;
    private boolean check_time;
    private boolean check_direction;
    private boolean check_play;
    private boolean check_radius;




/*
    Declare Constructors
     */


    Sound(Context app_context, int ID_sound, double x_value, double y_value, double radius, String name, String author, String description, int repeat, double[] delay, String file_path, int color, boolean visibility, boolean controls, double min_speed,double max_speed, double[][] approach_dir,double [][] time_range, String[][] AND_OR, String[] NOT, double volMod, boolean biaural, double load_radius)
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
        this.times_played=0;
        // default opacity= 5%
        this.color =(color & 0x00FFFFFF) | (0x0D << 24); ;
        this.visibility=visibility;
        this.is_visible=false;
        this.controls=controls;
        this.min_speed=min_speed;
        this.max_speed=max_speed;
        this.approach_dir=approach_dir;
        this.time_range=time_range;
        this.AND_OR_string=AND_OR;
        this.NOT_string=NOT;
        this.vol_mod_ind=volMod;
        this.biaural=biaural;
        this.load_radius=load_radius;
        location.setLatitude(x_value);
        location.setLongitude(y_value);
        this.delay=delay;
        this.delay_end_time=System.currentTimeMillis();
        this.context=app_context;
        this.in_distance=false;
        this.user_distance=99999;
        this.soundToUserDirection=0;
        this.userToSoundDirection=0;
        circleOptions = new CircleOptions()
            .center(new LatLng(x_value, y_value))
            .radius(radius)
            .fillColor(color)
            .strokeWidth(2)
            .clickable(visibility)
            .visible(false);
        this.view = new TextView(app_context);
        view.setText(name);
        view_in_distance=false;
        this.manualStartStop=false;
        this.playbackStarted=false;

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

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setManualStartStop(boolean manualStartStop){
        this.manualStartStop=manualStartStop;
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

    public boolean isVisible() {
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

    public boolean isCheck_turn() {
        return check_turn;
    }

    public void calculate_user_direction(Location user_location){
        soundToUserDirection=location.bearingTo(user_location);
        userToSoundDirection=user_location.bearingTo(location);
        if (user_location.getBearing()!=0) {
            user_bearing = user_location.getBearing();
        }
        if (soundToUserDirection<0){
            soundToUserDirection=360+soundToUserDirection;
        }
        if (userToSoundDirection<0){
            userToSoundDirection=360+userToSoundDirection;
        }

    }

    public long getDelay_end_time() {
        return delay_end_time;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public boolean isManualStartStop() {
        return manualStartStop;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setUser_speed(double user_speed) {
        this.user_speed = user_speed;
    }

    public void setUser_bearing(double user_bearing) {
        this.user_bearing = user_bearing;
    }

    public boolean get_played(){
        if (media_player!=null && media_player.isPlaying()){
            playbackStarted=true;
        }
        return playbackStarted;

    }


    public void checkDelay(){

        //Log.d(TAG, "checkDelay: Current time: " +System.currentTimeMillis());
        checkDelay=false;
        if(System.currentTimeMillis()>=delay_end_time) {
            //Log.d(TAG, "checkDelay: delay time reached= "+delay_end_time+", "+name);
            checkDelay=true;
        }
        else {
            //Log.d(TAG, "checkDelay: end time not reached= "+delay_end_time+", "+name);
        }
        Log.d(TAG, "checkDelay: "+name+ "Delay: "+checkDelay);
    }

    public void calculate_end_time(){
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
            Log.d(TAG, "calculate_end_time: Dynamic delay=== "+ ((delay_end_time-System.currentTimeMillis())/1000));

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
            Log.d(TAG, "calculate_user_distance: "+name+ "Distance== "+user_distance);
        }
    }

    public MediaPlayer getMedia_player() {
        return media_player;
    }


    public void loadMedia_player(Context context){
        boolean load_check= false;
        Log.d("checking load", "Searching...");

        if (user_distance < load_radius) {

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
            media_player = MediaPlayer.create(context, context.getResources().getIdentifier(file_path,
                    "raw", context.getPackageName()));
            //if this is a repeated load, rewind to last known location
            if (playerPosition!=0){
                media_player.seekTo(playerPosition);
            }
            if (repeat==0) {
                media_player.setLooping(false); // <------- if looping set to 'true', Oncompletionlistener is never called!
            }
            media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                // on completion calculate the end time for playback delay! Set playback counter!
                public void onCompletion(MediaPlayer mp) {
                    Log.d("Creating Media Players", "Playback ended: resetting end_time in sound: " + name);
                    calculate_end_time();
                    times_played++;
                    // rwind the player!
                    media_player.seekTo(0);
                    // check if the delay is 0 ---> sound should loop. IF 0 ---> player.start()
                    if (delay.length==1 && delay[0]==0){
                        media_player.start();
                    }

                    // todo change the play/pause buttons, views, circle color
                }
            });
            Log.d("Creating Media Players", "Media player for " + name + " =SET");
            loaded=true;
        }

        // if should NOT be loaded, but player IS loaded!  - unload player, set loaded indicator
        else if (!load_check && media_player!=null ) {
            media_player.release();
            media_player=null;
            loaded=false;
            Log.d("Creating Media Players", "Media player for " + name + " REMOVED");
        }
    }

    public CircleOptions getCircleOptions() {
        return circleOptions;
    }


    public void set_volume(){
        // volume based on distance
        double vol_mod = Math.pow(((radius - user_distance) / radius), 1 / vol_mod_ind);
        volume[0] = (float) (1 - (Math.log(100 - (vol_mod * 100)) / Math.log(100)));
        volume[1]=volume[0];

        if (biaural && user_bearing!=0){
            // Volume based on user bearing
            // Correcting user bearing
            if (user_bearing<0){
                user_bearing=360+user_bearing;
            }
            // calculate the relative bearing to sound node
            double relativeBearing=userToSoundDirection-user_bearing;
            Log.d(TAG, "set_volume for: "+name+" User bearing: "+user_bearing+ " ; User to Sound direction "+userToSoundDirection+ " ; Relative direction: "+relativeBearing);
            // will be greater than 0 when 0°->180° -> sound from Right! Decrease the volume on left speaker
                if (relativeBearing>0){
                    volume[0]=volume[0]- (float)(Math.sin(Math.toRadians(relativeBearing))*volume[0]*0.6);
                    Log.d(TAG, "set_volume: LEFT volume adjusted: "+volume[0]);
                }

                //will be smaller than 0, when relative direction 180°-360°
            if (relativeBearing<0){
                volume[1]=volume[1]+ (float)(Math.sin(Math.toRadians(relativeBearing))*volume[1]*0.6);
                Log.d(TAG, "set_volume: RIGHT volume adjusted: "+volume[1]);
                }
        }

        if (media_player!=null) {
            media_player.setVolume(volume[0], volume[1]);
            Log.d("Adjusting volumes", ": FOR: " + name + " Volume: " + volume);

        }
    }

    public void check_Radius(Vibrator v) {

                // newly inside radius= not yet registered as such
                if (user_distance < radius && !in_distance) {

                    in_distance=true;
                    Log.d("Searching for sounds", " New sound in distance found!");

                    //todo add onClickListener for textViews when adding the views to views_in_radius

                    //vibrate!!   Decide if should vibrate!!!
                    if (controls && is_visible) {
                        Log.d("Vibrating", "Enjoy!");
                        vibrate(v);
                    }
                }

                // NOT in radius, but registered as such -> leaving the sound circle
                if (user_distance > radius && in_distance) {
                    // set inDistance==false
                    in_distance=false;
                }
        Log.d(TAG, "check_Radius: "+name+" In Distance= "+in_distance);
    }

    public void check_speed(double user_speed){
            if ((max_speed>user_speed && user_speed>=min_speed) || playbackStarted){
                check_speed=true;
            }
            else {
                check_speed=false;
            }
        Log.d(TAG, "check_speed: "+name+"--------------->"+check_speed);
    }

    private void vibrate(Vibrator v){
        long[] pattern=new long[]{0,250,100,250,500,250,100,250};
        v.vibrate(pattern,-1);
    }


    public void check_time(){
        // todo Check for playing time in future versions!!!
        Calendar cal=Calendar.getInstance();
        double hour=cal.get(Calendar.HOUR_OF_DAY);
        double minute=cal.get(Calendar.MINUTE);
        double now=hour+(minute/60);
        check_time=false;
        for (double[] d:time_range) {
            if (d[1]<d[0]){
                now=now+24;
                d[1]=d[1]+24;
            }
            if (d[0] <= now && now <= d[1]) {
                check_time = true;
                Log.d(TAG, "check_time: "+name+", ---------------->" +check_time);
            }
        }
    }

    public void check_repeat(){
            if (repeat==0){
                // 0 === loop indefinitely
                check_repeat=true;
            }
            else if (repeat-times_played==0){
                check_repeat=false;
            }
            else {
                check_repeat=true;
            }
        Log.d(TAG, "check repeat :"+name+". Result : "+check_repeat+". Times played= "+times_played);
    }

    public void check_direction(){
          // check against user direction
            check_direction=false;
        Log.d(TAG, "check_direction: Direction= " +soundToUserDirection);
        Log.d(TAG,"check_direction: approach_dir: "+approach_dir[0][0]+" and "+approach_dir[0][1]);


            for (double[] d:approach_dir){
                double utility_user_direction=soundToUserDirection;
                //if the second directional parameter is smaller than first= +360 (2*PI)
                if (d[1]<=d[0]){

                    if (utility_user_direction<d[0]){
                        //adding 360 also to User direction, if it is between 0 and smallest number
                        // also checking if user direction has already been adjusted
                        utility_user_direction=utility_user_direction+360;
                        }
                    d[1]=d[1]+360;
                    }

                     if ((utility_user_direction>d[0] && utility_user_direction < d[1]) || playbackStarted){
                        check_direction=true;
                     }

                }

        Log.d(TAG, "check_direction: "+name+"----------------->"+check_direction);
    }

    public void check_turn(){
        // Check if it is the play-turn for the sounds
                // There are items in NOT array and at least one of them has been played!
                check_turn=false;

                if (check_NOT&&check_AND_OR){
                    check_turn=true;
                }

        Log.d(TAG, "check_turn: "+name+"-------------------> "+check_turn);
    }

    public void check_play(double user_speed, Location user_location, Vibrator v){
        Log.d(TAG, "check_play: Starting check stack: " +name);
        this.user_speed=user_speed;
        check_turn();
        checkDelay();
        check_speed(user_speed);
        calculate_user_direction(user_location);
        check_direction();
        check_repeat();
        check_time();
        check_Radius(v);
        //loadMedia_player(load_radius,context);   -----<<<<< Maybe in separate call....

        check_play=check_AND_OR && check_NOT && checkDelay && check_speed && check_direction && check_repeat &&check_time && in_distance;
        Log.d(TAG, "check_play: RESULT!!!!!!!!!!!!!!---------->  "+check_play+"\n\n");
    }

    // check if the sound circle should be visible!!!!!!!!
    public void check_visibility(){
            // NO visibility by default!
            if (!visibility){
                is_visible=false;
            }
            // should be visible if the preconditions about previously played tracks are met
            else {
                is_visible=check_turn;

            }
        Log.d("check_visibility()", "depends on check_turn()--------->  "+is_visible);
    }
    public void apply_visibility(){
        if (is_visible){
            circle.setClickable(true);
            circle.setVisible(true);
        }
        else {
            circle.setClickable(false);
            circle.setVisible(false);
        }
    }
    public void change_circle_color() {
        if (circle != null) {
            // setting all circle colors
            //todo change the colors dinamically- based on the base color of each circle!!

                if (playing) {
                    // Playing, opacity= 50%
                    int play_color=(color & 0x00FFFFFF) | (0x80 << 24);;
                    circle.setFillColor(play_color);
                    Log.d("Changing circle color", "Found circle playing, changing color: " + name);
                } else if (in_distance && !playing) {
                    // ir Radius, but not Playing, opacity= 20%
                    int radius_color=(color & 0x00FFFFFF) | (0x33 << 24);
                    circle.setFillColor(radius_color);
                    Log.d("Changing circle color", "found circle in Radius, changing color: " + name);
                } else {
                    // No change, use default color!!
                    circle.setFillColor(color);
                    Log.d("Changing circle color", "Normal color!" + name);

                }
        }
    }
    public void play() {

        // Should be playing, but is not
        if (check_play && !playing && media_player!=null && !manualStartStop){
            media_player.start();
            playing=true;
            playbackStarted=true;
            playerPosition=media_player.getCurrentPosition();
            Log.d(TAG, "play: Starting playback!:"+name+ "------------------------->       " +playing);
        }
        // Should not be playing but is playing
        else if(!check_play && playing && media_player!=null){
            media_player.pause();
            playing=false;
            playerPosition=media_player.getCurrentPosition();
            //
            Log.d(TAG, "play: Stopping playback!:"+name+ "------------------------->       " +playing);
                    }

                // all other cases not interesting
    }

}

