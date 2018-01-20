package com.reinis.hafen;

/**
 * Created by gagarin on 02.06.17.
 */

//todo SOUND DONE

import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;


class Sound implements Serializable {
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
    private boolean playing;
    private boolean in_distance;
    private boolean loaded;
    private boolean focused;
    private int view_ID;
    private int color;
    private boolean visibility;
    private boolean controls;
    private double min_speed;
    private double max_speed;
    private double[][] approach_dir;
    private double user_direction;
    private Integer[][] AND_OR;
    private Integer[] NOT;
    private String[][] AND_OR_string;
    private String[] NOT_string;
    private int repeat;
    private Integer times_played;
    private boolean is_visible;
    private boolean check_AND_OR;
    private boolean check_NOT;
    private double volume;

//todo export all the boolean values except visibility,controls, to Model boolean[] checks.

/*
    Declare Constructors
     */


    Sound(int ID_sound, double x_value, double y_value, double radius, String name, String author, String description, int repeat, String file_path, int color, int visibility, int controls, double min_speed,double max_speed, double[][] approach_dir, String[][] AND_OR, String[] NOT)
    {
        this.ID_sound=ID_sound;
        this.x_value=x_value;
        this.y_value=y_value;
        this.radius=radius;
        this.name=name;
        this.author=author;
        this.description=description;
        this.repeat=repeat;
        this.file_path=file_path;
        this.playing=false;
        this.color = color ;
        initiateVisibility(visibility);
        initiateControls(controls);
        this.min_speed=min_speed;
        this.max_speed=max_speed;
        this.approach_dir=approach_dir;
        this.AND_OR_string=AND_OR;
        this.NOT_string=NOT;

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

    boolean getIn_distance() {
        return in_distance;
    }

    void setIn_distance(boolean in_distance) {
        this.in_distance = in_distance;
    }

    boolean getLoaded() {
        return loaded;
    }

    void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    boolean getFocused() {
        return focused;
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
    public boolean get_played(){
        boolean played=false;
        if (times_played!=null){
            played=true;
        }
        return  played;
    }

    private void initiateVisibility(int vis){
        Log.d("initiating visibility; ", "start: " +this.getName());
        boolean visibility;
        if (vis!=1){
            this.visibility=false;
            Log.d(" visibility: ", "FALSE ");
        }
        else {
            this.visibility=true;
            Log.d(" visibility: ", "TRUE ");
        }
    }

    private void initiateControls(int contr){
        Log.d("initiating controls; ", "start: " +this.getName());
        boolean visibility;
        if (contr!=1){
            this.visibility=false;
            Log.d(" controls: ", "FALSE ");
        }
        else {
            this.visibility=true;
            Log.d(" controls: ", "TRUE ");
        }
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

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getUser_direction() {
        return user_direction;
    }

    public void setUser_direction(double user_direction) {
        this.user_direction = user_direction;
    }
}
