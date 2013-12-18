package com.kalandyk.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
import com.kalandyk.api.model.Debt;
import com.kalandyk.fragments.AddDebtDialogFragment;
import com.kalandyk.fragments.ConfirmationFragment;
import com.kalandyk.fragments.DebtsListFragment;
import com.kalandyk.listeners.NewDebtListener;
import com.kalandyk.services.DebtService;

import java.util.Stack;

public abstract class AbstractActivity extends FragmentActivity {

    public static final String TAG = "com.kalandyk.debtcontrol";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    protected Fragment currentFragment;

    private Stack<Fragment> fragmentStack;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        fragmentStack = new Stack<Fragment>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
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

        replaceFragment(getContentFragment());
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
        switch(item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: Add another fragments
        }
    }

    protected final void replaceFragment(Fragment fragment){
        replaceFragment(fragment, false);
    }

    private void replaceFragment(Fragment fragment, boolean fromStack){
        if(currentFragment != null && !fromStack){
            fragmentStack.add(currentFragment);
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        currentFragment = fragment;
    }

    protected abstract Fragment getContentFragment();

    public void onAddDebtButtonClick(View v){
        AddDebtDialogFragment addDebtDialogFragment = new AddDebtDialogFragment();
        addDebtDialogFragment.show(getFragmentManager(), "ADD DEBT DIALOG");
        addDebtDialogFragment.setNewDebtListener(new NewDebtListener() {
            @Override
            public void newDebtAdded(Debt debt) {
                Log.d(TAG, "New debt added");
                DebtService instance = DebtService.getInstance();
                instance.addDebt(debt);
                if(currentFragment instanceof DebtsListFragment){
                    ((DebtsListFragment)currentFragment).notifyDataChanged();
                }
            }
        });


    }

    public void onButtonConfirmationButtonClick(View v){
        replaceFragment(new ConfirmationFragment());
    }

    @Override
    public void onBackPressed(){
        if(!fragmentStack.empty()){
            Fragment lastFragment = fragmentStack.pop();
            replaceFragment(lastFragment, true);
        }
        else {
            super.onBackPressed();
        }
    }


}

