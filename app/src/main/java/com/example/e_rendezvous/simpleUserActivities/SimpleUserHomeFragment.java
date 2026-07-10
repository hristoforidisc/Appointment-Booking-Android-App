package com.example.e_rendezvous.simpleUserActivities;


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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import models.Rendezvous;
import models.SimpleUser;

public class SimpleUserHomeFragment extends Fragment {

    private RecyclerView rendezvousRecyclerView;
    private SimpleUserRendezvousAdapter rendezvousAdapter;
    private FloatingActionButton searchRendezvou;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_user_home_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchRendezvou = view.findViewById(R.id.searchRendezvou);
        rendezvousRecyclerView = view.findViewById(R.id.rendezvousRecyclerView);

        rendezvousAdapter = SimpleUserRendezvousAdapter.getInstance(view.getContext(), getUpcomingRendezvous());
        rendezvousRecyclerView.setAdapter(rendezvousAdapter);
        rendezvousRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchRendezvou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SearchFragment()).commit();
                transaction.addToBackStack(null);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        rendezvousAdapter.updateData(getUpcomingRendezvous());
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Rendezvous> getUpcomingRendezvous(){
        ArrayList<Rendezvous> upcomingRendezvous = new ArrayList<>();
        for(int i=0; i<SimpleUser.getInstance().getRendezvous().size(); i++){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate now = LocalDate.now();
            String[] date = SimpleUser.getInstance().getRendezvous().get(i).getDate().split("/");
            String newDate = date[0] + "/" +  (Integer.parseInt(date[1])+1) + "/" + date[2];
            LocalDate tmp = LocalDate.parse(newDate, dtf);
            boolean test = tmp.isBefore(now);
            if(!test){
                upcomingRendezvous.add(SimpleUser.getInstance().getRendezvous().get(i));
            }
        }
        return upcomingRendezvous;
    }
}
