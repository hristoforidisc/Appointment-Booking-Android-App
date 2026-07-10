package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.registration.ServiceAdapter;

import dbengine.RegistrationEngine;
import models.Business;
import models.ProfUser;
import models.Service;

public class EditProfUserServices extends AppCompatActivity {

    private int position;
    private EditText newServiceEditText, serviceDescription, serviseCostEditText, serviceDuretionEditText;
    private ImageButton addServiceButton, questionmarkButton, backButton;
    private Button finishServisesButton;
    private Business currentBusiness;
    private TextView currentGroupName;
    private RecyclerView serviceRecyclerView;
    private EditServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof_user_services);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = (Integer) extras.get("selectedGroup");
        }

        currentBusiness = ProfUser.getInstance().getBusiness();
        backButton = findViewById(R.id.backButton);

        questionmarkButton = findViewById(R.id.questionmarkButton);
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView);
        newServiceEditText = findViewById(R.id.newServiceEditText);
        serviceDescription = findViewById(R.id.ServiceDescription);
        serviseCostEditText = findViewById(R.id.ServiseCostEditText);
        serviceDuretionEditText = findViewById(R.id.ServiceDuretionEditText);
        addServiceButton = findViewById(R.id.addServiceButton);
        currentGroupName = findViewById(R.id.SelectedGroupName);
        finishServisesButton = findViewById(R.id.finishServisesButton);

        currentGroupName.setText(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceAdapter = new EditServiceAdapter(EditProfUserServices.this, position, ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises(),
                newServiceEditText,
                serviceDescription,
                serviseCostEditText,
                serviceDuretionEditText);
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(EditProfUserServices.this));


        questionmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String serviceName = String.valueOf(newServiceEditText.getText());
                String serviceDesc = String.valueOf(serviceDescription.getText());
                String serviseCost = String.valueOf(serviseCostEditText.getText());
                String serviceDuration = String.valueOf(serviceDuretionEditText.getText());

                int existService = -1;

                for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().size(); i++) {
                    if(serviceName.equals(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).getServiceName())){
                        if(serviceDesc.equals(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).getDescription()) &&
                                serviseCost.equals(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).getCost()) &&
                                serviceDuration.equals(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).getDuration())){
                            existService = 1;
                        }else{
                            existService = 2;
                        }
                        break;
                    }
                }

                Service currentService = new Service(serviceName, serviceDesc, serviseCost, serviceDuration);
                if(currentService.checkEmptyFields()) {
                    if((existService == -1 || (existService == 2 && serviceAdapter.getOldServiceName().equals(serviceName))) && !serviceAdapter.getOldServiceName().equals("")){
                        for(int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().size(); i++){
                            if (serviceAdapter.getOldServiceName().equals(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).getServiceName())) {
                                String result = RegistrationEngine.getInstance().serviceUpdate(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupName(), currentService, serviceAdapter.getOldServiceName());
                                if(result.equals("Success")) {
                                    ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).setServiceName(serviceName);
                                    ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).setDescription(serviceDesc);
                                    ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).setCost(serviseCost);
                                    ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().get(i).setDuration(serviceDuration);
                                    serviceAdapter.updateData(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises());
                                    Toast.makeText(getApplicationContext(), "Επιτυχής ενημέρωση στοιχείων υπηρεσίας!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                                newServiceEditText.setText("");
                                serviceDescription.setText("");
                                serviseCostEditText.setText("");
                                serviceDuretionEditText.setText("");
                                serviceAdapter.setOldServiceName("");
                            }
                        }
                    }else if(existService == 1 || existService == 2){
                        Toast.makeText(getApplicationContext(), "Η υπηρεσία που προσπαθείς να προσθέσεις υπάρχει ήδη!", Toast.LENGTH_LONG).show();
                        newServiceEditText.setText("");
                        serviceDescription.setText("");
                        serviseCostEditText.setText("");
                        serviceDuretionEditText.setText("");
                        serviceAdapter.setOldServiceName("");
                    }else {
                        RegistrationEngine.getInstance().serviceRegistrationFromEdit(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupName(), currentService);
                        ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().add(currentService);
                        serviceAdapter.updateData(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises());
                        newServiceEditText.setText("");
                        serviceDescription.setText("");
                        serviseCostEditText.setText("");
                        serviceDuretionEditText.setText("");
                        Toast.makeText(getApplicationContext(), "Η προσθήκη ήταν επιτυχής!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Όλα τα πεδία είναι απαραίτητα!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finishServisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!currentBusiness.getGroups().get(position).getGroupServises().isEmpty()){
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Συμπλήρωσε τις υπηρεσίες της κατηγορίας " + currentBusiness.getGroups().get(position).getGroupName() + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showInfoDialog() {

        Dialog dialog = new Dialog(EditProfUserServices.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.info_service_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button closeButton = dialog.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}