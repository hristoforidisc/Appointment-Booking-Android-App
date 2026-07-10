package com.example.e_rendezvous.profUserActivities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.e_rendezvous.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import models.Basket;
import models.ProfUser;
import models.Rendezvous;
import models.SimpleUser;


public class RendezvousBackgroudUpdate extends Service {

    private int rendezvousCounter = 0;
    private int flag = -1;
    private int loopFlag = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startUpdate();
        return super.onStartCommand(intent, flags, startId);

    }

    public void startUpdate(){
        String[] field = new String[1];
        field[0] = "profUsername";

        //Creating array for data
        String[] data = new String[1];
        data[0] = ProfUser.getInstance().getUsername();

        PutData putData = new PutData("https://example.com/api/prof-user-new-rendezvous-detection.php", "POST", field, data);
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
                        sendNotification();
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
        PutData putData = new PutData("https://example.com/api/prof-user-rendezvous-background-service.php", "POST", field1, data1);
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

        // Users from the app Contacts List Initializing
        String profUserData[] = result.split("NEXT_ARRAY");
        ArrayList<SimpleUser> simpleUsersContacts = new ArrayList<>();
        if(profUserData[0] != null) {
            if(!profUserData[0].equals("")){
                String []contacts = profUserData[0].split("NEXT_SIMPLE_USER_CONTACT");
                for (int i = 0; i < contacts.length; i++) {
                    String[] newContact = contacts[i].split("NUF");
                    simpleUsersContacts.add(new SimpleUser(newContact[0], newContact[1], newContact[5], newContact[4], newContact[2], newContact[3]));
                }
            }
        }

        ArrayList<Rendezvous> updatedRendezvous = new ArrayList<Rendezvous>();
        // Rendezvous Initializing
        if(profUserData[1] != null){
            if(!profUserData[1].equals("")) {
                String[] rendezvous = profUserData[1].split("NEXT_RENDEZVOUS");
                for (int i = 0; i < rendezvous.length; i++) {

                    String[] newRendezvous = rendezvous[i].split("NRF");

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

                    ArrayList<models.Service> servicesArrayList = new ArrayList<>();
                    boolean moreThanOneService = newRendezvous[6].contains(",");
                    String[] servicesNames;
                    if (moreThanOneService) {
                        servicesNames = newRendezvous[6].split(",");
                        for (int k = 0; k < servicesNames.length; k++) {
                            servicesArrayList.add(new models.Service(servicesNames[k]));
                        }
                    } else {
                        servicesArrayList.add(new models.Service(newRendezvous[6]));
                    }

                    updatedRendezvous.add(new Rendezvous(ProfUser.getInstance(), currentSimpleUser, newRendezvous[1], newRendezvous[2], newRendezvous[0], newRendezvous[7], new Basket(newRendezvous[3], newRendezvous[4], newRendezvous[5], servicesArrayList), newRendezvous[9]));
                }
            }
        }
        ProfUser.getInstance().setRendezvous(updatedRendezvous);
        ProfUser.getInstance().sortRendezvous();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loopFlag = 1;
    }

    private void sendNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My notification", "My notification", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        String message = "Έχεις ένα νέο ραντεβού για έλεγχο!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "My notification");
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Νέα ειδοποίηση");
        builder.setContentText(message);
        builder.setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), ProfUserMainActivity.class);
        intent.putExtra("destination","NotificationFragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
    }
}
