package com.project.rushabh.epicure.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.fragment.OrderFragment;
import com.project.rushabh.epicure.fragment.PlacesFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentTransaction fragmentTransaction;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //startService(new Intent(this, NotificationInstanceIdService.class));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentSetter(new PlacesFragment());

        // Notification BroadcastReceiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (Objects.equals(intent.getAction(), getString(R.string.token_registration_complete))) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.topic_global));

                    displayFirebaseRegId();

                } else if (Objects.equals(intent.getAction(), getString(R.string.push_notification))) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
        String regId = pref.getString(getString(R.string.spk_notification_token), null);

        Log.e(TAG, "Firebase reg id: " + regId);

        /*if (!TextUtils.isEmpty(regId))
            Toast.makeText(this, regId, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(getString(R.string.token_registration_complete)));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(getString(R.string.push_notification)));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_1) {
            fragmentSetter(new PlacesFragment());
        } else if (id == R.id.nav_item_2) {

        } else if (id == R.id.nav_item_3) {
            fragmentSetter(new OrderFragment());
        } else if (id == R.id.nav_item_4) {

        } else if (id == R.id.nav_item_5) {
            editor = sharedPreferences.edit();
            FirebaseAuth.getInstance().signOut();
            editor.putBoolean(getString(R.string.is_logged_in), false).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_sub_item_1) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fragmentSetter(Fragment fragmentToShow) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_drawer_main, fragmentToShow);
        fragmentTransaction.commit();
    }
}
