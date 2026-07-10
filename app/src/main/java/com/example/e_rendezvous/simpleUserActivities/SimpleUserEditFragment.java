package com.example.e_rendezvous.simpleUserActivities;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_rendezvous.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import dbengine.RegistrationEngine;
import models.SimpleUser;

public class SimpleUserEditFragment extends Fragment {

    private EditText simpleUserUsername, simpleUserPassword, simpleUserEmail, simpleUserName, simpleUserPhone;
    private Button simpleUserSignupButton;
    private SearchableSpinner simpleUserCitySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_user_fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        simpleUserUsername = view.findViewById(R.id.username);
        simpleUserPassword = view.findViewById(R.id.password);
        simpleUserEmail = view.findViewById(R.id.EmailTextField);
        simpleUserName = view.findViewById(R.id.NameTextField);
        simpleUserPhone = view.findViewById(R.id.PhoneTextField);
        simpleUserCitySpinner = (SearchableSpinner) view.findViewById(R.id.CitySpinner);
        simpleUserSignupButton = view.findViewById(R.id.SimpleUserSignupButton);

        simpleUserPassword.setText(SimpleUser.getInstance().getPassword());
        simpleUserEmail.setText(SimpleUser.getInstance().getEmail());
        simpleUserName.setText(SimpleUser.getInstance().getName());
        simpleUserUsername.setText(SimpleUser.getInstance().getUsername());
        simpleUserPhone.setText(SimpleUser.getInstance().getPhone());

        // City spinner setup, setting client's city as first option
        String[] allCities = getResources().getStringArray(R.array.Cities);
        ArrayList<String> temp = new ArrayList<>();
        temp.add(SimpleUser.getInstance().getCity());
        for(int i = 1; i < allCities.length; i++){
            if(!allCities[i].equals(SimpleUser.getInstance().getCity())) {
                temp.add(allCities[i]);
            }
        }

        simpleUserCitySpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, temp));


        simpleUserSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String username, password, email, name, phone, city;
                        username = String.valueOf(simpleUserUsername.getText());
                        password = String.valueOf(simpleUserPassword.getText());
                        email = String.valueOf(simpleUserEmail.getText());
                        phone = String.valueOf(simpleUserPhone.getText());
                        name = String.valueOf(simpleUserName.getText());
                        city = simpleUserCitySpinner.getSelectedItem().toString();

                        SimpleUser simpleUser = new SimpleUser(username, password, email, phone, name, city);


                        String result = RegistrationEngine.getInstance().updateSimpleUserProfile(simpleUser);

                        if(result.equals("Update Success")) {
                            SimpleUser.getInstance().updateProfile(simpleUser);
                            Toast.makeText(getContext(), "Τα δεδομένα έχουν ενημερωθεί!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
