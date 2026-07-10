package com.example.e_rendezvous.simpleUserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.registration.BusinessRegistration;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import dbengine.RegistrationEngine;
import models.SimpleUser;

public class SearchBusinessSpecialist extends AppCompatActivity {

    private SearchableSpinner typeSpecializationSpinner, CitySpinner;
    private String businessType;
    private ImageButton backButton;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_business_specialist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            businessType = (String) extras.get("bysinessType");
        }

        typeSpecializationSpinner = findViewById(R.id.TypeSpecializationSpinner);
        CitySpinner = findViewById(R.id.CitySpinner);
        backButton = findViewById(R.id.backButton);
        searchButton = findViewById(R.id.searchButton);

        findBusinessCategory(businessType);

        // City spinner setup, setting client's city as first option
        String[] allCities = getResources().getStringArray(R.array.Cities);
        ArrayList<String> temp = new ArrayList<>();
        temp.add(SimpleUser.getInstance().getCity());

        for(int i = 1; i < allCities.length; i++){
            if(!allCities[i].equals(SimpleUser.getInstance().getCity())) {
                temp.add(allCities[i]);
            }
        }

        CitySpinner.setAdapter(new ArrayAdapter<String>(SearchBusinessSpecialist.this, android.R.layout.simple_spinner_dropdown_item, temp));


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(typeSpecializationSpinner.getSelectedItem()).equals("Επέλεξε Ειδικότητα")){
                    Intent intent = new Intent(getApplicationContext(), SearchResultsMainActivity.class);
                    intent.putExtra("result", RegistrationEngine.getInstance().searchProfs(String.valueOf(typeSpecializationSpinner.getSelectedItem()), String.valueOf(CitySpinner.getSelectedItem())));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Επέλεξε την ειδικότητα του επαγγελματία που ψάχνεις!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findBusinessCategory(String profCategory){
        switch(profCategory) {
            case "Αθλητισμός":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Αθλητισμός));
                break;
            case "Κατασκευαστικός Κλάδος":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Κατασκευαστικός_Κλάδος));
                break;
            case "Λογιστικά Επαγγέλματα":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Λογιστικά_Επαγγέλματα));
                break;
            case "Μarketing":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Μarketing_Διαφήμιση_και_Δημόσιες_Σχέσεις));
                break;
            case "Μηχανολογικά Επαγγέλματα":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Μηχανολογικά_Επαγγέλματα));
                break;
            case "Νομικός Κλάδος":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Νομικός_Κλάδος));
                break;
            case "Επαγγέλματα Αισθητικής":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Επαγγέλματα_Αισθητικής));
                break;
            case "Τεχνολογία Πληροφορικής":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Τεχνολογία_Πληροφορικής));
                break;
            case "Τομέας Υγείας":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Τομέας_Υγείας));
                break;
            case "Επαγγέλματα Ψυχολογίας":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Επαγγέλματα_Ψυχολογίας));
                break;
            default:
                Toast.makeText(getApplicationContext(), "Παρακαλώ επιλέξτε επαγγελματική κατηγορία ", Toast.LENGTH_SHORT).show();
        }

    }

    private void setBusinessTypeSpinner(String[] array){
        typeSpecializationSpinner.setAdapter(new ArrayAdapter<String>(SearchBusinessSpecialist.this,
                android.R.layout.simple_spinner_dropdown_item,
                array));
    }
}