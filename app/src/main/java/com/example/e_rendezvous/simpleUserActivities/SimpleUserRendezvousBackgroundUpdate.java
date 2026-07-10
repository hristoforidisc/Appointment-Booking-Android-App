package com.example.e_rendezvous.simpleUserActivities;

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
import models.Business;
import models.ProfUser;
import models.Rendezvous;
import models.SimpleUser;

public class SimpleUserRendezvousBackgroundUpdate extends Service {

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
        field[0] = "clientPhone";

        //Creating array for data
        String[] data = new String[1];
        data[0] = SimpleUser.getInstance().getPhone();

        PutData putData = new PutData("https://example.com/api/simple-user-new-rendezvous-detection.php", "POST", field, data);
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
        field1[0] = "clientPhone";

        //Creating array for data
        String[] data1 = new String[1];
        data1[0] = SimpleUser.getInstance().getPhone();

        String result = "";
        PutData putData = new PutData("https://example.com/api/simple-user-background-rendezvous-update.php", "POST", field1, data1);
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
        String[] allRendezvous = result.split("NEXT_RENDEZVOUS");

        for (int i = 0; i < allRendezvous.length; i++) {
            String[] singleRendezvous = allRendezvous[i].split("NRF");
            ArrayList<models.Service> servicesArrayList = new ArrayList<models.Service>();
            boolean moreThanOneService = singleRendezvous[7].contains(",");
            String[] servicesNames;
            if (moreThanOneService) {
                servicesNames = singleRendezvous[7].split(",");
                for (int k = 0; k < servicesNames.length; k++) {
                    servicesArrayList.add(new models.Service(servicesNames[k]));
                }
            } else {
                servicesArrayList.add(new models.Service(singleRendezvous[7]));
            }
            ProfUser tempProfUser = new ProfUser();
            tempProfUser.setBusiness(new Business(singleRendezvous[9], singleRendezvous[10], singleRendezvous[12], singleRendezvous[13], singleRendezvous[14]));
            updatedRendezvous.add(new Rendezvous(tempProfUser, SimpleUser.getInstance(), singleRendezvous[2], singleRendezvous[3], singleRendezvous[1], singleRendezvous[8], new Basket(singleRendezvous[4], singleRendezvous[5], singleRendezvous[6], servicesArrayList), singleRendezvous[11]));
        }

        SimpleUser.getInstance().setRendezvous(updatedRendezvous);
        SimpleUser.getInstance().sortRendezvous();
        SimpleUserRendezvousAdapter.getInstance().updateData(SimpleUser.getInstance().getRendezvous());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loopFlag = 1;
    }

    private void sendNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("My notification2", "My notification2", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        String message = "Ένα ραντεβού σου ενημερώθηκε!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "My notification2");
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Νέα ειδοποίηση");
        builder.setContentText(message);
        builder.setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), SimpleUserMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
    }
}

