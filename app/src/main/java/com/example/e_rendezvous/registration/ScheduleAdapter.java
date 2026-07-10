package com.example.e_rendezvous.registration;

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
import java.util.ArrayList;
import java.util.List;

import models.DailySchedule;
import models.ProfUser;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private ArrayList<DailySchedule> dailySchedule;
    private String[] days;
    private int[] images;
    private int[] dayPositionChanged;
    private static int counter = 0;
    private static ScheduleAdapter currentAdapter = null;

    public static ScheduleAdapter getInstance(Context context, ArrayList<DailySchedule> dailySchedule, String[] days, int[] images){
        currentAdapter = new ScheduleAdapter(context, dailySchedule, days, images);
        return currentAdapter;
    }

    public static ScheduleAdapter getInstance(){
        if (currentAdapter == null){
            currentAdapter = new ScheduleAdapter();
        }
        return currentAdapter;
    }
    public ScheduleAdapter(){}
    public ScheduleAdapter(Context context, ArrayList<DailySchedule> dailySchedule, String[] days, int[] images){
        this.context = context;
        this.dailySchedule = dailySchedule;
        this.days = days;
        this.images = images;
        dayPositionChanged = new int[]{-1, -1, -1, -1, -1, -1, -1};
    }

    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.weekly_schedule_row, parent,false);

        return new ScheduleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {

        holder.dayName.setText(days[position]);
        holder.scheduleImage.setImageResource(images[position]);

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
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.dayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ifExists = -1;
                for(int i = 0; i < 7; i++) {
                    if (dayPositionChanged[i] == position) {
                        ifExists = 1;
                    }
                }
                if(ifExists == -1){
                    dayPositionChanged[counter] = position;
                    if(ScheduleAdapter.counter == 6){
                        ScheduleAdapter.counter = 0;
                    }else{
                        ScheduleAdapter.counter++;
                    }

                }

                Intent intent = new Intent(context, DailyScheduleRegistration.class);
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


    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView dayName;
        TextView scheduleType;
        TextView scheduleText;
        ImageView scheduleImage;
        CardView dayCardView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            scheduleType = itemView.findViewById(R.id.scheduleType);
            scheduleText = itemView.findViewById(R.id.scheduleText);
            scheduleImage = itemView.findViewById(R.id.scheduleImage);
            dayCardView = itemView.findViewById(R.id.dayCardView);
        }
    }
}


