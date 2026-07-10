package com.example.e_rendezvous.profUserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_rendezvous.R;

import org.jetbrains.annotations.NotNull;

public class EditFragment extends Fragment {

    private Button profileButton, businessButton, groupsServicesButton, scheduleButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        profileButton = view.findViewById(R.id.profileButton);
        businessButton = view.findViewById(R.id.businessButton);
        groupsServicesButton = view.findViewById(R.id.groupsServicesButton);
        scheduleButton = view.findViewById(R.id.scheduleButton);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfUserProfile.class);
                startActivity(intent);
            }
        });

        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfUserBusiness.class);
                startActivity(intent);
            }
        });

        groupsServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfUserGroupsServices.class);
                startActivity(intent);
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfUserSchedule.class);
                startActivity(intent);
            }
        });

    }
}
