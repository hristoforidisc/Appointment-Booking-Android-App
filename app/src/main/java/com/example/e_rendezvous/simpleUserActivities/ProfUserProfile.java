package com.example.e_rendezvous.simpleUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.profUserActivities.AddRendezvous;
import com.example.e_rendezvous.profUserActivities.GroupRendezvousAdapter;
import com.example.e_rendezvous.profUserActivities.ProfUserAddServices;
import com.example.e_rendezvous.profUserActivities.ProfUserShoppingCart;
import com.example.e_rendezvous.profUserActivities.ServicesRendezvousAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dbengine.RegistrationEngine;
import models.Business;
import models.DailySchedule;
import models.Group;
import models.ProfUser;
import models.Rendezvous;
import models.Service;

public class ProfUserProfile extends AppCompatActivity {

    private RecyclerView groupRecyclerView, servicesRecyclerView;
    private GroupRendezvousAdapter groupAdapter;
    private ServicesRendezvousAdapter servicesAdapter;
    private ImageButton cartButton, backButton;
    private Button finishButton;
    private TextView headerTextView;
    private FloatingActionButton calendarFloatingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_profile);

        Bundle extras = getIntent().getExtras();
        String profUsername = "";
        if (extras != null) {
            profUsername = (String) extras.get("username");
        }

        calendarFloatingButton = findViewById(R.id.calendarFloatingButton);
        headerTextView = findViewById(R.id.header);

        // ProfUser Initializing
        ProfUser.getInstance(profUsername);

        // Getting profile data from DB
        String result = RegistrationEngine.getInstance().findProfProfile(profUsername);

        // ProfUser Profile Setup
        String[][] profUserData = new String[4][];
        if(result!= null){

            String[] allResultsTest = result.split("NEXT_ARRAY");

            for (int i = 0; i < allResultsTest.length; i++) {
                profUserData[i] = allResultsTest[i].split("NEXT_FIELD");
            }

            // Business Initializing
            ProfUser.getInstance().setBusiness(new Business(profUserData[0][0], profUserData[0][1], profUserData[0][2], profUserData[0][3], profUserData[0][4], profUserData[0][5], profUserData[0][6]));

            // Business Groups & Services Initializing
            if(profUserData[1] != null){
                profUserData[1] = profUserData[1][0].split("NEXT_GROUP");
                for(int i = 0; i < profUserData[1].length; i++){

                    String[] groupName = profUserData[1][i].split("SERVICES_START");
                    ProfUser.getInstance().getBusiness().addGroup(new Group(groupName[0]));
                    String[] services = groupName[1].split("NEXT_SERVICE");

                    for(int j = 0; j < services.length; j++){
                        String[] newService = services[j].split("NSF");
                        ProfUser.getInstance().getBusiness().getGroups().get(i).addService(new Service(newService[0], newService[1], newService[2], newService[3]));
                    }
                }
            }

            // Schedule Initializing
            if(profUserData[2] != null){
                profUserData[2] = profUserData[2][0].split("NEXT_DAY");
                for(int i = 0; i < profUserData[2].length; i++){
                    String[] dailySchedule = profUserData[2][i].split("NDS");
                    ProfUser.getInstance().addDailySchedule(new DailySchedule(dailySchedule[0], dailySchedule[1], dailySchedule[2], dailySchedule[3], dailySchedule[4]));
                }
            }

            // Rendezvous Initializing
            if(profUserData[3] != null){
                profUserData[3] = profUserData[3][0].split("NEXT_RENDEZVOUS");
                for(int i = 0; i < profUserData[3].length; i++){
                    String[] newRendezvous = profUserData[3][i].split("NRF");
                    ProfUser.getInstance().getRendezvous().add(new Rendezvous(newRendezvous[0], newRendezvous[1], newRendezvous[2]));
                }
            }
        }

        // Header text initializing
        headerTextView.setText(ProfUser.getInstance().getBusiness().getBusinessName());


        groupRecyclerView = findViewById(R.id.groupRecyclerView);
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        backButton = findViewById(R.id.backButton);
        cartButton = findViewById(R.id.cartButton);
        finishButton = findViewById(R.id.finishButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfUserProfile.this, LinearLayoutManager.HORIZONTAL, false);
        groupAdapter = new GroupRendezvousAdapter(ProfUserProfile.this, ProfUser.getInstance().getBusiness().getGroupsNames());
        groupRecyclerView.setAdapter(groupAdapter);
        groupRecyclerView.setLayoutManager(layoutManager);

        servicesAdapter = ServicesRendezvousAdapter.getInstance(ProfUserProfile.this, ProfUser.getInstance().getBusiness().getGroups().get(0).getGroupServises());
        servicesRecyclerView.setAdapter(servicesAdapter);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(ProfUserProfile.this));

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity2")) {
                    finish();
                }
            }
        };


        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity2"));

        calendarFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfUserScheduleFreeHours.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SimpleUserShoppingCart.class);
                startActivity(intent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), SimpleUserSelectDate.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Πρόσθεσε υπηρεσίες στο καλάθι!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}