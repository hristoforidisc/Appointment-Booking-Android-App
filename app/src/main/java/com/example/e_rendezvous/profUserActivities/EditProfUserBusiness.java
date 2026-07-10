package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.e_rendezvous.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.util.ArrayList;
import dbengine.RegistrationEngine;
import models.Business;
import models.ProfUser;

public class EditProfUserBusiness extends AppCompatActivity {

    private Button businessSignupButton;
    private EditText businessNameText, businessAddressText, businessPostCodeText, businessPhoneText;
    private SearchableSpinner businessTypeSpinner, typeSpecializationSpinner, businessCitySpinner;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof_user_business);

        businessSignupButton = findViewById(R.id.BusinessSignupButton);
        businessNameText = findViewById(R.id.BusinessNameText);
        businessAddressText = findViewById(R.id.BusinessAddressText);
        businessPostCodeText = findViewById(R.id.PostCodeText);
        businessPhoneText = findViewById(R.id.BusinessPhone);
        businessCitySpinner = findViewById(R.id.CitySpinner);
        businessTypeSpinner = findViewById(R.id.BusinessTypeSpinner);
        typeSpecializationSpinner = findViewById(R.id.TypeSpecializationSpinner);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedCategory = typeSpecializationSpinner.getSelectedItem().toString();
                if(!selectedCategory.equals(ProfUser.getInstance().getBusiness().getTypeSpecialization()) || !businessTypeSpinner.getSelectedItem().toString().equals(ProfUser.getInstance().getBusiness().getBusinessType())) {
                    findBusinessCategory(businessTypeSpinner.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // City spinner setup, setting client's city as first option
        String[] allCities = getResources().getStringArray(R.array.Cities);
        ArrayList<String> temp = new ArrayList<>();
        temp.add(ProfUser.getInstance().getBusiness().getBusinessCity());
        for(int i = 1; i < allCities.length; i++){
            if(!allCities[i].equals(ProfUser.getInstance().getBusiness().getBusinessCity())) {
                temp.add(allCities[i]);
            }
        }
        businessCitySpinner.setAdapter(new ArrayAdapter<String>(EditProfUserBusiness.this, android.R.layout.simple_spinner_dropdown_item, temp));

        String[] allTypes = getResources().getStringArray(R.array.BusinessType);
        ArrayList<String> temp2 = new ArrayList<>();
        temp2.add(ProfUser.getInstance().getBusiness().getBusinessType());
        for(int i = 1; i < allTypes.length; i++){
            if(!allTypes[i].equals(ProfUser.getInstance().getBusiness().getBusinessType())) {
                temp2.add(allTypes[i]);
            }
        }
        businessTypeSpinner.setAdapter(new ArrayAdapter<String>(EditProfUserBusiness.this, android.R.layout.simple_spinner_dropdown_item, temp2));


        String[] allSpecialization = findBusinessCategoryForReturn(ProfUser.getInstance().getBusiness().getBusinessType());
        if(allSpecialization != null) {
            ArrayList<String> temp3 = new ArrayList<>();
            temp3.add(ProfUser.getInstance().getBusiness().getTypeSpecialization());
            for (int i = 1; i < allSpecialization.length; i++) {
                if (!allSpecialization[i].equals(ProfUser.getInstance().getBusiness().getTypeSpecialization())) {
                    temp3.add(allSpecialization[i]);
                }
            }
            setBusinessTypeSpinnerWithList(temp3);
        }

        businessNameText.setText(ProfUser.getInstance().getBusiness().getBusinessName());
        businessAddressText.setText(ProfUser.getInstance().getBusiness().getBusinessAddress());
        businessPostCodeText.setText(ProfUser.getInstance().getBusiness().getBusinessPostCode());
        businessPhoneText.setText(ProfUser.getInstance().getBusiness().getBusinessPhone());

        businessSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String businessName, businessType, typeSpecialization, businessCity,
                                businessAddress, businessPostCode, businessPhone;


                        businessName = String.valueOf(businessNameText.getText());
                        businessType = String.valueOf(businessTypeSpinner.getSelectedItem());
                        typeSpecialization = String.valueOf(typeSpecializationSpinner.getSelectedItem());
                        businessCity = String.valueOf(businessCitySpinner.getSelectedItem());
                        businessAddress = String.valueOf(businessAddressText.getText());
                        businessPostCode = String.valueOf(businessPostCodeText.getText());
                        businessPhone = String.valueOf(businessPhoneText.getText());

                        Business business = new Business(businessName, businessType, typeSpecialization, businessCity,
                                businessAddress, businessPostCode, businessPhone);

                        String result = RegistrationEngine.getInstance().updateProfUserBusiness(business);

                        if (result.equals("Success")) {
                            ProfUser.getInstance().getBusiness().setUpdatedBusiness(business);
                            System.out.println(ProfUser.getInstance().getBusiness().toString());
                            Toast.makeText(getApplicationContext(), "Επιτυχής ενημέρωση στοιχείων.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Όλα τα πεδία είναι απαραίτητα!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private String[] findBusinessCategoryForReturn(String profCategory){
        switch(profCategory) {
            case "Αθλητισμός":
                return getResources().getStringArray(R.array.Αθλητισμός);
            case "Κατασκευαστικός Κλάδος":
                return getResources().getStringArray(R.array.Κατασκευαστικός_Κλάδος);
            case "Λογιστικά Επαγγέλματα":
                return getResources().getStringArray(R.array.Λογιστικά_Επαγγέλματα);
            case "Μarketing":
                return getResources().getStringArray(R.array.Μarketing_Διαφήμιση_και_Δημόσιες_Σχέσεις);
            case "Μηχανολογικά Επαγγέλματα":
                return getResources().getStringArray(R.array.Μηχανολογικά_Επαγγέλματα);
            case "Νομικός Κλάδος":
                return getResources().getStringArray(R.array.Νομικός_Κλάδος);
            case "Επαγγέλματα Αισθητικής":
                return getResources().getStringArray(R.array.Επαγγέλματα_Αισθητικής);
            case "Τεχνολογία Πληροφορικής και Ηλεκτρολογία":
                return getResources().getStringArray(R.array.Τεχνολογία_Πληροφορικής);
            case "Τομέας Υγείας":
                return getResources().getStringArray(R.array.Τομέας_Υγείας);
            case "Επαγγέλματα Ψυχολογίας, Συμβουλευτικής και Προσανατολισμού":
                return getResources().getStringArray(R.array.Επαγγέλματα_Ψυχολογίας);
            default:
                Toast.makeText(getApplicationContext(), "Παρακαλώ επιλέξτε επαγγελματική κατηγορία ", Toast.LENGTH_SHORT).show();
        }
        return null;
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
            case "Τεχνολογία Πληροφορικής και Ηλεκτρολογία":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Τεχνολογία_Πληροφορικής));
                break;
            case "Τομέας Υγείας":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Τομέας_Υγείας));
                break;
            case "Επαγγέλματα Ψυχολογίας, Συμβουλευτικής και Προσανατολισμού":
                setBusinessTypeSpinner(getResources().getStringArray(R.array.Επαγγέλματα_Ψυχολογίας));
                break;
            default:
                Toast.makeText(getApplicationContext(), "Παρακαλώ επιλέξτε επαγγελματική κατηγορία ", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBusinessTypeSpinner(String[] array){
        typeSpecializationSpinner.setAdapter(new ArrayAdapter<String>(EditProfUserBusiness.this,
                android.R.layout.simple_spinner_dropdown_item,
                array));
    }

    private void setBusinessTypeSpinnerWithList(ArrayList<String> array){
        typeSpecializationSpinner.setAdapter(new ArrayAdapter<String>(EditProfUserBusiness.this,
                android.R.layout.simple_spinner_dropdown_item,
                array));
    }
}