package ru.develop_for_android.taifun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.JobIntentService;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ru.develop_for_android.taifun.networking.FoodSyncService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FoodListWithCategoriesFragment foodFragment;
    private PromoListFragment promoFragment;
    private OrdersListFragment ordersFragment;
    private MyInfoFragment myInfoFragment;

    private static final int PERMISSION_CALL_CODE = 124;

    private static final String ACTIVE_FRAGMENT_KEY = "active_fragment";
    private int activeDrawerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent openOrderDetails = new Intent(getBaseContext(), OrderDetailsActivity.class);
            startActivity(openOrderDetails);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Button callActionButton = navigationView.getHeaderView(0).findViewById(R.id.call_action);
        callActionButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_CODE);
            } else {
                callCafe();
            }
        });

        if (savedInstanceState == null) {
            activeDrawerId = R.id.nav_food;
        } else {
            activeDrawerId = savedInstanceState.getInt(ACTIVE_FRAGMENT_KEY, R.id.nav_food);
        }
        openSuitableFragment();

        listenForRemoteChanges();
    }

    private void listenForRemoteChanges() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference foodRef = db.collection("categories");
        foodRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w("FIREBASE", "Listen failed.", e);
                return;
            }

            if (queryDocumentSnapshots != null) {
                Log.d("FIREBASE", "Updates found");

                Intent bundle = new Intent();
                bundle.putExtra(FoodSyncService.KEY_JOB_TYPE, FoodSyncService.TYPE_FOOD);
                JobIntentService.enqueueWork(getBaseContext(),
                        FoodSyncService.class, FoodSyncService.jobId,
                        bundle);
            } else {
                Log.d("FIREBASE", "Current data: null");
            }

        });

        final CollectionReference promoRef = db.collection("promo");
        promoRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w("FIREBASE", "Listen failed.", e);
                return;
            }

            if (queryDocumentSnapshots != null) {
                Log.d("FIREBASE", "Updates found");

                Intent bundle = new Intent();
                bundle.putExtra(FoodSyncService.KEY_JOB_TYPE, FoodSyncService.TYPE_PROMO);
                JobIntentService.enqueueWork(getBaseContext(),
                        FoodSyncService.class, FoodSyncService.jobId,
                        bundle);
            } else {
                Log.d("FIREBASE", "Current data: null");
            }

        });
    }

    @SuppressLint("MissingPermission")
    private void callCafe() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123456789"));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CALL_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        callCafe();
                    } else {
                        Toast.makeText(getBaseContext(),
                                "permission is not granted. We are unable to call cafe.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
