package com.example.e_rendezvous.profUserActivities;

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
import android.widget.Toast;
import com.example.e_rendezvous.R;

import java.util.Calendar;

import models.ProfUser;

public class ProfUserAddServices extends AppCompatActivity {

    private RecyclerView groupRecyclerView, servicesRecyclerView;
    private GroupRendezvousAdapter groupAdapter;
    private ServicesRendezvousAdapter servicesAdapter;
    private ImageButton cartButton, backButton;
    private Button finishButton;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_add_services);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = (Calendar) extras.get("selectedDate");
        }

        groupRecyclerView = findViewById(R.id.groupRecyclerView);
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        backButton = findViewById(R.id.backButton);
        cartButton = findViewById(R.id.cartButton);
        finishButton = findViewById(R.id.finishButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfUserAddServices.this, LinearLayoutManager.HORIZONTAL, false);
        groupAdapter = new GroupRendezvousAdapter(ProfUserAddServices.this, ProfUser.getInstance().getBusiness().getGroupsNames());
        groupRecyclerView.setAdapter(groupAdapter);
        groupRecyclerView.setLayoutManager(layoutManager);

        servicesAdapter = ServicesRendezvousAdapter.getInstance(ProfUserAddServices.this, ProfUser.getInstance().getBusiness().getGroups().get(0).getGroupServises());
        servicesRecyclerView.setAdapter(servicesAdapter);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(ProfUserAddServices.this));


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };


        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfUserShoppingCart.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), AddRendezvous.class);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Πρόσθεσε υπηρεσίες στο καλάθι!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}