package com.reinis.hafen;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.reinis.hafen.R.id.left_drawer;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    private GoogleMap mMap;
    private Model model;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private DatabaseTranslator dbTranslator;
    private SQLiteDatabase database;
    private LocationManager locationManager;



    //views
    private ToggleButton playPauseButton;
    private ImageButton repeatButton;
    private SeekBar seekBar;
    private RelativeLayout mediaControls;
    private ListView drawer;
    private RelativeLayout main_view;
    private LinearLayout sounds_in_radius;
    private TextView track_position;
    private TextView track_duration;

    // Navigation Drawer!
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // needed to update SeekerBar
    private Handler mHandler = new Handler();

    // my very own
    private Vibrator v;

    //Permissions!!!!!
    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final int LOCATION_REQUEST = 1340;

    // Controller with main methods!
    Controller controller=new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Handling the Database

        dbTranslator = new DatabaseTranslator(dbHelper, this);

        // populate the model !!!!
        model = new Model(dbTranslator.getSoundsArray());



        //////////////////////  SETTING THE ACTION BAR (Menu bar) ////////////////////////


        // todo Toolbar actions to set up: 1) Settings?, 2) RESET PROGRESS!! 3) Impressum
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar aBar = getSupportActionBar();
        assert aBar != null;
        aBar.setDisplayShowTitleEnabled(false);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setDisplayShowHomeEnabled(true);
        aBar.setHomeButtonEnabled(true);


        ///Populating the ListView of Drawer Items (List of Tracks)

        drawer = (ListView) findViewById(left_drawer);

        // getting sounds name list!

        ArrayList<String> drawer_names = new ArrayList<String>();
        for (int i = 0; i < model.getSounds().length; i++) {
            drawer_names.add(model.getSounds()[i].getName());
            Log.d("Getting names List", "Adding " + model.getSounds()[i].getName());
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                drawer_names);

        Log.d("G ", " Setting Adapter");

        drawer.setAdapter(arrayAdapter);
        Log.d("G ", "Adapter SET");
        // setting Padding

        TypedValue tva = new TypedValue();
        int barHeight = 75;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tva, true)) {
            barHeight = TypedValue.complexToDimensionPixelSize(tva.data, getResources().getDisplayMetrics());
        }

        drawer.setPadding(0, barHeight + 3, 0, 0);

        // todo Enabling scrolling
        drawer.setVerticalScrollBarEnabled(true);


        ///// Setting the ONCLICK for list items!

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                LatLng loc = new LatLng(model.getSounds()[position].getX_value(), model.getSounds()[position].getY_value());
                Log.d("List click", "Zooming to" + model.getSounds()[position].getName());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));


                // todo Adding the Animation to the sound Circle after centering to it- flash or change color
                //circleList.get(position).setFillColor(ContextCompat.getColor(MapsActivity.this,R.color.circle_fill));
                //String circleID= sounds.get(selectedPosition).getCircle_ID();

            }
        });


        //// SETTING THE DRAWER LAYOUT!!

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                myToolbar,
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        //todo check why setDrawerListener is deprecated
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        //After instantiating your ActionBarDrawerToggle
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.sound_list, this.getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        /////////////// ADD MEDIA CONTROLLERS
        /**
         * Adding the Media Controller views:
         */
        track_position = (TextView) findViewById(R.id.time_position);
        track_duration = (TextView) findViewById(R.id.duration);

        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on clicks

                if (playPauseButton.isChecked()) { // Checked - Play icon visible


                    for (int i = 0; i < model.getSounds().length; i++) {

                        if (model.getSounds()[i].getFocused()) {
                            if (seekBar.getProgress() > 0) {
                                model.getMedia_player(i).seekTo(seekBar.getProgress());
                            }

                            model.getMedia_player(i).start();
                            Log.d("Play icon visible", " Starting the sound " + model.getSounds()[i].getName());
                            model.getSounds()[i].setPlaying(true);
                            if (model.getCircleList() != null) {
                                change_circle_color();
                            }
                            break;
                        }

                    }

                    //


                } else { // Unchecked - Pause icon visible

                    Log.d("Pause icon visible", "");


                    for (int i = 0; i < model.getSounds().length; i++) {

                        if (model.getSounds()[i].getPlaying()) {
                            model.getMedia_player(i).pause();
                            Log.d("Pause icon visible", " Pausing the sound " + model.getSounds()[i].getName());
                            model.getSounds()[i].setPlaying(false);
                            change_circle_color();
                            break;
                        }

                    }

                }
            }
        });


        // Setting the repeat button functionality
        repeatButton = (ImageButton) findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Rewind the currently playing sound to start, start again
                 */
                if (model.getPlaying() > -1) {
                    model.getMedia_player(model.getPlaying()).seekTo(0);
                    model.getMedia_player(model.getPlaying()).start();
                } else {
                    for (int i = 0; i < model.getSounds().length; i++) {

                        if (model.getSounds()[i].getFocused()) {

                            model.getMedia_player(i).seekTo(0);

                        }

                    }

                }

            }
        });


        //////////////        Assigning Views!    ////////////////////////////
        //main_view = (RelativeLayout) findViewById(R.id.main_view_container);  //todo Do I need this View??
        sounds_in_radius = (LinearLayout) findViewById(R.id.sounds_in_radius); // Container for the Names eing played
        mediaControls = (RelativeLayout) findViewById(R.id.media_actions);   //main playback control container
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        mediaControls.setVisibility(View.GONE);
        sounds_in_radius.setGravity(Gravity.CENTER);

        ///////////// setting the Vibrator
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        //todo enentually correct the 'screen rotation' problem: implement 'savedInstanceState' correctly
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //todo done based on this: https://stackoverflow.com/questions/35771531/call-requires-api-level-23-current-min-is-14-android-app-activityrequestperm
        //todo maybe remove the permissions at all? App will be handled like old-school SDK?
        //Checking the permissions!
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, LOCATION_PERMS, LOCATION_REQUEST);
        }


        mMap = googleMap;

        /**
         * Estimating the ActionBar size, to set the right Map Padding
         */
        int mActionBarSize;
        final TypedArray styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setPadding(0, 0, 0, mActionBarSize);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        /**
         * Adding Circle Graphics!
         * Initial creation of the CircleList ArrayList to store all the Circle graphics
         */


        for (int i = 0; i < model.getSounds().length; i++) {

            Circle circle = mMap.addCircle(model.getSounds()[i].getCircleOptions());

            final String circleID = circle.getId();
            model.getSounds()[i].setCircle_ID(circleID);

            Log.d("Circle creation", "SoundID= " + model.getSounds()[i].getID_sound() + "======== Sound name= " + model.getSounds()[i].getName() + "  ======================SoundCircleID= " + model.getSounds()[i].getCircle_ID());

            /**
             * Adding CircleClickListener!! See Listener defined under Methods... (although it is a Object....)
             */

            mMap.setOnCircleClickListener(circleListener);

            model.getCircleList().add(circle);

        }



        if (model.getUser().getLocation()!=null) {
            double lat =  model.getUser().getLocation().getLatitude();
            double lng = model.getUser().getLocation().getLongitude();
            LatLng coordinate = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));

        }

        /**
         * Setting up the location manager. It is responsible for getting the user position!
         */

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        /**
         * This creates a seekerbar and starts updating it, if a view is focused...
         */
        MapsActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(model.getPlaying_with_controls() > -1){
                    int mCurrentPosition = model.getMedia_player(model.getPlaying_with_controls()).getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);
                    Log.d("Setting the seekerbar"," Playing: "+model.getSounds()[model.getPlaying_with_controls()].getName());
                    track_position.setText(milliSecondsToTime(mCurrentPosition));
                }

                else {

                    for (int i=0;i<model.getSounds().length;i++) {
                        if (model.getSounds()[i].getFocused()) {
                            Log.d("Setting the seekerbar"," Focused: "+model.getSounds()[i].getName());

                            int mCurrentPosition;

                            if (model.getMedia_player(i)!=null) {
                                mCurrentPosition = model.getMedia_player(i).getCurrentPosition();
                            }
                            else {
                                mCurrentPosition=0;
                            }
                            seekBar.setProgress(mCurrentPosition);
                            track_position.setText(milliSecondsToTime(mCurrentPosition));
                        }
                    }

                }

                mHandler.postDelayed(this, 500);
            }
        });

    }


    //todo Decide if and how the sound should be played/paused. Display/hide the playback controls: play_sound()

    // todo Method to show/hide/rewind the seekbar!
    /**
     * Methods to control and adjust the Seeker bar- replay progress bar
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // todo adjust_visibility() = check if should be displayed at all+ change_circle_color()

    private void change_circle_color() {
        if (model.getCircleList() != null) {
            Log.d("Changing circle color", "");
            // setting all circle colors
            //todo change the colors dinamically- based on the base color of each circle!!
            for (int i = 0; i < model.getSounds().length; i++) {
                if (model.getSounds()[i].getPlaying()) {
                    // Playing
                    model.getCircleList().get(i).setFillColor(model.getSounds()[i].getColor() + 1000);
                    Log.d("Changing circle color", "Found circle playing, changing color: " + model.getSounds()[i].getName());
                } else if (model.getSounds()[i].getIn_distance() && !model.getSounds()[i].getPlaying()) {
                    // ir Radius, but not Playing
                    model.getCircleList().get(i).setFillColor(model.getSounds()[i].getColor() - 1000);
                    Log.d("Changing circle color", "found circle in Radius, changing color: " + model.getSounds()[i].getName());
                } else {
                    // No change, use default color!!
                    model.getCircleList().get(i).setFillColor(model.getSounds()[i].getColor());
                    Log.d("Changing circle color", "Reseting the rest of circle colors");

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //todo   Toolbar actions to set up: 1) Settings?, 2) RESET PROGRESS!! 3) Impressum  What to do with 'settings'--- to develop for later versions of app
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    GoogleMap.OnCircleClickListener circleListener = new GoogleMap.OnCircleClickListener() {
        @Override
        public void onCircleClick(Circle circle) {
            String circleID = circle.getId();
            String name = null;
            String author = null;
            String description = null;
            Log.d("Circle Listener", "Circle ID= " + circleID);

            for (int i = 0; i < model.getSounds().length; i++) {
                Log.d("Circle Listener Loop", "Looping..." + i);
                if (model.getSounds()[i].getCircle_ID().equals(circleID)) {

                    Log.d("Circle Listener Loop", "Circle ID= " + circleID + "---------        SoundID= " + model.getSounds()[i].getName());

                    name = model.getSounds()[i].getName();
                    author = model.getSounds()[i].getAuthor();
                    description = model.getSounds()[i].getDescription();


                }
            }

            showInfo(name, author, description);

        }
    };

    /**
     * locationListener is called when the location changes. LocationListener returns the coordinates and creates Controller to run functions
     */
    LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location loc) {
            if (loc != null) {

                Log.d("Position changed", "******************************************************");
                model.getUser().setLocation(loc);
                model.getUser().setSpeed(loc.getSpeed());
                model.getUser().setBearing(loc.getBearing());


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    /**
     * Handle the Side Drawer!!!
     * @param savedInstanceState
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // todo When settings implemented, add on ItemSelected action?
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    /**
     * Method to add the text name_views to the User Interface= to sounds_in_radius Listview!!
     */
    private void add_Names_View() {
        if (sounds_in_radius!=null) {
            sounds_in_radius.removeAllViews();
        }

        if (model.getSoundsInDistance() != null) {
            for (int i = 0; i < model.getSoundsInDistance().size(); i++) {

                TextView view = model.getSoundsInDistance().get(i);
                view.setGravity(Gravity.CENTER);
                final String name= model.getSounds()[i].getName();
                final String author= model.getSounds()[i].getAuthor();
                final String description= model.getSounds()[i].getDescription();


                view.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        showInfo(name,author,description);
                    }
                });

                sounds_in_radius.addView(view);
                Log.d("Adding View",""+view.getText());
            }
        }
    }
    private void check_media_controls(){

        Log.d("Checking controls","visibility");

        if (model.getSoundsInDistance().size()>0){
            mediaControls.setVisibility(View.VISIBLE);
            Log.d("Control visibility"," Visible");
        }
        else {
            mediaControls.setVisibility(View.GONE);
            Log.d("Control visibility"," GONE");
        }
    }


    public String milliSecondsToTime(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Setting the information View...a View in the same Activity!
     * @param name
     * @param author
     * @param description
     */
    public void showInfo(String name, String author, String description) {

        Log.d("Setting Layouts", "Relative Layout");

        final RelativeLayout fake_layout = new RelativeLayout(MapsActivity.this);
        final ScrollView scrollView = new ScrollView(MapsActivity.this);
        RelativeLayout.LayoutParams layout_fake_layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //RelativeLayout.LayoutParams scroll_layout= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        fake_layout.setLayoutParams(layout_fake_layout);
        fake_layout.setBackgroundColor(getResources().getColor(R.color.gray));
        fake_layout.setClickable(true);

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 75;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        //fake_layout.setPadding(0,actionBarHeight,0,0);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_view_container);
        relativeLayout.addView(fake_layout);
        Log.d("Setting Layouts", "Fake Layout added..");

        fake_layout.addView(scrollView);

        Log.d("Setting Layouts", "Scroll View added..");

        LinearLayout text = new LinearLayout(MapsActivity.this);
        text.setOrientation(LinearLayout.VERTICAL);
        text.setVerticalScrollBarEnabled(true);


        final TextView expanded_description = new TextView(MapsActivity.this);
        final ImageButton close = new ImageButton(MapsActivity.this);
        final TextView name_view2 = new TextView(MapsActivity.this);
        final TextView author_view2 = new TextView(MapsActivity.this);

        // Setting Layouts
        RelativeLayout.LayoutParams layout_name = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_author = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_button = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //adding extra Rules to position the Layouts for Text, Name, Author and Close Button
        layout_button.addRule(RelativeLayout.ALIGN_PARENT_END);
        layout_button.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        layout_name.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout_name.addRule(RelativeLayout.CENTER_HORIZONTAL);


        layout_author.addRule(RelativeLayout.BELOW, name_view2.getId());
        layout_author.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout_description.addRule(RelativeLayout.CENTER_VERTICAL);


        //Set text properties!
        expanded_description.setText(description);
        expanded_description.setBackgroundColor(getResources().getColor(R.color.gray));
        name_view2.setText(name);
        author_view2.setText(getString(R.string.author, author) + "\n");


        close.setBackgroundResource(R.drawable.close);
        close.setClickable(true);


        // adding OnClick! Close Description view and Button, set Table view Buttons clickable
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((ViewGroup) fake_layout.getParent()).removeView(fake_layout);

            }
        });

        Log.d("Setting Layouts", "addin views to text view");
        text.addView(name_view2, layout_name);
        text.addView(author_view2, layout_author);
        text.addView(expanded_description, layout_description);
        Log.d("Setting Layouts", "adding text to scroll view");
        scrollView.addView(text);
        Log.d("Setting Layouts", "adding close button to fake view");
        fake_layout.addView(close, layout_button);

    }


}
