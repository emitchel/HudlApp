package com.elliot.mitchell.hudlapp.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.enums.MenuAction;
import com.elliot.mitchell.hudlapp.fragments.FavoritesFragment;
import com.elliot.mitchell.hudlapp.fragments.NavigationDrawerFragment;
import com.elliot.mitchell.hudlapp.fragments.SearchFragment;


public class MainActivity extends ActionBarActivity
        implements
                    NavigationDrawerFragment.NavigationDrawerCallbacks {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    //
    private MenuAction _eMA = MenuAction.SEARCH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        //Prevent keyboard from popping up on initial install
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // set global
        _eMA = MenuAction.getMenuActionById(position);

        //replace it
        fragmentManager.beginTransaction()
                .replace(R.id.container, getFragment(_eMA))
                .commit();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#414D58")));
        actionBar.setTitle(mTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.search, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragment(MenuAction eMA){
        Fragment returnFrag = null;
        switch(eMA){
            case SEARCH:
                SearchFragment searchfrag = new SearchFragment();
                mTitle= getResources().getString(R.string.search_google_plus);
                returnFrag = searchfrag;
                break;
            case FAVORITES:
                FavoritesFragment favfrag = new FavoritesFragment();
                mTitle=getResources().getString(R.string.your_favorites);
                returnFrag = favfrag;
                break;
            default:
                SearchFragment defaultfrag = new SearchFragment();
                mTitle= getResources().getString(R.string.search_google_plus);
                returnFrag = defaultfrag;
                break;
        }
        return returnFrag;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }






}
