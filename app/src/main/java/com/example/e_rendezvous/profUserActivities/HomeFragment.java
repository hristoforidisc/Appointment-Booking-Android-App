package com.example.e_rendezvous.profUserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.e_rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import models.ProfUser;
import models.Rendezvous;


public class HomeFragment  extends Fragment implements OnNavigationButtonClickedListener{

    private CustomCalendar customCalendar;
    private Calendar calendar;
    private HashMap<Integer, Object> currentDay;
    private int today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        calendar = Calendar.getInstance();
        customCalendar = (CustomCalendar) getActivity().findViewById(R.id.custom_calendar);

        HashMap<Object, Property> mapDescToProp = new HashMap<>();
        Property propUnavailable = new Property();
        propUnavailable.layoutResource = R.layout.unavailable_view;
        propUnavailable.dateTextViewResource = R.id.text_view;
        propUnavailable.enable = false;
        mapDescToProp.put("unavailable", propUnavailable);

        //For curent date
        Property currentProp = new Property();
        currentProp.layoutResource = R.layout.current_view;
        currentProp.dateTextViewResource = R.id.text_view;
        mapDescToProp.put("current", currentProp);

        customCalendar.setMapDescToProp(mapDescToProp);

        currentDay = ProfUser.getInstance().getDaysWithRendezvous(calendar)[calendar.get(Calendar.MONTH)];
        today = calendar.get(Calendar.DAY_OF_MONTH);
        currentDay.put(today, "current");
        customCalendar.setDate(calendar, currentDay);


        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);


        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                Intent intent = new Intent(getActivity(), ProfUserDailyRendezvous.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        currentDay = ProfUser.getInstance().getDaysWithRendezvous(calendar)[calendar.get(Calendar.MONTH)];
        currentDay.put(today, "current");
        customCalendar.setDate(calendar, currentDay);
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
        Map<Integer, Object> temp = new HashMap<>();

        if(ProfUser.getInstance().getRendezvous().isEmpty()) {
            temp = ProfUser.getInstance().getDaysWithRendezvous(newMonth)[monthNumber];
        }else{
            for (int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
                if(newMonth.get(Calendar.YEAR) == ProfUser.getInstance().getRendezvous().get(i).getYear()){
                    temp = ProfUser.getInstance().getDaysWithRendezvous(newMonth)[monthNumber];
                }
            }
        }

        if(calendar.get(Calendar.MONTH) == monthNumber && calendar.get(Calendar.YEAR) == newMonth.get(Calendar.YEAR)){
            temp.put(today, "current");
        }

        return temp;
    }

}