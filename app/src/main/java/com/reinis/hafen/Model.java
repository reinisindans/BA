package com.reinis.hafen;

import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;

/**
 * Created by root on 01.01.18.
 */

public class Model {

    private ArrayList<Sound> sounds;
    private ArrayList<Circle> circleList = new ArrayList<>();

    // Constructors
    Model(ArrayList<Sound> sounds,ArrayList<Circle> circleList ){
        this.sounds=sounds;
        this.circleList=circleList;
    }
    Model(){

    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(ArrayList<Circle> circleList) {
        this.circleList = circleList;
    }
}
