package com.example.e_rendezvous.profUserActivities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_rendezvous.R;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import models.ProfUser;
import models.Rendezvous;

public class NotificationFragment extends Fragment {

    private RecyclerView rendezvousRecyclerView;
    private NotificationAdapter notificationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rendezvousRecyclerView = view.findViewById(R.id.rendezvousRecyclerView);

        notificationAdapter = new NotificationAdapter(view.getContext(), getUpcomingRendezvous());
        rendezvousRecyclerView.setAdapter(notificationAdapter);
        rendezvousRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Rendezvous> getUpcomingRendezvous(){
        ArrayList<Rendezvous> upcomingRendezvous = new ArrayList<>();
        for(int i=0; i<ProfUser.getInstance().getRendezvous().size(); i++){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate now = LocalDate.now();
            String[] date = ProfUser.getInstance().getRendezvous().get(i).getDate().split("/");
            String newDate = date[0] + "/" +  (Integer.parseInt(date[1])+1) + "/" + date[2];
            LocalDate tmp = LocalDate.parse(newDate, dtf);
            boolean test = tmp.isBefore(now);
            if(!test && ProfUser.getInstance().getRendezvous().get(i).getIsReserved().equals("false")){
                upcomingRendezvous.add(ProfUser.getInstance().getRendezvous().get(i));
            }
        }
        return upcomingRendezvous;
    }
}
