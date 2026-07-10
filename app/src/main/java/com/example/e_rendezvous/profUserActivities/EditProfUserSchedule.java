package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_rendezvous.Login;
import com.example.e_rendezvous.R;
import com.example.e_rendezvous.registration.ScheduleAdapter;
import com.example.e_rendezvous.registration.WeeklyScheduleRegistration;

import java.util.ArrayList;

import dbengine.RegistrationEngine;
import models.DailySchedule;
import models.ProfUser;

public class EditProfUserSchedule extends AppCompatActivity {

    private Button finishButton;
    private RecyclerView weeklyScheduleRecyclerView;
    private EditScheduleAdapter scheduleAdapter;
    private ImageButton backButton;

    private String[] days;
    private int images[] = {R.drawable.ic_monday, R.drawable.ic_tuesday, R.drawable.ic_wednesday,
            R.drawable.ic_thursday, R.drawable.ic_friday, R.drawable.ic_saturday, R.drawable.ic_sunday};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof_user_schedule);

        backButton = findViewById(R.id.backButton);
        finishButton = findViewById(R.id.finishWeeklySchedule);
        weeklyScheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        days = getResources().getStringArray(R.array.Days);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        scheduleAdapter = EditScheduleAdapter.getInstance(EditProfUserSchedule.this, ProfUser.getInstance().getWeeklySchedule(), days, images);
        weeklyScheduleRecyclerView.setAdapter(scheduleAdapter);
        weeklyScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(EditProfUserSchedule.this));


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = RegistrationEngine.getInstance().updateDailySchedule();
                if (result.equals("Success")) {
                    Toast.makeText(getApplicationContext(),"Επιτυχής ενημέρωση ωραρίου", Toast.LENGTH_SHORT).show();
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