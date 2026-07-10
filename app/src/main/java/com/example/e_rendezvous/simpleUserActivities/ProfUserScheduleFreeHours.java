package com.example.e_rendezvous.simpleUserActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_rendezvous.R;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import models.ProfUser;

public class ProfUserScheduleFreeHours extends AppCompatActivity implements OnNavigationButtonClickedListener {

    private CustomCalendar customCalendar;
    private Calendar calendar;
    private HashMap<Integer, Object> currentDay;
    private ImageButton backButton;
    private int today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_schedule_free_hours);

        calendar = Calendar.getInstance();
        customCalendar = findViewById(R.id.custom_calendar);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HashMap<Object, Property> mapDescToProp = new HashMap<>();
        Property propUnavailable = new Property();
        propUnavailable.layoutResource = R.layout.not_working_view;
        propUnavailable.dateTextViewResource = R.id.text_view;
        propUnavailable.enable = false;
        mapDescToProp.put("unavailable", propUnavailable);

        //For curent date
        Property currentProp = new Property();
        currentProp.layoutResource = R.layout.current_view;
        currentProp.dateTextViewResource = R.id.text_view;
        mapDescToProp.put("current", currentProp);

        customCalendar.setMapDescToProp(mapDescToProp);

        // Gia ton trexon mina
        currentDay = ProfUser.getInstance().getUnavailableDays(calendar)[calendar.get(Calendar.MONTH)];
        today = calendar.get(Calendar.DAY_OF_MONTH);
        currentDay.put(today, "current");
        customCalendar.setDate(calendar, currentDay);


        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        ArrayList<Integer> profUserWorkingDays = new ArrayList<>();
        for(int i = 0; i < ProfUser.getInstance().getWeeklySchedule().size(); i++){
            switch (ProfUser.getInstance().getWeeklySchedule().get(i).getDay()) {
                case "Κυριακή":
                    profUserWorkingDays.add(1);
                    break;
                case "Δευτέρα":
                    profUserWorkingDays.add(2);
                    break;
                case "Τρίτη":
                    profUserWorkingDays.add(3);
                    break;
                case "Τετάρτη":
                    profUserWorkingDays.add(4);
                    break;
                case "Πέμπτη":
                    profUserWorkingDays.add(5);
                    break;
                case "Παρασκευή":
                    profUserWorkingDays.add(6);
                    break;
                case "Σάββατο":
                    profUserWorkingDays.add(7);
                    break;
            }
        }

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String daySelected = "";
                switch (selectedDate.get(Calendar.DAY_OF_WEEK)) {
                    case 1:
                        daySelected = "Κυριακή";
                        break;
                    case 2:
                        daySelected = "Δευτέρα";
                        break;
                    case 3:
                        daySelected = "Τρίτη";
                        break;
                    case 4:
                        daySelected = "Τετάρτη";
                        break;
                    case 5:
                        daySelected = "Πέμπτη";
                        break;
                    case 6:
                        daySelected = "Παρασκευή";
                        break;
                    case 7:
                        daySelected = "Σάββατο";
                        break;
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate now = LocalDate.now();
                String newDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" +  (selectedDate.get(Calendar.MONTH)+1) + "/" + selectedDate.get(Calendar.YEAR);
                LocalDate tmp = LocalDate.parse(newDate, dtf);

                if(!profUserWorkingDays.contains(selectedDate.get(Calendar.DAY_OF_WEEK))) {
                    Toast.makeText(getApplicationContext(), "Η ημέρα που διάλεξες δεν είναι εργάσιμη για τον τρέχον επαγγελματία!", Toast.LENGTH_SHORT).show();

                }else if(tmp.isBefore(now)){
                    Toast.makeText(getApplicationContext(), "Δεν επιτρέπεται να κλείσεις ραντεβού σε περασμένη ημερομηνία!",  Toast.LENGTH_LONG).show();
                }else{
                    if(daySelected != "") {
                        Intent intent = new Intent(getApplicationContext(), ProfUserScheduleFreeHoursInfo.class);
                        intent.putExtra("selectedDate", selectedDate);
                        intent.putExtra("dayName", daySelected);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();
        arr[1] = new HashMap<>();


        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                arr[0] = monthHelper(0, newMonth);
                break;
            case Calendar.FEBRUARY:
                arr[0] = monthHelper(1, newMonth);
                break;
            case Calendar.MARCH:
                arr[0] = monthHelper(2, newMonth);
                break;
            case Calendar.APRIL:
                arr[0] = monthHelper(3, newMonth);
                break;
            case Calendar.MAY:
                arr[0] = monthHelper(4, newMonth);
                break;
            case Calendar.JUNE:
                arr[0] = monthHelper(5, newMonth);
                break;
            case Calendar.JULY:
                arr[0] = monthHelper(6, newMonth);
                break;
            case Calendar.AUGUST:
                arr[0] = monthHelper(7, newMonth);
                break;
            case Calendar.SEPTEMBER:
                arr[0] = monthHelper(8, newMonth);
                break;
            case Calendar.OCTOBER:
                arr[0] = monthHelper(9, newMonth);
                break;
            case Calendar.NOVEMBER:
                arr[0] = monthHelper(10, newMonth);
                break;

            case Calendar.DECEMBER:
                arr[0] = monthHelper(11, newMonth);
                break;
        }
        return arr;
    }


    private Map<Integer, Object> monthHelper(int monthNumber, Calendar newMonth){
        Map<Integer, Object> temp = ProfUser.getInstance().getUnavailableDays(newMonth)[monthNumber];

        if(calendar.get(Calendar.MONTH) == monthNumber && calendar.get(Calendar.YEAR) == newMonth.get(Calendar.YEAR)){
            temp.put(today, "current");
        }
        return temp;
    }
}