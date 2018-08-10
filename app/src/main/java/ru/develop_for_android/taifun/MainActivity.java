package ru.develop_for_android.taifun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.JobIntentService;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.develop_for_android.taifun.networking.FoodSyncService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FoodListWithCategoriesFragment foodFragment;
    private PromoListFragment promoFragment;
    private OrdersListFragment ordersFragment;
    private MyInfoFragment myInfoFragment;

    private static final String ACTIVE_FRAGMENT_KEY = "active_fragment";
    private int activeDrawerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            activeDrawerId = R.id.nav_food;
        } else {
            activeDrawerId = savedInstanceState.getInt(ACTIVE_FRAGMENT_KEY, R.id.nav_food);
        }
        openSuitableFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ACTIVE_FRAGMENT_KEY, activeDrawerId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent bundle = new Intent();
            JobIntentService.enqueueWork(getBaseContext(),
                    FoodSyncService.class, FoodSyncService.jobId,
                    bundle);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        activeDrawerId = item.getItemId();
        openSuitableFragment();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openSuitableFragment() {

        if (activeDrawerId == R.id.nav_food) {
            foodFragment = new FoodListWithCategoriesFragment();
            changeCurrentFragment(foodFragment);
        } else if (activeDrawerId == R.id.nav_promo) {
            promoFragment = new PromoListFragment();
            changeCurrentFragment(promoFragment);
        } else if (activeDrawerId == R.id.nav_orders) {
            ordersFragment = new OrdersListFragment();
            changeCurrentFragment(ordersFragment);
        } else if (activeDrawerId == R.id.nav_my_info) {
            myInfoFragment = new MyInfoFragment();
            changeCurrentFragment(myInfoFragment);
        }
    }

    private void changeCurrentFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_fragment_place, fragment)
                .commit();
    }
}