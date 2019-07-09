package com.wahanaartha.survey.responden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.wahanaartha.survey.LoginActivity;
import com.wahanaartha.survey.R;
import com.wahanaartha.survey.SplashScreenActivity;
import com.wahanaartha.survey.admin.AdminActivity;
import com.wahanaartha.survey.model.API;
import com.wahanaartha.survey.model.LoginUser;
import com.wahanaartha.survey.other.CircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF;
import static com.wahanaartha.survey.LoginActivity.MY_LOGIN_PREF_KEY;

public class RespondenActivity extends AppCompatActivity {

    public NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtGroup;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    String username,password;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://www.static-src.com/wcsstore/Indraprastha/images/catalog/medium//1473/honda_honda-all-new-beat-esp-fi-sporty-cw-sepeda-motor---dance-white--otr-jadetabek-_full02.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_INDEX_SURVEY = "index";
    private static final String TAG_HISTORY_SURVEY= "history";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private boolean searchActive = false;
    @BindView(R.id.search_edittext) EditText searchEdittext;
    @BindView(R.id.search_button) ImageButton searchButton;
    @BindView(android.R.id.title) TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responden_activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtGroup = (TextView) navHeader.findViewById(R.id.group);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles_responden);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        title.setText("Home Survey");
        title.setTextColor(Color.WHITE);
        searchButton.setVisibility(View.GONE);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website
     */
    private void loadNavHeader() {

        // name, website
        LoginUser savedUser = new Gson().fromJson(RespondenActivity.this.getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE).getString(MY_LOGIN_PREF_KEY, ""), LoginUser.class);
        String name = savedUser.getName();
        String nickname = savedUser.getUsername();
        String title = savedUser.getTitle();

        txtName.setText(name +" - "+nickname);
        txtGroup.setText(title);

        Glide.with(this)
                .load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                RespondenHomeFragment respondenHomeFragment = new RespondenHomeFragment();
                return respondenHomeFragment;
            case 1:
                // home
                RespondenProfileFragment rara = new RespondenProfileFragment();
                return rara;
            case 2:
                // home
                RespondenIndexFragment respondenIndexFragment = new RespondenIndexFragment();
                return respondenIndexFragment;
            case 3:
                // home
                RespondenHistoryIndexFragment res = new RespondenHistoryIndexFragment();
                return res;
            default:
                return new RespondenHomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        title.setText("Home Survey");
                        dataFalse();
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        title.setText("My Profile");
                        dataFalse();
                        break;
                    case R.id.nav_index_survey:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_INDEX_SURVEY;
                        title.setText("Index Survey");
                        title.setTextColor(Color.WHITE);
                        searchButton.setVisibility(View.VISIBLE);
                        searchButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                search();
                            }
                        });
                        break;
                    case R.id.nav_history_survey:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_HISTORY_SURVEY;
                        title.setText("History Survey");
                        title.setTextColor(Color.WHITE);
                        searchButton.setVisibility(View.VISIBLE);
                        searchButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                search();
                            }
                        });
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    void dataFalse(){
        title.setTextColor(Color.WHITE);
        title.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.GONE);
        searchEdittext.setVisibility(View.INVISIBLE);
    }
    void search() {
        searchActive = !searchActive;
        if (searchActive) {
            searchEdittext.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(searchEdittext, 0);
        } else {
            if (searchEdittext.getText().toString().length() > 0) {
                searchEdittext.setText("");
                searchActive = !searchActive;
                return;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEdittext.getWindowToken(), 0);
        }
        searchButton.setImageResource(searchActive ? R.drawable.ic_clear : R.drawable.ic_search);
        searchEdittext.setVisibility(searchActive ? View.VISIBLE : View.GONE);
        title.setVisibility(searchActive ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        SharedPreferences preferences = getSharedPreferences(MY_LOGIN_PREF, Context.MODE_PRIVATE);
        preferences.edit().remove(MY_LOGIN_PREF_KEY).apply();
        Intent i = new Intent(RespondenActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
