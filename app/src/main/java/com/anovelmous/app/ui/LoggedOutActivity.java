package com.anovelmous.app.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.resource.User;
import com.anovelmous.app.ui.novels.NovelSelectFragment;
import com.anovelmous.app.ui.reading.ReadingFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.ms.square.android.etsyblur.EtsyActionBarDrawerToggle;

/**
 * Created by Greg Ziegan on 6/28/15.
 */
public final class LoggedOutActivity extends ToolbarControlBaseActivity<ObservableScrollView>
        implements BaseFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {
    public static final String USER_LOGIN_ID = "com.anovelmous.app.ui.LoggedOutActivity.USER_LOGIN_ID";

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.contribute_action_button:
                navigateFromReadToContribute(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Fragment contributeFragment = fragmentManager.findFragmentById(R.id.contribute_container);
        if (contributeFragment != null && contributeFragment.isAdded()) // TODO: create a more elegant way of listening for navigation away from contribute
            fragmentManager.beginTransaction().remove(contributeFragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void navigateFromReadToContribute(MenuItem menuItem) {
        showLoginDialog();
    }

    private void showLoginDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        LoginFragment loginFragment = LoginFragment.newInstance(getString(R.string.login_dialog_title));
        loginFragment.show(getSupportFragmentManager(), "dialog");
        ft.commit();
    }

    @Override
    public void onSuccessfulLogin(User user) {
        Intent intent = new Intent(this, LoggedInActivity.class);
        intent.putExtra(LoggedOutActivity.USER_LOGIN_ID, user.id);
        startActivity(intent);
        finish();
    }
}
