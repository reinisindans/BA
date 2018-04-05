package com.reinis.hafen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Reinis on 01.01.18.
 */

public class DatabaseTranslator {

    private ArrayList<Sound> sounds;
    private Sound[] soundsArray;
    private Cursor dbCursor;
    private SQLiteDatabase database;

    // Constructor
    DatabaseTranslator(DatabaseHelper dbHelper, Context app_context)
    {

        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
        sounds = new ArrayList<Sound>();
        try {
            database = dbHelper.getDataBase();
            Log.d("Getting Sounds", " Database read!" + database.getPath());
            dbCursor = database.rawQuery("SELECT * FROM sound;",
                    null);

            Log.d("Cursor Indexing", "Preparing");
            dbCursor.moveToFirst();

            Log.d("Cursor Indexing", "Indexing");
            /* search through all necessary info, determine Column indexes  */
            int id_index = dbCursor.getColumnIndex("ID_sound");
            int x_value_index = dbCursor.getColumnIndex("X");
            int y_value_index = dbCursor.getColumnIndex("Y");
            int radius_index = dbCursor.getColumnIndex("Radius");
            int visibility_index= dbCursor.getColumnIndex("Visibility");
            int color_index= dbCursor.getColumnIndex("Color");
            int control_index=dbCursor.getColumnIndex("Controls");
            int min_speed_index=dbCursor.getColumnIndex("min_speed");
            int max_speed_index=dbCursor.getColumnIndex("max_speed");
            int approach_dir_index=dbCursor.getColumnIndex("Approach_dir");
            int AND_OR_index=dbCursor.getColumnIndex("AND_OR");
            int NOT_index=dbCursor.getColumnIndex("NOT");
            int repeat_index = dbCursor.getColumnIndex("Repeat");
            int delay_index = dbCursor.getColumnIndex("Delay");
            int name_index = dbCursor.getColumnIndex("Name");
            int description_index = dbCursor.getColumnIndex("Description");
            int author_index = dbCursor.getColumnIndex("Author");
            int file_path_index = dbCursor.getColumnIndex("File_path");

            Log.d("Cursor Indexing", "Indexing done");

             /* Add the Entry (Row, one Sound information) to the Database Array

             */
            while (!dbCursor.isAfterLast()) {
                Log.d("Reading Cursor", "Row " + dbCursor.getCount());
                int id = Integer.parseInt(dbCursor.getString(id_index));
                Log.d("Reading Cursor", "x_value");
                double x_value = Double.parseDouble(dbCursor.getString(x_value_index));
                Log.d("Reading Cursor", "y_value");
                double y_value = Double.parseDouble(dbCursor.getString(y_value_index));
                Log.d("Reading Cursor", "radius");
                double radius = Double.parseDouble(dbCursor.getString(radius_index));
                Log.d("Reading Cursor", "visibility");
                int vis_int=Integer.parseInt(dbCursor.getString(visibility_index));
                boolean visibility;
                if (vis_int!=1){
                    visibility=false;
                    Log.d(" visibility: ", "FALSE ");
                }
                else {
                    visibility=true;
                    Log.d(" visibility: ", "TRUE ");
                }
                Log.d("Reading Cursor", "color");
                int color=Color.parseColor(dbCursor.getString(color_index));
                Log.d("Reading Cursor", "controls");
                int cont_int=Integer.parseInt(dbCursor.getString(control_index));
                boolean controls;
                if (cont_int!=1){
                    controls=false;
                    Log.d(" controls: ", "FALSE ");
                }
                else {
                    controls=true;
                    Log.d(" controls: ", "TRUE ");
                }

                Log.d("Reading Cursor", "min_speed");
                double min_speed=Double.parseDouble(dbCursor.getString(min_speed_index));
                Log.d("Reading Cursor", "max_speed");
                double max_speed=Double.parseDouble(dbCursor.getString(max_speed_index));

                Log.d("Reading Cursor", "approach direction");
                String[] approach_first_order=dbCursor.getString(approach_dir_index).split(";");
                String[][] approach_second_order=new String[approach_first_order.length][2];
                for (int i = 0; i < approach_first_order.length; i++) {
                    String[] directions_array=approach_first_order[i].split(",");
                    approach_second_order[i]=directions_array;
                }

                double[][] approach_dir=new double[approach_second_order.length][2];
                for (int i=0;i<approach_second_order.length;i++){
                    for (int k=0; k<approach_second_order[i].length;k++){
                        approach_dir[i][k]=Double.parseDouble(approach_second_order[i][k]);
                    }

                }

                Log.d("Reading Cursor", "AND/OR");
                String[] array_or= dbCursor.getString(AND_OR_index).split(";");
                Log.d("Reading Cursor", "AND/OR, creating arrays");
                String [][] AND_OR = new String[array_or.length][];
                for (int i = 0; i < array_or.length; i++) {
                    String[] array_and=array_or[i].split(",");
                    Log.d("Reading Cursor", "AND/OR, adding an AND array");
                    AND_OR[i]=array_and;
                }

                Log.d("Reading Cursor", "NOT");
                String[] NOT=dbCursor.getString(NOT_index).split(",");
                Log.d("Reading Cursor", "repeat");
                int repeat = Integer.parseInt(dbCursor.getString(repeat_index));

                Log.d("Reading Cursor", "delay");
                String[] delay_str=dbCursor.getString(delay_index).split(",");
                double[] delay=new double[delay_str.length];
                for (int i=0; i < delay_str.length ;i++){
                    delay[i]=Double.parseDouble(delay_str[i]);
                }



                Log.d("Reading Cursor", "name");
                String name = dbCursor.getString(name_index);
                Log.d("Reading Cursor", "description");
                String description = dbCursor.getString(description_index);
                Log.d("Reading Cursor", "author");
                String author = dbCursor.getString(author_index);
                Log.d("Reading Cursor", "File_path");
                String file_path = dbCursor.getString(file_path_index);

                /*
                Create the Sound instance!
                 */

                Sound sound = new Sound(app_context,id, x_value, y_value, radius, name, author, description, repeat, delay, file_path, color, visibility, controls, min_speed, max_speed, approach_dir, AND_OR, NOT);

                /*
                Add to the List af all sounds in the Database!!
                 */
                sounds.add(sound);
                dbCursor.moveToNext();

            }
        } finally {
            if (database != null) {
                dbHelper.close();
                dbCursor.close();
            }
        }



    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }


    public Sound[] getSoundsArray(){
        soundsArray=new Sound[sounds.size()];
        for (int i=0;i>this.sounds.size();i++){
            soundsArray[i]=sounds.get(i);
        }


        return soundsArray;
    }
}


