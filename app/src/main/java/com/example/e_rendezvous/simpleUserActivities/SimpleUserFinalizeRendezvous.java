package com.example.e_rendezvous.simpleUserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.profUserActivities.ServicesRendezvousAdapter;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import dbengine.RegistrationEngine;
import models.Basket;
import models.ProfUser;
import models.Rendezvous;
import models.SimpleUser;

public class SimpleUserFinalizeRendezvous extends AppCompatActivity {

    private Calendar selectedDate;
    private ImageButton backButton;
    private int durationSum;
    private float costSum;
    private EditText descriptionEditText;
    private Button finishButton;
    private Spinner freeSlotsSpinner;
    private String selectedHour, dayName, randezvousDate;
    private ArrayList<String> availableTimeSlots;
    private int rendezvousCounter = 0;
    private int flag = -1;
    private int loopFlag = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            loopFlag = 1;
            finish();
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_finalize_rendezvous);

        availableTimeSlots = new ArrayList<>();
        finishButton = findViewById(R.id.finishButton);
        freeSlotsSpinner = findViewById(R.id.freeSlotsSpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopFlag = 1;
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = (Calendar) extras.get("selectedDate");
            dayName = (String) extras.get("dayName");
        }

        randezvousDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + selectedDate.get(Calendar.MONTH) + "/" + selectedDate.get(Calendar.YEAR);

        // Calculate Basket Cost & Duration
        durationSum = 0;
        costSum = 0;
        calculateBasketCostAndDuration();


        populateAvailableTimeSlots();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, availableTimeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        freeSlotsSpinner.setAdapter(adapter);
        freeSlotsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHour = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

       finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = String.valueOf(descriptionEditText.getText());

                if(selectedHour.equals("Επέλεξε Διαθέσιμη ώρα") || selectedHour.equals("Πρωινές ώρες") || selectedHour.equals("Απογευματινές ώρες")){
                    Toast.makeText(getApplicationContext(), "Συμπλήρωσε την ώρα που επιθυμείς!", Toast.LENGTH_LONG).show();
                } else {

                    String time = selectedHour;
                    time = time.replace("Από ", "");
                    String[] CutTime = time.split(" Έως ");

                    String startTime = CutTime[0];
                    String endTime = CutTime[1];


                    Basket basket = new Basket(String.valueOf(costSum), String.valueOf(durationSum), description, ServicesRendezvousAdapter.getInstance().getServicesShoppingCart());
                    Rendezvous currentRendezvous = new Rendezvous(ProfUser.getInstance(), SimpleUser.getInstance(), startTime, endTime, randezvousDate, "false", basket);

                    String result = RegistrationEngine.getInstance().newRendezvousRegistration(currentRendezvous);


                    currentRendezvous.setRendezvousID(result);
                    SimpleUser.getInstance().getRendezvous().add(currentRendezvous);


                    if (result.equals("RendezvousRegistrationFailed")){
                        Toast.makeText(getApplicationContext(),"Κάτι πήγε στραβά!", Toast.LENGTH_SHORT).show();
                    } else{
                        loopFlag = 1;
                        Intent intent = new Intent(getApplicationContext(), SimpleUserMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        startUpdate();
    }

    public void calculateBasketCostAndDuration(){
        for(int i = 0; i < ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().size(); i++){
            costSum += Float.parseFloat(ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().get(i).getCost());
            durationSum += Integer.parseInt(ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().get(i).getDuration());
        }
    }

    public ArrayList<String> getFreeTimeSlots(String randezvousDate, int startNewHour, int startNewMinute, int stopHour, int stopMinute, String message){

        ArrayList<String> availableTimeSlots = new ArrayList<>();
        availableTimeSlots.add(message);

        ArrayList<String> reservedStartTime = new ArrayList<>();
        ArrayList<String> reservedEndTime = new ArrayList<>();

        for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
            if(ProfUser.getInstance().getRendezvous().get(i).getDate().equals(randezvousDate)){
                reservedStartTime.add(ProfUser.getInstance().getRendezvous().get(i).getStartTime());
                reservedEndTime.add(ProfUser.getInstance().getRendezvous().get(i).getEndTime());
            }
        }

        int rendezvousDurationHour = durationSum / 60;
        int rendezvousDurationMin = durationSum % 60;

        for(int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {

                int[] array = calculateNewEnd(rendezvousDurationHour, rendezvousDurationMin, startNewHour, startNewMinute);
                int newEndTimeHour = array[0];
                int newEndTimeMin = array[1];
                int rendezvousPass = 0;

                if(newEndTimeHour < stopHour || (newEndTimeHour == stopHour && newEndTimeMin == stopMinute)) {
                    for (int k = 0; k < reservedStartTime.size(); k++){

                        String[] startTimeKratimeno = reservedStartTime.get(k).split(" : ");
                        int startTimeHour = Integer.parseInt(startTimeKratimeno[0]);
                        int startTimeMinute = Integer.parseInt(startTimeKratimeno[1]);

                        String[] endTimeKratimeno = reservedEndTime.get(k).split(" : ");
                        int endTimeHour = Integer.parseInt(endTimeKratimeno[0]);
                        int endTimeMinute = Integer.parseInt(endTimeKratimeno[1]);


                        if (startTimeHour == startNewHour && startTimeHour == newEndTimeHour && endTimeHour == startTimeHour){
                            if (newEndTimeMin <= startTimeMinute || startNewMinute >= endTimeMinute){
                                rendezvousPass++;
                            }

                        }else if (startTimeHour == startNewHour && endTimeHour > newEndTimeHour && newEndTimeHour == startNewHour){
                            if (newEndTimeMin <= startTimeMinute){
                                rendezvousPass++;
                            }

                        }else if (startTimeHour == startNewHour && endTimeHour < newEndTimeHour && startNewHour == endTimeHour){
                            if (startNewMinute >= endTimeMinute){
                                rendezvousPass++;
                            }
                        }else if (startTimeHour < startNewHour && endTimeHour < startNewHour){
                            rendezvousPass++;

                        } else if (startTimeHour > newEndTimeHour){
                            rendezvousPass++;

                        } else if (newEndTimeHour == startTimeHour){
                            if (newEndTimeMin <= startTimeMinute){
                                rendezvousPass++;
                            }
                        }else if (startTimeHour < startNewHour && endTimeHour == startNewHour){
                            if(endTimeMinute <= startNewMinute){
                                rendezvousPass++;
                            }
                        }
                    }
                    if(rendezvousPass == reservedStartTime.size()){

                        String finalStartHour = String.valueOf(startNewHour);
                        String finalStartMin = String.valueOf(startNewMinute);
                        String finalEndHour = String.valueOf(newEndTimeHour);
                        String finalEndMin = String.valueOf(newEndTimeMin);

                        if(startNewHour < 10){
                            finalStartHour = "0" + finalStartHour;
                        }
                        if(startNewMinute < 10){
                            finalStartMin = "0" + finalStartMin;
                        }
                        if(newEndTimeHour < 10){
                            finalEndHour = "0" + finalEndHour;
                        }
                        if(newEndTimeMin < 10){
                            finalEndMin = "0" + finalEndMin;
                        }

                        availableTimeSlots.add("Από " + finalStartHour + " : " + finalStartMin + " Έως " + finalEndHour + " : " + finalEndMin);
                    }
                }
                startNewMinute += 15;
            }
            startNewHour++;
            startNewMinute = 0;
        }
        return availableTimeSlots;
    }


    public int[] calculateNewEnd(int rendezvousDurationHour, int rendezvousDurationMin, int startNewHour, int startNewMinute){

        int[] array = new int[2];

        if((rendezvousDurationMin + startNewMinute) >= 60){
            array[0] = startNewHour + rendezvousDurationHour + 1;
            array[1] = (rendezvousDurationMin + startNewMinute) - 60;
        }
        else{
            array[0] = startNewHour + rendezvousDurationHour;
            array[1] = startNewMinute + rendezvousDurationMin;
        }
        return array;
    }


    public void startUpdate(){
        String[] field = new String[1];
        field[0] = "profUsername";

        //Creating array for data
        String[] data = new String[1];
        data[0] = ProfUser.getInstance().getUsername();

        PutData putData = new PutData("https://example.com/api/prof-user-all-rendezvous-detection.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {

                // Answer from database
                String newRendezvousExistResult = putData.getResult();
                if(flag == -1){

                    rendezvousCounter = Integer.parseInt(newRendezvousExistResult);
                    flag = 0;
                }
                if(flag == 0){
                    int tmp = Integer.parseInt(newRendezvousExistResult);
                    if(rendezvousCounter < tmp){
                        updateRendezvous();
                        rendezvousCounter = tmp;
                    }
                }
            }
        }

        if(loopFlag == 0){
            makeLoop(6000);
        }
    }

    private void makeLoop(int milliseconds){

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startUpdate();
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }


    private void updateRendezvous(){

        String[] field1 = new String[1];
        field1[0] = "profUsername";

        //Creating array for data
        String[] data1 = new String[1];
        data1[0] = ProfUser.getInstance().getUsername();

        String result = "";
        PutData putData = new PutData("https://example.com/api/simple-user-prof-user-rendezvous-update.php", "POST", field1, data1);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                // Answer from database
                result = putData.getResult();
            }
        }

        // Decoding the DB answer to show greek characters
        try {
            result = new String(result.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        ArrayList<Rendezvous> updatedRendezvous = new ArrayList<Rendezvous>();
        String []rendezvousData = result.split("NEXT_RENDEZVOUS");
        for(int i = 0; i < rendezvousData.length; i++){
            String[] newRendezvous = rendezvousData[i].split("NRF");
            updatedRendezvous.add(new Rendezvous(newRendezvous[0], newRendezvous[1], newRendezvous[2]));
        }

        ProfUser.getInstance().setRendezvous(updatedRendezvous);

        populateAvailableTimeSlots();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, availableTimeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freeSlotsSpinner.setAdapter(adapter);
    }


    private void populateAvailableTimeSlots(){

        for(int i=0; i<ProfUser.getInstance().getWeeklySchedule().size(); i++){
            if(ProfUser.getInstance().getWeeklySchedule().get(i).getDay().equals(dayName)){
                if(ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonStartTime().equals("Not working") &&
                        ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonEndTime().equals("Not working")){

                    String[] startTimeKratimeno = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime().split(" : ");
                    int startTimeHour = Integer.parseInt(startTimeKratimeno[0]);
                    int startTimeMinute = Integer.parseInt(startTimeKratimeno[1]);

                    String[] endTimeKratimeno = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime().split(" : ");
                    int endTimeHour = Integer.parseInt(endTimeKratimeno[0]);
                    int endTimeMinute = Integer.parseInt(endTimeKratimeno[1]);

                    availableTimeSlots = getFreeTimeSlots(randezvousDate, startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, "Επιλέξτε διαθέσιμη ώρα");
                }
                else{
                    // Morning working hours slots
                    ArrayList<String> morning = new ArrayList<>();

                    String[] startTimeKratimeno = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime().split(" : ");
                    int startTimeHour = Integer.parseInt(startTimeKratimeno[0]);
                    int startTimeMinute = Integer.parseInt(startTimeKratimeno[1]);

                    String[] endTimeKratimeno = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime().split(" : ");
                    int endTimeHour = Integer.parseInt(endTimeKratimeno[0]);
                    int endTimeMinute = Integer.parseInt(endTimeKratimeno[1]);

                    morning = getFreeTimeSlots(randezvousDate, startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, "Πρωινές ώρες");

                    // Afternoon working hours slots
                    ArrayList<String> afternoon = new ArrayList<>();

                    String[] startTimeKratimeno2 = ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonStartTime().split(" : ");
                    int startTimeHour2 = Integer.parseInt(startTimeKratimeno2[0]);
                    int startTimeMinute2 = Integer.parseInt(startTimeKratimeno2[1]);

                    String[] endTimeKratimeno2 = ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonEndTime().split(" : ");
                    int endTimeHour2 = Integer.parseInt(endTimeKratimeno2[0]);
                    int endTimeMinute2 = Integer.parseInt(endTimeKratimeno2[1]);

                    afternoon = getFreeTimeSlots(randezvousDate, startTimeHour2, startTimeMinute2, endTimeHour2, endTimeMinute2, "Απογευματινές ώρες");

                    for(int k = 0; k < morning.size(); k++){
                        availableTimeSlots.add(morning.get(k));
                    }
                    for(int j = 0; j < afternoon.size(); j++){
                        availableTimeSlots.add(afternoon.get(j));
                    }
                }
                break;
            }
        }
    }
}