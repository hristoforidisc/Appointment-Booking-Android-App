package com.example.e_rendezvous.profUserActivities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_rendezvous.Login;
import com.example.e_rendezvous.R;
import com.google.android.material.navigation.NavigationView;


import models.ProfUser;

public class ProfUserMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ImageView profUserImage;
    private TextView profUserName;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfUserMainActivity.this);
            builder.setTitle("Τερματισμός εφαρμογής");
            builder.setMessage("Πού πας; Θέλεις σίγουρα να φύγεις;");
            builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    stopService(new Intent(getApplicationContext(), RendezvousBackgroudUpdate.class));
                    finish();
                }
            });
            builder.setNegativeButton("Όχι", null);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_main);

        startService(new Intent(this, RendezvousBackgroudUpdate.class));

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.profUserToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.profUserDrawerLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =  navigationView.getHeaderView(0);
        profUserImage = (ImageView)headerView.findViewById(R.id.profUserImage);
        profUserName = (TextView)headerView.findViewById(R.id.profUserName);
        profUserImage.setImageBitmap(ProfUser.getInstance().getPhotoProfil());
        profUserName.setText(ProfUser.getInstance().getName());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ProfUserMainActivity.this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        onNewIntent(getIntent());
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
        profUserImage.setImageBitmap(ProfUser.getInstance().getPhotoProfil());
        profUserName.setText(ProfUser.getInstance().getName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("destination"))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                break;
            case R.id.edit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditFragment()).commit();
                break;
            case R.id.logout:
                stopService(new Intent(getApplicationContext(), RendezvousBackgroudUpdate.class));
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}