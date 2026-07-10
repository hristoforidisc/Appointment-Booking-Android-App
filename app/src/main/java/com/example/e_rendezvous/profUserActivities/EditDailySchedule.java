package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.e_rendezvous.R;

import java.util.ArrayList;

import dbengine.RegistrationEngine;
import models.DailySchedule;
import models.ProfUser;

public class EditDailySchedule extends AppCompatActivity {

    private Button morningStartTimeButton, morningEndTimeButton, afternoonStartTimeButton, afternoonEndTimeButton, finishDailySchedule, notWorking;
    private RadioGroup radioButtons;
    private RadioButton fullSchedule, breakedSchedule;
    private TextView morningTextView, afternoonTextView, currentDay;
    private String morningStartTime, morningEndTime, afternoonStartTime, afternoonEndTime, day;
    private ImageButton backButton;
    private int t2Hour, t2Minute, buttonFlag, dayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_schedule);

        notWorking = findViewById(R.id.notWorking);
        backButton = findViewById(R.id.backButton);
        morningStartTimeButton = findViewById(R.id.morningStartTime);
        morningEndTimeButton = findViewById(R.id.morningEndTime);
        afternoonStartTimeButton = findViewById(R.id.afternoonStartTime);
        afternoonEndTimeButton = findViewById(R.id.afternoonEndTime);
        finishDailySchedule = findViewById(R.id.finishDailySchedule);
        morningTextView = findViewById(R.id.morningTextView);
        afternoonTextView = findViewById(R.id.afternoonTextView);
        currentDay = findViewById(R.id.currentDay);
        radioButtons = findViewById(R.id.radioButtons);
        fullSchedule = findViewById(R.id.fullSchedule);
        breakedSchedule = findViewById(R.id.breakedSchedule);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(fullSchedule.isPressed()){
                    morningTextView.setText("Επιλογή ενιαίου ωραρίου");
                    afternoonTextView.setVisibility(View.GONE);
                    afternoonStartTimeButton.setVisibility(View.GONE);
                    afternoonEndTimeButton.setVisibility(View.GONE);
                }
                else {
                    morningTextView.setText("Επιλογή πρωινού ωραρίου");
                    afternoonTextView.setText("Επιλογή απογευματινού ωραρίου");
                    afternoonTextView.setVisibility(View.VISIBLE);
                    afternoonStartTimeButton.setVisibility(View.VISIBLE);
                    afternoonEndTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            day = (String) extras.get("day");
            dayPosition = (int) extras.get("pos");
        }

        currentDay.setText(day);

        notWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int isWorkingDay = -1;
                for(int i = 0; i < ProfUser.getInstance().getWeeklySchedule().size(); i++){
                    if(day.equals(ProfUser.getInstance().getWeeklySchedule().get(i).getDay())){
                        isWorkingDay = 1;
                        String result = RegistrationEngine.getInstance().deleteDailySchedule(day);
                        if(result.equals("DailyScheduleDeleteSuccess")) {
                            Toast.makeText(getApplicationContext(), "Η ημέρα " + day + " είναι πλέον μη εργάσιμη!", Toast.LENGTH_SHORT).show();
                            EditScheduleAdapter.getInstance().getDayPositionChanged(day);
                            ProfUser.getInstance().getWeeklySchedule().remove(i);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(isWorkingDay == -1){
                    Toast.makeText(getApplicationContext(), "Η ημέρα " + day + " δεν είναι εργάσιμη!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        for(int i = 0; i< ProfUser.getInstance().getWeeklySchedule().size(); i++){
            if(day.equals(ProfUser.getInstance().getWeeklySchedule().get(i).getDay())){
                DailySchedule currentDay = ProfUser.getInstance().getWeeklySchedule().get(i);
                if (currentDay.getAfternoonStartTime().equals("Not working")) {
                    fullSchedule.setChecked(true);
                    morningStartTime = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime();
                    morningEndTime = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime();
                    morningStartTimeButton.setText(getTimeString(morningStartTime));
                    morningEndTimeButton.setText(getTimeString(morningEndTime));
                } else if (!currentDay.getAfternoonStartTime().equals("Not working") && !currentDay.getAfternoonStartTime().equals("")){
                    breakedSchedule.setChecked(true);
                    morningStartTime = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime();
                    morningEndTime = ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime();
                    afternoonStartTime = ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonStartTime();
                    afternoonEndTime = ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonEndTime();

                    morningStartTimeButton.setText(getTimeString(morningStartTime));
                    morningEndTimeButton.setText(getTimeString(morningEndTime));
                    afternoonStartTimeButton.setText(getTimeString(afternoonStartTime));
                    afternoonEndTimeButton.setText(getTimeString(afternoonEndTime));
                }
            }
        }

        morningStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFlag = 0;
                timePopup(v);
            }
        });

        morningEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFlag = 1;
                timePopup(v);
            }
        });

        afternoonStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFlag = 2;
                timePopup(v);
            }
        });

        afternoonEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFlag = 3;
                timePopup(v);
            }
        });

        finishDailySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullSchedule.isChecked()){
                    afternoonStartTimeButton.setText("Έναρξη");
                    afternoonEndTimeButton.setText("Λήξη");

                    if(!morningStartTimeButton.getText().equals("Έναρξη") && !morningEndTimeButton.getText().equals("Λήξη")
                            && afternoonStartTimeButton.getText().equals("Έναρξη") && afternoonEndTimeButton.getText().equals("Λήξη")){

                        afternoonStartTime = "Not working";
                        afternoonEndTime = "Not working";
                        updateSchedule();
                        EditScheduleAdapter.getInstance().notifyItemChanged(dayPosition);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Συμπλήρωσε το ωράριο εργασίας", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(!morningStartTimeButton.getText().equals("Έναρξη") && !morningEndTimeButton.getText().equals("Λήξη")
                            && !afternoonStartTimeButton.getText().equals("Έναρξη") && !afternoonEndTimeButton.getText().equals("Λήξη")){

                        updateSchedule();
                        EditScheduleAdapter.getInstance().notifyItemChanged(dayPosition);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Συμπλήρωσε το ωράριο εργασίας", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateSchedule(){

        ArrayList<DailySchedule> weeklySchedule = ProfUser.getInstance().getWeeklySchedule();

        if(weeklySchedule.isEmpty()){
            weeklySchedule.add(new DailySchedule(day, morningStartTime, morningEndTime, afternoonStartTime, afternoonEndTime));
        } else {
            int flagDayExists = -1;
            for(int i = 0; i < weeklySchedule.size(); i++) {
                if (day.equals(weeklySchedule.get(i).getDay())) {
                    flagDayExists = i;
                    break;
                }
            }
            if (flagDayExists == -1){
                weeklySchedule.add(new DailySchedule(day, morningStartTime, morningEndTime, afternoonStartTime, afternoonEndTime));
            } else {
                weeklySchedule.set(flagDayExists, new DailySchedule(day, morningStartTime, morningEndTime, afternoonStartTime, afternoonEndTime));
            }
        }
        ProfUser.getInstance().setWeeklySchedule(weeklySchedule);
    }

    private void timePopup(View v){
        TimePickerDialog newFragment = new TimePickerDialog(EditDailySchedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                t2Hour = hourOfDay;
                t2Minute = minute;

                if(buttonFlag == 0){
                    morningStartTime = getTimeString(t2Hour + " : " + t2Minute);
                    morningStartTimeButton.setText(morningStartTime);
                }
                else if(buttonFlag == 1){
                    morningEndTime = getTimeString(t2Hour + " : " + t2Minute);
                    morningEndTimeButton.setText(morningEndTime);
                }
                else if(buttonFlag == 2){
                    afternoonStartTime = getTimeString(t2Hour + " : " + t2Minute);
                    afternoonStartTimeButton.setText(afternoonStartTime);
                }
                else {
                    afternoonEndTime = getTimeString(t2Hour + " : " + t2Minute);
                    afternoonEndTimeButton.setText(afternoonEndTime);
                }
            }
        },0,0,true);
        newFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newFragment.updateTime(t2Hour, t2Minute);
        newFragment.show();
    }


    public String getTimeString(String inputTime){

        String[] splittedInput = inputTime.split(" : ");

        int hour = Integer.parseInt(splittedInput[0]);
        int minute =  Integer.parseInt(splittedInput[1]);

        if(hour < 10){
            if(minute < 10){
                return "0" + hour + " : " + "0" + minute;
            } else {
                return "0" + hour + " : " + minute;
            }
        } else {
            if(minute<10){
                return hour + " : " + "0" + minute;
            } else {
                return hour + " : " + minute;
            }
        }
    }
}