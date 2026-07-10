package com.example.e_rendezvous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_rendezvous.profUserActivities.EditProfUserGroupsServices;
import com.example.e_rendezvous.profUserActivities.ProfUserMainActivity;
import com.example.e_rendezvous.profUserActivities.SendHttpRequestTask;
import com.example.e_rendezvous.registration.BusinessRegistration;
import com.example.e_rendezvous.registration.GroupRegistration;
import com.example.e_rendezvous.registration.WeeklyScheduleRegistration;
import com.example.e_rendezvous.simpleUserActivities.SimpleUserMainActivity;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import models.Basket;
import models.Business;
import models.DailySchedule;
import models.Group;
import models.ProfUser;
import models.Rendezvous;
import models.Service;
import models.SimpleUser;


public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView signupText;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.SimpleUserSignupButton);
        signupText = findViewById(R.id.SignupText);

        signupText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreSignup.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameInput, passwordInput;
                usernameInput = String.valueOf(username.getText());
                passwordInput = String.valueOf(password.getText());

                if(!usernameInput.equals("") && !passwordInput.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";

                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = usernameInput;
                            data[1] = passwordInput;

                            PutData putData = new PutData("https://example.com/api/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    // Answer from database
                                    String result = putData.getResult();

                                    // Checks if result contais NEXT_ARRAY, if true we have a ProfUser
                                    boolean isFound = result.contains("NEXT_ARRAY");

                                    // Decoding the DB answer to show greek characters
                                    try {
                                        result = new String(result.getBytes("ISO-8859-1"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    String[][] profUserData = new String[7][];

                                    if(!isFound && !result.equals("Username or Password wrong")){
                                        //Simple user
                                        String[][] simpleUserData = new String[2][];

                                        String[] allResults = result.split("NEXT_SU_ARRAY");

                                        for (int i = 0; i < allResults.length; i++) {
                                            simpleUserData[i] = allResults[i].split("NEXT_FIELD");
                                        }

                                        SimpleUser.getInstance(simpleUserData[0][0], simpleUserData[0][1], simpleUserData[0][5], simpleUserData[0][4], simpleUserData[0][2], simpleUserData[0][3]);


                                        if(simpleUserData[1] != null) {
                                            String[] allRendezvous = simpleUserData[1][0].split("NEXT_RENDEZVOUS");

                                            for (int i = 0; i < allRendezvous.length; i++) {
                                                String[] singleRendezvous = allRendezvous[i].split("NRF");

                                                ArrayList<Service> servicesArrayList = new ArrayList<Service>();
                                                boolean moreThanOneService = singleRendezvous[7].contains(",");
                                                String[] servicesNames;
                                                if (moreThanOneService) {
                                                    servicesNames = singleRendezvous[7].split(",");
                                                    for (int k = 0; k < servicesNames.length; k++) {
                                                        servicesArrayList.add(new Service(servicesNames[k]));
                                                    }
                                                } else {
                                                    servicesArrayList.add(new Service(singleRendezvous[7]));
                                                }
                                                ProfUser tempProfUser = new ProfUser();
                                                tempProfUser.setBusiness(new Business(singleRendezvous[9], singleRendezvous[10], singleRendezvous[12], singleRendezvous[13], singleRendezvous[14]));

                                                SimpleUser.getInstance().getRendezvous().add(new Rendezvous(tempProfUser, SimpleUser.getInstance(), singleRendezvous[2], singleRendezvous[3], singleRendezvous[1], singleRendezvous[8], new Basket(singleRendezvous[4], singleRendezvous[5], singleRendezvous[6], servicesArrayList), singleRendezvous[11]));
                                            }
                                        }

                                        SimpleUser.getInstance().sortRendezvous();

                                        Intent intent = new Intent(getApplicationContext(), SimpleUserMainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else if (isFound && !result.equals("Username or Password wrong")) {

                                        String[] allResultsTest = result.split("NEXT_ARRAY");

                                        for (int i = 0; i < allResultsTest.length; i++) {
                                            profUserData[i] = allResultsTest[i].split("NEXT_FIELD");

                                        }
                                        // ProfUser Initializing
                                        ProfUser.getInstance(profUserData[0][0], profUserData[0][1], profUserData[0][2], profUserData[0][3]);


                                        if(profUserData[1] != null){

                                            // Business Initializing
                                            ProfUser.getInstance().setBusiness(new Business(profUserData[1][0], profUserData[1][1], profUserData[1][2], profUserData[1][3],
                                                    profUserData[1][4], profUserData[1][5], profUserData[1][6]));

                                            // Groups & Services Initializing
                                            if(profUserData[2] != null){
                                                profUserData[2] = profUserData[2][0].split("NEXT_GROUP");

                                                int groupTestServices = -1;
                                                for(int i = 0; i < profUserData[2].length; i++){

                                                    String[] groupName = profUserData[2][i].split("SERVICES_START");
                                                    if(!groupName[0].equals("")){
                                                        ProfUser.getInstance().getBusiness().addGroup(new Group(groupName[0]));
                                                    }
                                                    if(groupName.length == 1){
                                                        groupTestServices = 1;
                                                    }else{

                                                        String[] services = groupName[1].split("NEXT_SERVICE");
                                                        for(int j = 0; j < services.length; j++){
                                                            String[] newService = services[j].split("NSF");
                                                            ProfUser.getInstance().getBusiness().getGroups().get(i).addService(new Service(newService[0], newService[1], newService[2], newService[3]));
                                                        }
                                                    }
                                                }

                                                // Schedule Initializing
                                                if(profUserData[3] != null){
                                                    profUserData[3] = profUserData[3][0].split("NEXT_DAY");

                                                    for(int i = 0; i <  profUserData[3].length; i++){

                                                        String[] dailySchedule = profUserData[3][i].split("NDS");
                                                        ProfUser.getInstance().addDailySchedule(new DailySchedule(dailySchedule[0], dailySchedule[1], dailySchedule[2], dailySchedule[3], dailySchedule[4]));
                                                    }

                                                    // Users outside the app Contacts List Initializing
                                                    if(profUserData[4] != null) {

                                                        if (!profUserData[4][0].equals("")) {

                                                            profUserData[4] = profUserData[4][0].split("NEXT_CONTACT");
                                                            for (int i = 0; i < profUserData[4].length; i++) {
                                                                String[] newContact = profUserData[4][i].split("NCF");
                                                                ProfUser.getInstance().addNewContact(new SimpleUser(newContact[0], newContact[1]));
                                                            }
                                                        }
                                                    }

                                                    // Users from the app Contacts List Initializing
                                                    ArrayList<SimpleUser> simpleUsersContacts = new ArrayList<>();
                                                    if(profUserData[5] != null) {
                                                        if(!profUserData[5][0].equals("")){

                                                            profUserData[5] = profUserData[5][0].split("NEXT_SIMPLE_USER_CONTACT");
                                                            for (int i = 0; i < profUserData[5].length; i++) {
                                                                String[] newContact = profUserData[5][i].split("NUF");
                                                                simpleUsersContacts.add(new SimpleUser(newContact[0], newContact[1], newContact[5], newContact[4], newContact[2], newContact[3]));
                                                            }
                                                        }
                                                    }

                                                    // Rendezvous Initializing
                                                    if(profUserData[6] != null){

                                                        if(!profUserData[6][0].equals("")) {
                                                            profUserData[6] = profUserData[6][0].split("NEXT_RENDEZVOUS");
                                                            for (int i = 0; i < profUserData[6].length; i++) {

                                                                String[] newRendezvous = profUserData[6][i].split("NRF");
                                                                String clientPhone = newRendezvous[8];
                                                                SimpleUser currentSimpleUser = new SimpleUser();
                                                                int userFound = 0;

                                                                for (int j = 0; j < simpleUsersContacts.size(); j++) {
                                                                    if (simpleUsersContacts.get(j).getPhone().equals(clientPhone)) {
                                                                        currentSimpleUser = simpleUsersContacts.get(j);
                                                                        userFound = 1;
                                                                    }
                                                                }

                                                                if (userFound == 0) {
                                                                    for (int j = 0; j < ProfUser.getInstance().getContactsList().size(); j++) {
                                                                        if (ProfUser.getInstance().getContactsList().get(j).getPhone().equals(clientPhone)) {
                                                                            currentSimpleUser = ProfUser.getInstance().getContactsList().get(j);
                                                                        }
                                                                    }
                                                                }

                                                                ArrayList<Service> servicesArrayList = new ArrayList<Service>();
                                                                boolean moreThanOneService = newRendezvous[6].contains(",");
                                                                String[] servicesNames;

                                                                if (moreThanOneService) {

                                                                    servicesNames = newRendezvous[6].split(",");
                                                                    for (int k = 0; k < servicesNames.length; k++) {
                                                                        servicesArrayList.add(new Service(servicesNames[k]));
                                                                    }

                                                                } else {
                                                                    servicesArrayList.add(new Service(newRendezvous[6]));
                                                                }

                                                                ProfUser.getInstance().addNewRendezvous(new Rendezvous(ProfUser.getInstance(), currentSimpleUser, newRendezvous[1], newRendezvous[2], newRendezvous[0], newRendezvous[7], new Basket(newRendezvous[3], newRendezvous[4], newRendezvous[5], servicesArrayList), newRendezvous[9]));
                                                            }
                                                        }
                                                    }

                                                    try {
                                                        Bitmap returned_bitmap = new SendHttpRequestTask(ProfUser.getInstance().getUsername()).execute().get();
                                                        ProfUser.getInstance().setPhotoProfil(returned_bitmap);

                                                    } catch (ExecutionException | InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    ProfUser.getInstance().sortRendezvous();


                                                    if(groupTestServices == 1) {
                                                        Intent intent = new Intent(getApplicationContext(), EditProfUserGroupsServices.class);
                                                        startActivityForResult(intent, 1);
                                                    }else {
                                                        Intent intent = new Intent(getApplicationContext(), ProfUserMainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                }else{
                                                    Intent intent = new Intent(getApplicationContext(), WeeklyScheduleRegistration.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }else{
                                                Intent intent = new Intent(getApplicationContext(), GroupRegistration.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }else{
                                            Intent intent = new Intent(getApplicationContext(), BusinessRegistration.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Όλα τα πεδία είναι απαραίτητα", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), ProfUserMainActivity.class);
        startActivity(intent);
        finish();
    }

}