package com.example.e_rendezvous.simpleUserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.Calendar;

import models.DailySchedule;
import models.ProfUser;
import models.Rendezvous;

public class ProfUserScheduleFreeHoursInfo extends AppCompatActivity {

    private String day;
    private Calendar date;
    private String rendezvousDate;
    private ArrayList<Rendezvous> rendezvous;
    private ListView hoursListView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_schedule_free_hours_info);

        backButton = findViewById(R.id.backButton);
        hoursListView = findViewById(R.id.hoursListView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();

        if (extra != null){
            day =  extra.getString("dayName");
            date =  (Calendar) extra.get("selectedDate");
        }

        rendezvousDate = date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR);

        rendezvous = new ArrayList<>();

        for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
            if (ProfUser.getInstance().getRendezvous().get(i).getDate().equals(rendezvousDate)) {
                rendezvous.add(ProfUser.getInstance().getRendezvous().get(i));
            }
        }

        sortRendezvous();

        DailySchedule workingDay = new DailySchedule();
        for(int j = 0; j < ProfUser.getInstance().getWeeklySchedule().size(); j++){
            if(ProfUser.getInstance().getWeeklySchedule().get(j).getDay().equals(day)){
                workingDay = ProfUser.getInstance().getWeeklySchedule().get(j);
                break;
            }
        }


        ArrayList<String> timeZones = new ArrayList<>();
        if(!rendezvous.isEmpty()) {

            ArrayList<Rendezvous> morningRendezvous = new ArrayList<>();
            ArrayList<Rendezvous> afternoonRendezvous = new ArrayList<>();
            int[] morningStart = findTime(workingDay.getMorningStartTime());
            int[] morningEnd = findTime(workingDay.getMorningEndTime());

            if (!workingDay.getAfternoonStartTime().equals("Not working")) {

                int[] afternoonStart = findTime(workingDay.getAfternoonStartTime());
                int[] afternoonEnd = findTime(workingDay.getAfternoonEndTime());

                for (int i = 0; i < rendezvous.size(); i++) {

                    int[] rendezvousStartTime = findTime(rendezvous.get(i).getStartTime());
                    int[] rendezvousEndTime = findTime((rendezvous.get(i).getEndTime()));

                    if(((rendezvousStartTime[0] > morningStart[0] || (rendezvousStartTime[0] == morningStart[0] && rendezvousStartTime[1] > morningStart[1])) && (rendezvousStartTime[0] < morningEnd[0] || (rendezvousStartTime[0] == morningEnd[0] && rendezvousStartTime[1] < morningEnd[1])))
                    || ((rendezvousEndTime[0] > morningStart[0] || (rendezvousEndTime[0] == morningStart[0] && rendezvousEndTime[1] > morningStart[1])) && (rendezvousEndTime[0] < morningEnd[0] || (rendezvousEndTime[0] == morningEnd[0] && rendezvousEndTime[1] < morningEnd[1])))
                    || ((rendezvousStartTime[0] > afternoonStart[0] || (rendezvousStartTime[0] == afternoonStart[0] && rendezvousStartTime[1] > afternoonStart[1])) && (rendezvousStartTime[0] < afternoonEnd[0] || (rendezvousStartTime[0] == afternoonEnd[0] && rendezvousStartTime[1] < afternoonEnd[1])))
                    || ((rendezvousEndTime[0] > afternoonStart[0] || (rendezvousEndTime[0] == afternoonStart[0] && rendezvousEndTime[1] > afternoonStart[1])) && (rendezvousEndTime[0] < afternoonEnd[0] || (rendezvousEndTime[0] == afternoonEnd[0] && rendezvousEndTime[1] < afternoonEnd[1])))) {

                        if ((rendezvousStartTime[0] > afternoonStart[0] || (rendezvousStartTime[0] == afternoonStart[0] && rendezvousStartTime[1] > afternoonStart[1])) || (rendezvousEndTime[0] > afternoonStart[0] || (rendezvousEndTime[0] == afternoonStart[0] && rendezvousEndTime[1] > afternoonStart[1]))) {

                            if (rendezvousEndTime[0] > afternoonEnd[0] || (rendezvousEndTime[0] == afternoonEnd[0] && rendezvousEndTime[1] > afternoonEnd[1])) {
                                rendezvous.get(i).setEndTime(workingDay.getAfternoonEndTime());
                            } else if ((rendezvousEndTime[0] > afternoonStart[0] || (rendezvousEndTime[0] == afternoonStart[0] && rendezvousEndTime[1] > afternoonStart[1])) && rendezvousStartTime[0] < afternoonStart[0] || (rendezvousStartTime[0] == afternoonStart[0] && rendezvousStartTime[1] < afternoonStart[1])) {
                                rendezvous.get(i).setStartTime(workingDay.getAfternoonStartTime());
                            }
                            afternoonRendezvous.add(rendezvous.get(i));

                        } else {

                            if (rendezvousEndTime[0] > morningEnd[0] || (rendezvousEndTime[0] == morningEnd[0] && rendezvousEndTime[1] > morningEnd[1])) {
                                rendezvous.get(i).setEndTime(workingDay.getMorningEndTime());
                            } else if ((rendezvousEndTime[0] > morningStart[0] || (rendezvousEndTime[0] == morningStart[0] && rendezvousEndTime[1] > morningStart[1])) && rendezvousStartTime[0] < morningStart[0] || (rendezvousStartTime[0] == morningStart[0] && rendezvousStartTime[1] < morningStart[1])) {
                                rendezvous.get(i).setStartTime(workingDay.getMorningStartTime());
                            }
                            morningRendezvous.add(rendezvous.get(i));
                        }
                    }
                }

                if(!morningRendezvous.isEmpty()) {
                    // FOR MORNING RENDEZVOUS
                    if (!workingDay.getMorningStartTime().equals(morningRendezvous.get(0).getStartTime())) {
                        timeZones.add(workingDay.getMorningStartTime() + " - " + morningRendezvous.get(0).getStartTime());
                    }
                    for (int i = 0; i < morningRendezvous.size() - 1; i++) {
                        if (!morningRendezvous.get(i).getEndTime().equals(morningRendezvous.get(i + 1).getStartTime())) {
                            timeZones.add(morningRendezvous.get(i).getEndTime() + " - " + morningRendezvous.get(i + 1).getStartTime());
                        }
                    }
                    if (!workingDay.getMorningEndTime().equals(morningRendezvous.get(morningRendezvous.size() - 1).getEndTime())) {
                        timeZones.add(morningRendezvous.get(morningRendezvous.size() - 1).getEndTime() + " - " + workingDay.getMorningEndTime());
                    }
                }else{
                    timeZones.add(workingDay.getMorningStartTime() + " - " + workingDay.getMorningEndTime());
                }
                if(!afternoonRendezvous.isEmpty()) {
                    // FOR AFTERNOON RENDEZVOUS
                    if (!workingDay.getAfternoonStartTime().equals(afternoonRendezvous.get(0).getStartTime())) {
                        timeZones.add(workingDay.getAfternoonStartTime() + " - " + afternoonRendezvous.get(0).getStartTime());
                    }
                    for (int i = 0; i < afternoonRendezvous.size() - 1; i++) {
                        if (!afternoonRendezvous.get(i).getEndTime().equals(afternoonRendezvous.get(i + 1).getStartTime())) {
                            timeZones.add(afternoonRendezvous.get(i).getEndTime() + " - " + afternoonRendezvous.get(i + 1).getStartTime());
                        }
                    }
                    if (!workingDay.getAfternoonEndTime().equals(afternoonRendezvous.get(afternoonRendezvous.size() - 1).getEndTime())) {
                        timeZones.add(afternoonRendezvous.get(afternoonRendezvous.size() - 1).getEndTime() + " - " + workingDay.getAfternoonEndTime());
                    }
                }else{
                    timeZones.add(workingDay.getAfternoonStartTime() + " - " + workingDay.getAfternoonEndTime());
                }

            } else {

                ArrayList<Rendezvous> allDayRendezvous = new ArrayList<>();

                for (int i = 0; i < rendezvous.size(); i++) {

                    int[] rendezvousStartTime = findTime(rendezvous.get(i).getStartTime());
                    int[] rendezvousEndTime = findTime((rendezvous.get(i).getEndTime()));

                    if (((rendezvousStartTime[0] > morningStart[0] || (rendezvousStartTime[0] == morningStart[0] && rendezvousStartTime[1] > morningStart[1])) && (rendezvousStartTime[0] < morningEnd[0] || (rendezvousStartTime[0] == morningEnd[0] && rendezvousStartTime[1] < morningEnd[1])))
                            || ((rendezvousEndTime[0] > morningStart[0] || (rendezvousEndTime[0] == morningStart[0] && rendezvousEndTime[1] > morningStart[1])) && (rendezvousEndTime[0] < morningEnd[0] || (rendezvousEndTime[0] == morningEnd[0] && rendezvousEndTime[1] < morningEnd[1])))) {

                        allDayRendezvous.add(rendezvous.get(i));
                    }
                }

                if(!allDayRendezvous.isEmpty()) {

                    int[] rendezvousStartTime = findTime(allDayRendezvous.get(0).getStartTime());
                    int[] rendezvousEndTime = findTime((allDayRendezvous.get(0).getEndTime()));

                    if ((rendezvousEndTime[0] > morningStart[0] || (rendezvousEndTime[0] == morningStart[0] && rendezvousEndTime[1] > morningStart[1])) && rendezvousStartTime[0] < morningStart[0] || (rendezvousStartTime[0] == morningStart[0] && rendezvousStartTime[1] < morningStart[1])) {
                        allDayRendezvous.get(0).setStartTime(workingDay.getMorningStartTime());
                    }
                    if (!workingDay.getMorningStartTime().equals(allDayRendezvous.get(0).getStartTime())) {
                        timeZones.add(workingDay.getMorningStartTime() + " - " + allDayRendezvous.get(0).getStartTime());
                    }

                    for (int i = 0; i < allDayRendezvous.size() - 1; i++) {
                        if (!allDayRendezvous.get(i).getEndTime().equals(allDayRendezvous.get(i + 1).getStartTime())) {
                            timeZones.add(allDayRendezvous.get(i).getEndTime() + " - " + allDayRendezvous.get(i + 1).getStartTime());
                        }
                    }

                    rendezvousEndTime = findTime((allDayRendezvous.get(allDayRendezvous.size() - 1).getEndTime()));
                    if (rendezvousEndTime[0] > morningEnd[0] || (rendezvousEndTime[0] == morningEnd[0] && rendezvousEndTime[1] > morningEnd[1])) {
                        allDayRendezvous.get(allDayRendezvous.size() - 1).setEndTime(workingDay.getMorningEndTime());
                    }
                    if (!workingDay.getMorningEndTime().equals(allDayRendezvous.get(allDayRendezvous.size() - 1).getEndTime())) {
                        timeZones.add(allDayRendezvous.get(allDayRendezvous.size() - 1).getEndTime() + " - " + workingDay.getMorningEndTime());
                    }
                }
            }

        }else{
            if(workingDay.getAfternoonStartTime().equals("Not working")){
                timeZones.add(workingDay.getMorningStartTime() + " - " + workingDay.getMorningEndTime());
            }else {
                timeZones.add(workingDay.getMorningStartTime() + " - " + workingDay.getMorningEndTime());
                timeZones.add(workingDay.getAfternoonStartTime() + " - " + workingDay.getAfternoonEndTime());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.hours_row, timeZones);
        hoursListView.setAdapter(adapter);
    }

    private int[] findTime(String time){

        String start[] = time.split(" : ");
        int startHour = Integer.parseInt(start[0]);
        int startMinute = Integer.parseInt(start[1]);
        int returnedTime[] = {startHour, startMinute};

        return returnedTime;
    }


    public void sortRendezvous(){

        for(int i = 0; i < rendezvous.size(); i++) {
            for(int j = i + 1; j < rendezvous.size(); j++) {
                String []datei = rendezvous.get(i).getDate().split("/");
                String []datej = rendezvous.get(j).getDate().split("/");
                if (Integer.parseInt(datei[2]) > Integer.parseInt(datej[2])) {
                    Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i).getDate(), rendezvous.get(i).getStartTime(), rendezvous.get(i).getEndTime());
                    rendezvous.set(i, rendezvous.get(j));
                    rendezvous.set(j, tempRendesvous);
                }else if (Integer.parseInt(datei[2]) == Integer.parseInt(datej[2])) {
                    if (Integer.parseInt(datei[1]) > Integer.parseInt(datej[1])){
                        Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i).getDate(), rendezvous.get(i).getStartTime(), rendezvous.get(i).getEndTime());
                        rendezvous.set(i, rendezvous.get(j));
                        rendezvous.set(j, tempRendesvous);
                    }else if(Integer.parseInt(datei[1]) == Integer.parseInt(datej[1])){
                        if (Integer.parseInt(datei[0]) > Integer.parseInt(datej[0])){
                            Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i).getDate(), rendezvous.get(i).getStartTime(), rendezvous.get(i).getEndTime());
                            rendezvous.set(i, rendezvous.get(j));
                            rendezvous.set(j, tempRendesvous);
                        }else if(Integer.parseInt(datei[0]) == Integer.parseInt(datej[0])){
                            String []startTimei = rendezvous.get(i).getStartTime().split(" : ");
                            String []startTimej = rendezvous.get(j).getStartTime().split(" : ");
                            if (Integer.parseInt(startTimei[0]) > Integer.parseInt(startTimej[0])) {
                                Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i).getDate(), rendezvous.get(i).getStartTime(), rendezvous.get(i).getEndTime());
                                rendezvous.set(i, rendezvous.get(j));
                                rendezvous.set(j, tempRendesvous);
                            }else if (Integer.parseInt(startTimei[0]) == Integer.parseInt(startTimej[0])) {
                                if (Integer.parseInt(startTimei[1]) > Integer.parseInt(startTimej[1])){
                                    Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i).getDate(), rendezvous.get(i).getStartTime(), rendezvous.get(i).getEndTime());
                                    rendezvous.set(i, rendezvous.get(j));
                                    rendezvous.set(j, tempRendesvous);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}