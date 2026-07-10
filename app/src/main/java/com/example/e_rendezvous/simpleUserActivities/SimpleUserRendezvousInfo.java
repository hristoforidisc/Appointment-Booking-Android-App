package com.example.e_rendezvous.simpleUserActivities;

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
import models.Rendezvous;
import models.SimpleUser;

public class SimpleUserRendezvousInfo extends AppCompatActivity {

    private ImageButton backButton, googleMaps;
    private Button removeRendezvousButton;
    private TextView profUserNameText, phoneinfo, TimeText, dateText, durationText, costText, descriptionText, serviceText, addressinfo;
    private Rendezvous currentRendezvous;
    private int position;
    private int currentRendezvousPosition;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_rendezvous_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = (Integer) extras.get("position");
        }

        ArrayList<Rendezvous> upcomingRendexvous = getUpcomingRendezvous();

        for(int j = 0; j < SimpleUser.getInstance().getRendezvous().size(); j++){
            if(upcomingRendexvous.get(position).getRendezvousID().equals(SimpleUser.getInstance().getRendezvous().get(j).getRendezvousID())){
                currentRendezvous = upcomingRendexvous.get(position);
                currentRendezvousPosition = j;
                break;
            }
        }

        addressinfo = findViewById(R.id.addressinfo);
        profUserNameText = findViewById(R.id.profUserNameText);
        phoneinfo = findViewById(R.id.phoneinfo);
        TimeText= findViewById(R.id.TimeText);
        dateText= findViewById(R.id.dateText);
        durationText= findViewById(R.id.durationText);
        costText= findViewById(R.id.costText);
        descriptionText= findViewById(R.id.descriptionText);
        serviceText= findViewById(R.id.serviceText);
        backButton = findViewById(R.id.backButton);
        removeRendezvousButton = findViewById(R.id.removeRendezvousButton);
        googleMaps = findViewById(R.id.googleMaps);

        googleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getProfUser().getBusiness().getBusinessAddress().replace(" ", "+");
                address += ",+" + SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getProfUser().getBusiness().getBusinessCity() + ",+" + SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getProfUser().getBusiness().getBusinessPostCode();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                startActivity(intent);
            }
        });

        removeRendezvousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SimpleUserRendezvousInfo.this);
                builder.setTitle("Ακύρωση Ραντεβού");
                builder.setMessage("Θέλεις σίγουρα να ακυρώσεις αυτό το ραντεβού;");
                builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getRendezvousID() != null){

                            boolean installed = appInstalledOrNot("com.whatsapp");

                            if(installed){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                String message = "Ονομάζομαι " + SimpleUser.getInstance().getName() + " και θα ήθελα να ακυρώσω το ραντεβού που είχα κλείσει στις " + SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).deleteToString();
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+30" + SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getProfUser().getBusiness().getBusinessPhone() +"&text=" + message));
                                startActivity(intent);
                            }
                            RegistrationEngine.getInstance().deleteRendezvous(SimpleUser.getInstance().getRendezvous().get(currentRendezvousPosition).getRendezvousID());
                            SimpleUser.getInstance().getRendezvous().remove(currentRendezvousPosition);
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("Όχι", null);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profUserNameText.setText(currentRendezvous.getProfUser().getBusiness().getBusinessName());
        phoneinfo.setText(currentRendezvous.getProfUser().getBusiness().getBusinessPhone());
        TimeText.setText(currentRendezvous.getStartTime() + " - " + currentRendezvous.getEndTime());
        dateText.setText(currentRendezvous.getDay() + " / " + (currentRendezvous.getMonth() + 1) + " / " + currentRendezvous.getYear());
        durationText.setText(currentRendezvous.getBasket().getFinalDuration() + " Λεπτά");
        costText.setText(currentRendezvous.getBasket().getFinalCost() + " Ευρώ");
        descriptionText.setText(currentRendezvous.getBasket().getDescription());
        serviceText.setText(currentRendezvous.getBasket().getServicesOfBasketNames());
        addressinfo.setText(currentRendezvous.getProfUser().getBusiness().getBusinessAddress());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Rendezvous> getUpcomingRendezvous(){
        ArrayList<Rendezvous> upcomingRendezvous = new ArrayList<>();
        for(int i=0; i<SimpleUser.getInstance().getRendezvous().size(); i++){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate now = LocalDate.now();
            String[] date = SimpleUser.getInstance().getRendezvous().get(i).getDate().split("/");
            String newDate = date[0] + "/" +  (Integer.parseInt(date[1])+1) + "/" + date[2];
            LocalDate tmp = LocalDate.parse(newDate, dtf);
            boolean test = tmp.isBefore(now);
            if(!test){
                upcomingRendezvous.add(SimpleUser.getInstance().getRendezvous().get(i));
            }
        }
        return upcomingRendezvous;
    }
}