package com.anovelmous.app.ui.reading;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.InjectingActivityModule;
import com.anovelmous.app.Injector;
import com.anovelmous.app.R;
import com.anovelmous.app.ui.AboutFragment;
import com.anovelmous.app.ui.ContributeFragment;
import com.anovelmous.app.ui.ToolbarControlBaseActivity;
import com.anovelmous.app.ui.novels.NovelSelectFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.ms.square.android.etsyblur.EtsyActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;
import static com.anovelmous.app.util.Preconditions.checkState;

public final class ReadingActivity extends ToolbarControlBaseActivity<ObservableScrollView>
        implements ReadingFragment.OnFragmentInteractionListener, ContributeFragment.OnFragmentInteractionListener,
                   NovelSelectFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener,
                   Injector {
    private ObjectGraph mObjectGraph;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private EtsyActionBarDrawerToggle drawerToggle;

    @Override
    protected ObservableScrollView createScrollable() {
        return (ObservableScrollView) findViewById(R.id.scrollable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph appGraph = AnovelmousApp.get(this).getObjectGraph();
        List<Object> activityModules = getModules();
        mObjectGraph = appGraph.plus(activityModules.toArray());

        // now we can inject ourselves
        inject(this);

        mDrawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        drawerToggle = setupDrawerToggle();

        mDrawer.setDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        selectDrawerItem(nvDrawer.getMenu().getItem(0));
    }

    private EtsyActionBarDrawerToggle setupDrawerToggle() {
        return new EtsyActionBarDrawerToggle(this, mDrawer, getToolbar(), R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Uncomment to inflate menu items to Action Bar
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_read_fragment:
                fragmentClass = ReadingFragment.class;
                break;
            case R.id.nav_contribute_fragment:
                fragmentClass = ContributeFragment.class;
                break;
            case R.id.nav_novels_fragment:
                fragmentClass = NovelSelectFragment.class;
                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = ReadingFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.scroll_container, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        result.add(new InjectingActivityModule(this, this));
        return result;
    }
}
