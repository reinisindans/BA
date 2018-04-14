package com.reinis.hafen;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
    private String dataDbName="sound.db";
    private String saveDbName="save.db";
    private GoogleMap mMap;
    private Model model;
    private DatabaseTranslator dbTranslator;
    private SQLiteDatabase database;
    private LocationManager locationManager;
    private String TAG="Main Activity";

    final private double load_radius=400;

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
    private LinearLayout bug_view;

    // Navigation Drawer!
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // needed to update SeekerBar
    private Handler mHandler = new Handler();

    // my very own
    private Vibrator vibro;

    //Permissions!!!!!
    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final int LOCATION_REQUEST = 1340;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Handling the Database
        // Creates a database Helper= database fetcher and then
        // transforms database data into application variables in database tanslator
        DatabaseHelper dbHelper= new DatabaseHelper(this);
        dbTranslator = new DatabaseTranslator(dbHelper);

        // populate the model !!!!
        model = new Model(dbTranslator.getSoundsArray(),load_radius,this);



        //////////////////////  SETTING THE ACTION BAR (Menu bar) ////////////////////////


        // todo Toolbar actions to set up: 1) Settings?, 2) RESET PROGRESS!! 3) Impressum
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Log.d(TAG, "onCreate: defining toolbar!---------------------->>"+myToolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            ActionBar aBar = getSupportActionBar();
            assert aBar != null;
            aBar.setDisplayShowTitleEnabled(false);
            aBar.setDisplayHomeAsUpEnabled(true);
            aBar.setDisplayShowHomeEnabled(true);
            aBar.setHomeButtonEnabled(true);
        }





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

        //TypedValue tva = new TypedValue();
        //int barHeight = 75;
        //if (getTheme().resolveAttribute(R.attr.actionBarSize, tva, true)) {
        //    barHeight = TypedValue.complexToDimensionPixelSize(tva.data, getResources().getDisplayMetrics());
        //}

        drawer.setPadding(0, 0, 0, 0);

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


                    for (Sound s:model.getSounds()) {

                        if (s.isFocused() && s.getControls()) {
                            if (seekBar.getProgress() > 0) {
                                s.getMedia_player().seekTo(seekBar.getProgress());
                            }

                            s.getMedia_player().start();
                            Log.d("Play icon visible", " Starting the sound " + s.getName());
                            s.setPlaying(true);

                            break;
                        }

                    }

                    //


                } else { // Unchecked - Pause icon visible

                    Log.d("Pause icon visible", "");


                    for (Sound s:model.getSounds()) {

                        if (s.getPlaying() && s.getControls() && s.isFocused()) {
                            s.getMedia_player().pause();
                            s.setManualStartStop(true);
                            Log.d("Pause icon visible", " Pausing the sound " + s.getName());
                            s.setPlaying(false);
                            s.change_circle_color();
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
                // if currently playing!
                if (model.getPlaying_with_controls() > -1) {
                    model.getSounds()[model.getPlaying_with_controls()].getMedia_player().seekTo(0);
                    model.getSounds()[model.getPlaying_with_controls()].getMedia_player().start();
                } else {
                    //not currently playing
                    for (Sound s:model.getSounds()) {

                        if (s.isFocused()) {

                            s.getMedia_player().seekTo(0);

                        }

                    }

                }

            }
        });


        //////////////        Assigning Views!    ////////////////////////////
        //main_view = (RelativeLayout) findViewById(R.id.main_view_container);  //todo Do I need this View??
        sounds_in_radius = (LinearLayout) findViewById(R.id.sounds_in_radius); // Container for the Names being played
        mediaControls = (RelativeLayout) findViewById(R.id.media_actions);   //main playback control container
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        bug_view=(LinearLayout) findViewById(R.id.playing);

        mediaControls.setVisibility(View.GONE);
        sounds_in_radius.setGravity(Gravity.CENTER);



        ///////////// setting the Vibrator
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


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

        // Alert when GPS not enabled

        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
        alertDialog.setTitle("Enable GPS");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage("App will not function withour GPS. Do you want to turn on GPS in device's settings? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MapsActivity.this.startActivity(intent);
            }
        });

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
        Log.d(TAG, "onMapReady: Action Bar size: "+mActionBarSize);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setPadding(0, 0, 0, mActionBarSize);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        /**
         * Adding Circle Graphics!
         * Initial creation of the CircleList ArrayList to store all the Circle graphics
         */

        for (Sound s:model.getSounds()) {

            Circle circle = mMap.addCircle(s.getCircleOptions());


            Log.d("Circle creation", "======== Sound name= " + s.getName());

            /**
             * Adding CircleClickListener!! See Listener defined under Methods... (although it is a Object....)
             */

            mMap.setOnCircleClickListener(circleListener);


            s.setCircle(circle);

        }

        // Setting up the preliminary views and states= passing the default user location to sound

                actOnLocation();

                Log.d(TAG,"Initial values delivered");

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
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d(TAG, "No GPS, asking for permissions ");
            alertDialog.show();
        }
        /**
         * This creates a seekerbar and starts updating it, if a view is focused...
         */
        MapsActivity.this.runOnUiThread(new Runnable() {
            // todo simplify?????
            @Override
            public void run() {
                int mCurrentPosition=0;
                int mDuration=0;
                if(model.getPlaying_with_controls() > -1){
                    mCurrentPosition = model.getSounds()[model.getPlaying_with_controls()].getMedia_player().getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);
                    Log.d("Setting the seekerbar"," Playing: "+model.getSounds()[model.getPlaying_with_controls()].getName());
                    track_position.setText(milliSecondsToTime(mCurrentPosition));
                    mDuration=model.getSounds()[model.getPlaying_with_controls()].getMedia_player().getDuration();
                    track_duration.setText(milliSecondsToTime(mDuration));
                }

                else {
                    for (Sound s:model.getSounds()) {
                        if (s.isFocused()) {
                            Log.d("Setting the seekerbar"," Focused: "+s.getName()+" Value: "+s.isFocused());



                            if (s.getMedia_player()!=null) {
                                mCurrentPosition = s.getMedia_player().getCurrentPosition();
                                mDuration=s.getMedia_player().getDuration();
                            }
                            else {
                                mCurrentPosition=0;
                                mDuration=0;
                            }
                            seekBar.setProgress(mCurrentPosition);
                            track_position.setText(milliSecondsToTime(mCurrentPosition));
                            seekBar.setMax(mDuration);
                            track_duration.setText(milliSecondsToTime(mDuration));
                        }
                    }




                }
                //adjustPlayPauseButton();
                mHandler.postDelayed(this, 250);

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
    //SeekBar Methods!
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int playing =model.getPlaying_with_controls();
        if (playing>-1 && fromUser) {

            model.getSounds()[playing].getMedia_player().seekTo(progress);
            track_position.setText(milliSecondsToTime(model.getSounds()[playing].getMedia_player().getCurrentPosition()));
        }

        else if (fromUser){ for (Sound s:model.getSounds()){


            if (s.isFocused()) {
                s.getMedia_player().seekTo(progress);
                track_position.setText(milliSecondsToTime(s.getMedia_player().getCurrentPosition()));
            }
        }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
            String name = null;
            String author = null;
            String description = null;
            Log.d("Circle Listener", "Circle ID= " );

            for (Sound s:model.getSounds()) {
                Log.d("Circle Listener Loop", "Looping...");
                Log.d("Circle Listener Loop", "---------        SoundID= " + s.getName());
                if (s.getCircle().isVisible() && circle.getId().equals(s.getCircle().getId())) {

                    name = s.getName();
                    author = s.getAuthor();
                    description = s.getDescription();
                    break;
                }
            }
                showInfo(name, author, description);
        }
    };

    /**
     * locationListener is called when the location changes. LocationListener returns the coordinates and
     */
    LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location loc) {
            if (loc != null) {

                Log.d("Position changed", "******************************************************");
                // Adjusting User class
                model.getUser().setLocation(loc);
                model.getUser().setSpeed(loc.getSpeed());
                model.getUser().setBearing(loc.getBearing());
                actOnLocation();

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

        if (item.getItemId()==R.id.reset){
            // Resetting the database
            reset();

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

    private void add_names_view(){
        sounds_in_radius.removeAllViews();
        if (model.getSoundsInDistance()!=null){
            for (TextView v:model.getSoundsInDistance()){
                sounds_in_radius.addView(v);
            }
        }
    }

    private void bug_view(){
        bug_view.removeAllViews();
        for (Sound s:model.getSounds()){
            if (s.getMedia_player()!=null){
                TextView name=new TextView(this);
                name.setTextSize(10);
                name.setText(s.getName()+" ; Visibility: "+s.isVisible()+"; Controls: "+ s.getControls()+" ; Focused: "+s.isFocused()+"; Playing: "+s.isPlaying()+" ; Times played: "+s.getTimes_played());
                bug_view.addView(name);
            }
        }
    }

private void actOnLocation(){

    //check in Model playability against already played sounds
    model.check_AND_OR();
    model.check_NOT();
    model.calculate_distances();
    model.load_players();
    model.play_sounds(vibro);

    // Checking Sound class for playability and play
    // checking Circles for visibility and color!!

    model.set_views();

    // setting the Text Views for info--1) set views, determine focus, adjust text size
    model.setSoundsInDistanceView();
    model.set_Text_View_IDs();
    model.set_focus();
    model.change_text_sizes();
    add_Names_View();
    adjustPlayPauseButton();
    check_media_controls();
    bug_view();

    }


    private void adjustPlayPauseButton(){
        // check if there is maybe a sound with controls in radius,
        // then reset playpause button to display play icon if there are no relevant sounds in distance

        boolean isPlaying=false;

        for (Sound s:model.getSounds()) {
            if (s.isIn_distance() && s.isVisible() && s.getControls() && s.isPlaying()) {
                isPlaying=true;
            }
        }

            playPauseButton.setChecked(isPlaying);
        Log.d(TAG, "adjustPlayPauseButton: Set "+isPlaying );


    }

private void reset(){
        // re- initialize the data. Clear map, and draw all the graphics again
        model=null;
        mMap.clear();
    dbTranslator = new DatabaseTranslator(new DatabaseHelper(this));

        // populate the model !!!!
        model = new Model(dbTranslator.getSoundsArray(),load_radius,this);


        /**
        * Adding Circle Graphics!
        */

        for (Sound s:model.getSounds()) {

            Circle circle = mMap.addCircle(s.getCircleOptions());


            Log.d("Circle creation", "======== Sound name= " + s.getName());

            /**
            * Adding CircleClickListener!! See Listener defined under Methods... (although it is a Object....)
            */

            mMap.setOnCircleClickListener(circleListener);


            s.setCircle(circle);

        }
        Log.d(TAG, "onOptionsItemSelected: Progress reset: ");
    }

    // saving data to save-database!!!!!

    private void save(){

    }

}
