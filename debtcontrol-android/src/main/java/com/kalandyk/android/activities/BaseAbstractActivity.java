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
import com.kalandyk.android.fragments.AbstractFragment;
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

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerMenuItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshDataAction();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    protected abstract void refreshDataAction();

    protected abstract void replaceFragment(AbstractFragment fragment);

    protected abstract void replaceFragmentWithStackClearing(AbstractFragment fragment);

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
