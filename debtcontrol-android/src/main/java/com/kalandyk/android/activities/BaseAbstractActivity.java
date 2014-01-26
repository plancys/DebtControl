package com.kalandyk.android.activities;

import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.android.activities.drawer.actions.DrawerAction;
import com.kalandyk.android.fragments.DebtsListFragment;
import com.kalandyk.android.fragments.WelcomeFragment;
import com.kalandyk.android.utils.SharedPreferencesBuilder;

/**
 * Created by kamil on 12/29/13.
 */
public abstract class BaseAbstractActivity extends FragmentActivity {

    protected final SharedPreferencesBuilder sharedPreferencesBuilder = new SharedPreferencesBuilder(this);

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] drawerMenuItems;


    protected void initNavigationDrawer() {
        mTitle = mDrawerTitle = getTitle();
        drawerMenuItems = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void replaceFragment(Fragment fragment);

    protected abstract void replaceFragmentWithStackClearing(Fragment fragment);

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: Add another fragments
            Log.d(AbstractDebtActivity.TAG, "onDrawerItemClick()");
            selectDrawerMenuItem(position);

        }
    }


    private void selectDrawerMenuItem(int position) {

        switch (position) {
            case DrawerAction.DASHBOARD:
                replaceFragment(new DebtsListFragment());
                break;
            case DrawerAction.LOGOUT:
                sharedPreferencesBuilder.saveCurrentUser(null);
                replaceFragmentWithStackClearing(new WelcomeFragment());
                break;

            default:
                replaceFragment(new DebtsListFragment());
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }


}
