package com.example.e_rendezvous.profUserActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_rendezvous.R;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import dbengine.RegistrationEngine;
import models.ProfUser;
import models.Rendezvous;

public class RendezvousInfo extends AppCompatActivity {

    private ImageButton backButton;
    private Button removeRendezvousButton;
    private TextView simpleUserNameText, TimeText, dateText, durationText, costText, descriptionText, serviceText, phoneinfo;
    private Rendezvous currentRendezvous;
    private String dayClicked, currentRendezvousID;
    private int position;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rendezvous_info);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = (Integer) extras.get("position");
            dayClicked = (String) extras.get("dayClicked");
        }

        ArrayList<Rendezvous> rendezvousOfSelectedDay = new ArrayList<Rendezvous>();
        if(dayClicked != null){
            for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
                if(ProfUser.getInstance().getRendezvous().get(i).getDate().equals(dayClicked) && ProfUser.getInstance().getRendezvous().get(i).getIsReserved().equals("true")){
                    rendezvousOfSelectedDay.add(ProfUser.getInstance().getRendezvous().get(i));
                }
            }

            for(int j = 0; j < rendezvousOfSelectedDay.size(); j++){
                if(position == j){
                    currentRendezvous = rendezvousOfSelectedDay.get(j);
                    currentRendezvousID = rendezvousOfSelectedDay.get(j).getRendezvousID();
                }
            }
        }


        phoneinfo = findViewById(R.id.phoneinfo);
        removeRendezvousButton = findViewById(R.id.removeRendezvousButton);
        simpleUserNameText = findViewById(R.id.simpleUserNameText);
        TimeText= findViewById(R.id.TimeText);
        dateText= findViewById(R.id.dateText);
        durationText= findViewById(R.id.durationText);
        costText= findViewById(R.id.costText);
        descriptionText= findViewById(R.id.descriptionText);
        serviceText= findViewById(R.id.serviceText);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        removeRendezvousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RendezvousInfo.this);
                builder.setTitle("Ακύρωση Ραντεβού");
                builder.setMessage("Θέλεις σίγουρα να ακυρώσεις αυτό το ραντεβού;");
                builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int which) {

                        for(int j = 0; j < ProfUser.getInstance().getRendezvous().size(); j++){
                            if(ProfUser.getInstance().getRendezvous().get(j).getRendezvousID().equals(currentRendezvousID)){

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
                                LocalDate now = LocalDate.now();
                                LocalDate tmp = LocalDate.parse(dayClicked, dtf);

                                if(!tmp.isBefore(now)){
                                    boolean installed = appInstalledOrNot("com.whatsapp");
                                    if(installed){
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        String message = "Ονομάζομαι " + ProfUser.getInstance().getName() + " και θα ήθελα να ακυρώσω το ραντεβού που είχατε κλείσει στις " + ProfUser.getInstance().getRendezvous().get(j).deleteToString();
                                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+30" + ProfUser.getInstance().getRendezvous().get(j).getSimpleUser().getPhone() +"&text=" + message));
                                        startActivity(intent);
                                    }
                                }
                                RegistrationEngine.getInstance().deleteRendezvous(ProfUser.getInstance().getRendezvous().get(j).getRendezvousID());
                                ProfUser.getInstance().getRendezvous().remove(j);
                                break;
                            }
                        }
                        finish();
                    }
                });
                builder.setNegativeButton("Όχι", null);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });

        simpleUserNameText.setText(currentRendezvous.getSimpleUser().getName());
        TimeText.setText(currentRendezvous.getStartTime() + " - " + currentRendezvous.getEndTime());
        dateText.setText(currentRendezvous.getDay() + " / " + (currentRendezvous.getMonth() + 1) + " / " + currentRendezvous.getYear());
        durationText.setText(currentRendezvous.getBasket().getFinalDuration() + " Λεπτά");
        costText.setText(currentRendezvous.getBasket().getFinalCost() + " Ευρώ");
        descriptionText.setText(currentRendezvous.getBasket().getDescription());
        serviceText.setText(currentRendezvous.getBasket().getServicesOfBasketNames());
        phoneinfo.setText(currentRendezvous.getSimpleUser().getPhone());
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            Toast.makeText(getApplicationContext(), " Για την αποστολή ενημερωτικού μηνύματος πρέπει να εγκαταστήσετε την εφαρμογή 'WhatsApp'", Toast.LENGTH_LONG).show();
            app_installed = false;
        }
        return app_installed;
    }

    public ArrayList<Rendezvous> getSortedRendezvous(ArrayList<Rendezvous> selectedDateRendezvous){

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
        return selectedDateRendezvous;
    }
}