package com.example.e_rendezvous.profUserActivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.registration.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

import models.DailySchedule;
import models.ProfUser;

public class EditScheduleAdapter extends RecyclerView.Adapter<EditScheduleAdapter.EditScheduleAdapterViewHolder> {

    private Context context;
    private ArrayList<DailySchedule> dailySchedule;
    private String[] days;
    private int[] images;
    private int[] dayPositionChanged;
    private static int counter = 0;
    private static EditScheduleAdapter currentAdapter = null;
    private int reset = 0;

    public EditScheduleAdapter(){}

    public static EditScheduleAdapter getInstance(Context context, ArrayList<DailySchedule> dailySchedule, String[] days, int[] images){
        currentAdapter = new EditScheduleAdapter(context, dailySchedule, days, images);
        return currentAdapter;
    }

    public static EditScheduleAdapter getInstance(){
        if (currentAdapter == null){
            currentAdapter = new EditScheduleAdapter();
        }
        return currentAdapter;
    }

    public EditScheduleAdapter(Context context, ArrayList<DailySchedule> dailySchedule, String[] days, int[] images){
        this.context = context;
        this.dailySchedule = dailySchedule;
        this.days = days;
        this.images = images;
        dayPositionChanged = new int[]{-1, -1, -1, -1, -1, -1, -1};
        for(int i = 0; i < dailySchedule.size(); i++){
            switch (dailySchedule.get(i).getDay()){
                case "Δευτέρα":
                    dayPositionChanged[i] = 0;
                    break;
                case "Τρίτη":
                    dayPositionChanged[i] = 1;
                    break;
                case "Τετάρτη":
                    dayPositionChanged[i] = 2;
                    break;
                case "Πέμπτη":
                    dayPositionChanged[i] = 3;
                    break;
                case "Παρασκευή":
                    dayPositionChanged[i] = 4;
                    break;
                case "Σάββατο":
                    dayPositionChanged[i] = 5;
                    break;
                case "Κυριακή":
                    dayPositionChanged[i] = 6;
                    break;
            }
        }
    }

    public void getDayPositionChanged(String day){
        reset = 1;

        switch (day) {
            case "Δευτέρα":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 0) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(0);
                    }
                }
                break;
            case "Τρίτη":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 1) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(1);
                    }
                }
                break;
            case "Τετάρτη":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 2) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(2);
                    }
                }
                break;
            case "Πέμπτη":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 3) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(3);
                    }
                }
                break;
            case "Παρασκευή":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 4) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(4);
                    }
                }
                break;
            case "Σάββατο":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 5) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(5);
                    }
                }
                break;
            case "Κυριακή":
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == 6) {
                        dayPositionChanged[i] = -1;
                        notifyItemChanged(6);
                    }
                }
                break;
        }
    }

    public EditScheduleAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.weekly_schedule_row, parent,false);

        return new EditScheduleAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EditScheduleAdapterViewHolder holder, int position) {

        holder.dayName.setText(days[position]);
        holder.scheduleImage.setImageResource(images[position]);

        if(reset == 1){
            holder.scheduleType.setText("");
            holder.scheduleText.setText("");
        }

        for(int i = 0; i < 7; i++){
            if (dayPositionChanged[i] == position) {
                if(ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonStartTime().equals("Not working")){
                    holder.scheduleType.setText("Ενιαίο ωράριο");
                    holder.scheduleText.setText(ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime() + " - " + ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime());
                } else {
                    holder.scheduleType.setText("Σπαστό ωράριο");
                    holder.scheduleText.setText("Πρωί: " + ProfUser.getInstance().getWeeklySchedule().get(i).getMorningStartTime() + " - " + ProfUser.getInstance().getWeeklySchedule().get(i).getMorningEndTime() + "\n"
                            + "Απόγευμα: " + ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonStartTime() + " - " + ProfUser.getInstance().getWeeklySchedule().get(i).getAfternoonEndTime());
                }
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull EditScheduleAdapterViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.dayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset = 0;
                int ifExists = -1;
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == position) {
                        ifExists = 1;
                    }
                }
                if(ifExists == -1){
                    counter = ProfUser.getInstance().getWeeklySchedule().size();
                    dayPositionChanged[counter] = position;
                }

                Intent intent = new Intent(context, EditDailySchedule.class);
                intent.putExtra("day", days[position]);
                intent.putExtra("pos", position);
                context.startActivity(intent);
                context.stopService(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class EditScheduleAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView dayName;
        TextView scheduleType;
        TextView scheduleText;
        ImageView scheduleImage;
        CardView dayCardView;

        public EditScheduleAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            scheduleType = itemView.findViewById(R.id.scheduleType);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            scheduleImage = itemView.findViewById(R.id.scheduleImage);
            dayCardView = itemView.findViewById(R.id.dayCardView);
        }
    }
}


