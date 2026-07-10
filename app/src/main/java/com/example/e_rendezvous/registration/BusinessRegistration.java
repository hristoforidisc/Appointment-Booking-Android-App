package com.example.e_rendezvous.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.e_rendezvous.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.io.UnsupportedEncodingException;
import dbengine.RegistrationEngine;
import models.Business;
import models.ProfUser;

public class BusinessRegistration extends AppCompatActivity {

    private Button businessSignupButton;
    private EditText businessNameText, businessAddressText, businessPostCodeText, businessPhoneText;
    private SearchableSpinner businessTypeSpinner, typeSpecializationSpinner, businessCitySpinner;
    private ProgressDialog progressDialog;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BusinessRegistration.this);
            builder.setTitle("Τερματισμός εφαρμογής");
            builder.setMessage("Πού πας; Θέλεις σίγουρα να φύγεις;");
            builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Όχι", null);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_registration);

        businessSignupButton = findViewById(R.id.BusinessSignupButton);
        businessNameText = findViewById(R.id.BusinessNameText);
        businessAddressText = findViewById(R.id.BusinessAddressText);
        businessPostCodeText = findViewById(R.id.PostCodeText);
        businessPhoneText = findViewById(R.id.BusinessPhone);
        businessCitySpinner = findViewById(R.id.CitySpinner);
        businessTypeSpinner = findViewById(R.id.BusinessTypeSpinner);
        typeSpecializationSpinner = findViewById(R.id.TypeSpecializationSpinner);


        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = businessTypeSpinner.getSelectedItem().toString();
                if (!selectedCategory.equals("Είδος Επιχείρησης")) {
                    typeSpecializationSpinner.setVisibility(View.VISIBLE);
                    findBusinessCategory(selectedCategory);
                }else{
                    typeSpecializationSpinner.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        businessSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(BusinessRegistration.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.pop_up_loading);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

                        ProfUser.getInstance().setBusiness(new Business(businessName, businessType, typeSpecialization, businessCity,
                                businessAddress, businessPostCode, businessPhone));

                        String result = RegistrationEngine.getInstance().businessRegistration();


                        try {
                            result = new String(result.getBytes("ISO-8859-1"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (result.equals("Business Registration Success")) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), GroupRegistration.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Όλα τα πεδία είναι απαραίτητα!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
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
        typeSpecializationSpinner.setAdapter(new ArrayAdapter<String>(BusinessRegistration.this,
                android.R.layout.simple_spinner_dropdown_item,
                array));
    }
}