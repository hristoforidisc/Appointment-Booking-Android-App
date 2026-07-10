package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import models.ProfUser;
import models.Rendezvous;

public class ProfUserDailyRendezvous extends AppCompatActivity {

    private Calendar selectedDate;
    private String dayClicked;
    private ArrayList<Rendezvous> selectedDateRendezvous;
    private RecyclerView rendezvousRecyclerView;
    private ProfUserRendezvousAdapter rendezvousAdapter;
    private FloatingActionButton addRendezvous;
    private ImageButton backButton;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext(), ProfUserMainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_daily_rendezvous);

        addRendezvous = findViewById(R.id.addRendezvou);
        rendezvousRecyclerView = findViewById(R.id.rendezvousRecyclerView);
        TextView selectedDateTextView = findViewById(R.id.selectedDateTextView);
        backButton = findViewById(R.id.backButton);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = (Calendar) extras.get("selectedDate");
        }


        String toastString = selectedDate.get(Calendar.DAY_OF_MONTH) + " / " + (selectedDate.get(Calendar.MONTH) + 1) + " / " + selectedDate.get(Calendar.YEAR);
        selectedDateTextView.setText(toastString);

        dayClicked = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + selectedDate.get(Calendar.MONTH) + "/" + selectedDate.get(Calendar.YEAR);

        ProfUser.getInstance().sortRendezvous();
        selectedDateRendezvous = findRendezvousOfSelectedDay();

        rendezvousAdapter = new ProfUserRendezvousAdapter(ProfUserDailyRendezvous.this, selectedDateRendezvous, dayClicked);
        rendezvousRecyclerView.setAdapter(rendezvousAdapter);
        rendezvousRecyclerView.setLayoutManager(new LinearLayoutManager(ProfUserDailyRendezvous.this));

        addRendezvous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProfUserAddServices.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onResume() {
        ProfUser.getInstance().sortRendezvous();
        rendezvousAdapter.updateData(findRendezvousOfSelectedDay());
        super.onResume();
    }


    public ArrayList<Rendezvous> findRendezvousOfSelectedDay(){
        ArrayList<Rendezvous> tempRendezvous = new ArrayList<>();
        for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
            if(ProfUser.getInstance().getRendezvous().get(i).getDate().equals(dayClicked) && ProfUser.getInstance().getRendezvous().get(i).getIsReserved().equals("true")){
                tempRendezvous.add(ProfUser.getInstance().getRendezvous().get(i));
            }
        }
        return tempRendezvous;
    }

    public void getSortedRendezvous(){

        for(int i = 0; i < selectedDateRendezvous.size(); i++) {
            for(int j = i + 1; j < selectedDateRendezvous.size(); j++) {
                String []startTimei = selectedDateRendezvous.get(i).getStartTime().split(" : ");
                String []startTimej = selectedDateRendezvous.get(j).getStartTime().split(" : ");
                if (Integer.parseInt(startTimei[0]) > Integer.parseInt(startTimej[0])) {
                    Rendezvous tempRendesvous = new Rendezvous(selectedDateRendezvous.get(i));
                    selectedDateRendezvous.set(i, selectedDateRendezvous.get(j));
                    selectedDateRendezvous.set(j, tempRendesvous);
                }else if (Integer.parseInt(startTimei[0]) == Integer.parseInt(startTimej[0])) {
                    if (Integer.parseInt(startTimei[1]) > Integer.parseInt(startTimej[1])){
                        Rendezvous tempRendesvous = new Rendezvous(selectedDateRendezvous.get(i));
                        selectedDateRendezvous.set(i, selectedDateRendezvous.get(j));
                        selectedDateRendezvous.set(j, tempRendesvous);
                    }
                }
            }
        }
    }
}