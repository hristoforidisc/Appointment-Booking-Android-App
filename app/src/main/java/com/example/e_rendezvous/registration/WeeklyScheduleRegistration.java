package com.example.e_rendezvous.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_rendezvous.Login;
import com.example.e_rendezvous.R;

import dbengine.RegistrationEngine;
import models.ProfUser;

public class WeeklyScheduleRegistration extends AppCompatActivity {

    private Button finishButton;
    private RecyclerView weeklyScheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;

    private String[] days;
    private int images[] = {R.drawable.ic_monday, R.drawable.ic_tuesday, R.drawable.ic_wednesday,
            R.drawable.ic_thursday, R.drawable.ic_friday, R.drawable.ic_saturday, R.drawable.ic_sunday};



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WeeklyScheduleRegistration.this);
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
        setContentView(R.layout.activity_weekly_schedule_registration);

        finishButton = findViewById(R.id.finishWeeklySchedule);
        weeklyScheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        days = getResources().getStringArray(R.array.Days);


        scheduleAdapter = ScheduleAdapter.getInstance(WeeklyScheduleRegistration.this, ProfUser.getInstance().getWeeklySchedule(), days, images);
        weeklyScheduleRecyclerView.setAdapter(scheduleAdapter);
        weeklyScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(WeeklyScheduleRegistration.this));


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = RegistrationEngine.getInstance().dailyScheduleRegistration();
                if (result.equals("Success")) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (result.equals("Όλα τα πεδία είναι απαραίτητα")) {
                        Toast.makeText(getApplicationContext(), "Συμπλήρωσε το ωράριο εργασίας σου επιλέγοντας τις ημέρες που επιθυμείς!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}