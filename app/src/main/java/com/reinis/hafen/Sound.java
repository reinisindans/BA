package com.reinis.hafen;

/**
 * Created by gagarin on 02.06.17.
 */

//todo SOUND DONE

import android.graphics.Color;

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
    private int visibility;
    private int controls;
    private double speed;
    private double approach_dir;
    private String[][] AND_OR;
    private String[] NOT;
    private int repeat;



/*
    Declare Constructors
     */


    Sound(int ID_sound, double x_value, double y_value, double radius, String name, String author, String description, int repeat, String file_path, int color, int visibility, int controls, double speed, double approach_dir, String[][] AND_OR, String[] NOT)
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
        this.color=color;
        this.visibility=visibility;
        this.controls=controls;
        this.speed=speed;
        this.approach_dir=approach_dir;
        this.AND_OR=AND_OR;
        this.NOT=NOT;


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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getControls() {
        return controls;
    }

    public void setControls(int controls) {
        this.controls = controls;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getApproach_dir() {
        return approach_dir;
    }

    public void setApproach_dir(double approach_dir) {
        this.approach_dir = approach_dir;
    }

    public String[][] getAND_OR() {
        return AND_OR;
    }

    public void setAND_OR(String[][] AND_OR) {
        this.AND_OR = AND_OR;
    }

    public String[] getNOT() {
        return NOT;
    }

    public void setNOT(String[] NOT) {
        this.NOT = NOT;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

}
