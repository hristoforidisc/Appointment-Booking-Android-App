package com.example.e_rendezvous.simpleUserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_rendezvous.Login;
import com.example.e_rendezvous.R;
import com.example.e_rendezvous.profUserActivities.EditFragment;
import com.example.e_rendezvous.profUserActivities.HomeFragment;
import com.example.e_rendezvous.profUserActivities.NotificationFragment;
import com.example.e_rendezvous.profUserActivities.ProfUserMainActivity;
import com.google.android.material.navigation.NavigationView;

import models.ProfUser;
import models.SimpleUser;

public class SimpleUserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SimpleUser currentUser;
    private DrawerLayout drawerLayout;
    private TextView simpleUserName;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SimpleUserMainActivity.this);
            builder.setTitle("Τερματισμός εφαρμογής");
            builder.setMessage("Πού πας; Θέλεις σίγουρα να φύγεις;");
            builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Όχι", null);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_main);

        startService(new Intent(this, SimpleUserRendezvousBackgroundUpdate.class));

        currentUser = SimpleUser.getInstance();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.simpleUserToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.simpleUserDrawerLayout);

        NavigationView navigationView = findViewById(R.id.simple_user_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =  navigationView.getHeaderView(0);
        simpleUserName = (TextView)headerView.findViewById(R.id.simpleUserName);
        simpleUserName.setText(currentUser.getName());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(SimpleUserMainActivity.this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                simpleUserName.setText(SimpleUser.getInstance().getName());
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SimpleUserHomeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleUserName.setText(SimpleUser.getInstance().getName());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SimpleUserHomeFragment()).commit();
                break;
            case R.id.search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                break;
            case R.id.edit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SimpleUserEditFragment()).commit();
                break;
            case R.id.logout:
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}