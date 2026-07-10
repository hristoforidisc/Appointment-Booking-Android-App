package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.e_rendezvous.R;
import java.util.ArrayList;
import java.util.Calendar;
import dbengine.RegistrationEngine;
import models.Basket;
import models.ProfUser;
import models.Rendezvous;
import models.SimpleUser;

public class AddRendezvous extends AppCompatActivity {

    private Calendar selectedDate;
    private ImageButton backButton;
    private int durationSum;
    private float costSum;
    private EditText customerName, customerPhone, descriptionEditText;
    private Button contactButton, finishButton;
    private Spinner freeSlotsSpinner;
    private String selectedHour;
    private RecyclerView myNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rendezvous);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = (Calendar) extras.get("selectedDate");
        }

        customerName = findViewById(R.id.customerName);
        customerPhone = findViewById(R.id.customerPhone);
        contactButton = findViewById(R.id.contactButton);
        finishButton = findViewById(R.id.finishButton);
        freeSlotsSpinner = findViewById(R.id.freeSlotsSpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Calculate Basket Cost & Duration
        durationSum = 0;
        costSum = 0;

        calculateBasketCostAndDuration();

        // Arraylist free time slots
        String randezvousDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + selectedDate.get(Calendar.MONTH) + "/" + selectedDate.get(Calendar.YEAR);
        ArrayList<String> availableTimeSlots = getFreeTimeSlots(randezvousDate);
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

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddRendezvous.this);
                dialog.setContentView(R.layout.contact_list);
                dialog.setTitle("Title...");
                myNames = dialog.findViewById(R.id.List);
                ContactsAdapter namesAdapter = new ContactsAdapter(AddRendezvous.this, dialog, customerName, customerPhone, ProfUser.getInstance().getContactsList());
                myNames.setLayoutManager(new LinearLayoutManager(AddRendezvous.this));
                myNames.setAdapter(namesAdapter);
                dialog.show();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clientName = String.valueOf(customerName.getText());
                String clientPhone = String.valueOf(customerPhone.getText());
                String description = String.valueOf(descriptionEditText.getText());

                System.out.println(selectedHour);

                if(clientName.equals("") || clientPhone.equals("") || selectedHour.equals("Επέλεξε Διαθέσιμη ώρα")){
                    Toast.makeText(getApplicationContext(), "Συμπλήρωσε τα στοιχεία του πελάτη", Toast.LENGTH_LONG).show();
                } else {
                    SimpleUser newContact = checkIfContactExist(clientName, clientPhone);

                    String time = String.valueOf(selectedHour);
                    time = time.replace("Από ", "");
                    String[] CutTime = time.split(" Έως ");

                    String startTime = CutTime[0];
                    String endTime = CutTime[1];

                    Basket basket = new Basket(String.valueOf(costSum), String.valueOf(durationSum), description, ServicesRendezvousAdapter.getInstance().getServicesShoppingCart());
                    Rendezvous currentRendezvous = new Rendezvous(ProfUser.getInstance(), newContact, startTime, endTime, randezvousDate, "true", basket);

                    String result = RegistrationEngine.getInstance().newRendezvousRegistration(currentRendezvous);
                    currentRendezvous.setRendezvousID(result);
                    ProfUser.getInstance().addNewRendezvous(currentRendezvous);


                    if (result.equals("RendezvousRegistrationFailed")){
                        Toast.makeText(getApplicationContext(),"Κάτι πήγε στραβά!", Toast.LENGTH_SHORT).show();
                    } else{
                        Intent intent = new Intent(getApplicationContext(), ProfUserDailyRendezvous.class);
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private SimpleUser checkIfContactExist(String clientName, String clientPhone){
        int checkIfContactExitst = -1;
        SimpleUser newContact = new SimpleUser();
        for(int i = 0; i < ProfUser.getInstance().getContactsList().size(); i++){
            if(ProfUser.getInstance().getContactsList().get(i).getName().equals(clientName) && ProfUser.getInstance().getContactsList().get(i).getPhone().equals(clientPhone)){
                checkIfContactExitst = 0;
                newContact = ProfUser.getInstance().getContactsList().get(i);
                break;
            }
        }

        if(checkIfContactExitst != 0){
            newContact = new SimpleUser(clientName, clientPhone);
            ProfUser.getInstance().addNewContact(newContact);
            String result = RegistrationEngine.getInstance().newContactRegistration(newContact);
            if(result.equals("Success")) {
                return newContact;
            }
        }
        return newContact;
    }

    public void calculateBasketCostAndDuration(){
        for(int i = 0; i < ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().size(); i++){
            costSum += Float.parseFloat(ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().get(i).getCost());
            durationSum += Integer.parseInt(ServicesRendezvousAdapter.getInstance().getServicesShoppingCart().get(i).getDuration());
        }
    }

    public ArrayList<String> getFreeTimeSlots(String randezvousDate){

        ArrayList<String> availableTimeSlots = new ArrayList<>();
        availableTimeSlots.add("Επέλεξε Διαθέσιμη ώρα");


        ArrayList<String> reservedStartTime = new ArrayList<>();
        ArrayList<String> reservedEndTime = new ArrayList<>();


        //Ta rantezvou tis meras pou theloume na prosthesoume rantevou
        for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
            if(ProfUser.getInstance().getRendezvous().get(i).getDate().equals(randezvousDate)){
                reservedStartTime.add(ProfUser.getInstance().getRendezvous().get(i).getStartTime());
                reservedEndTime.add(ProfUser.getInstance().getRendezvous().get(i).getEndTime());
            }
        }

        int startNewHour = 6;
        int startNewMinute = 0;
        int rendezvousDurationHour = durationSum / 60;
        int rendezvousDurationMin = durationSum % 60;


        for(int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {

                int[] array = calculateNewEnd(rendezvousDurationHour, rendezvousDurationMin, startNewHour, startNewMinute);
                int newEndTimeHour = array[0];
                int newEndTimeMin = array[1];
                int rendezvousPass = 0;

                if(newEndTimeHour<22 || (newEndTimeHour == 22 && newEndTimeMin == 0)) {
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
}