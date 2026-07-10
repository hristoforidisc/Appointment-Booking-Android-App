package com.example.e_rendezvous.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.e_rendezvous.R;
import models.Business;
import models.ProfUser;
import models.Service;

public class ServiceRegistration extends AppCompatActivity {

    private int position;
    private EditText newServiceEditText, serviceDescription, serviseCostEditText, serviceDuretionEditText;
    private ImageButton addServiceButton, questionmarkButton, backButton;
    private Button finishServisesButton;
    private Business currentBusiness;
    private TextView currentGroupName;
    private RecyclerView serviceRecyclerView;
    private ServiceAdapter serviceAdapter;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_registration);

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

        currentGroupName.setText(currentBusiness.getGroups().get(position).getGroupName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        serviceAdapter = new ServiceAdapter(ServiceRegistration.this, position, currentBusiness.getGroups().get(position).getGroupServises());
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(com.example.e_rendezvous.registration.ServiceRegistration.this));


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

                Service currentService = new Service(serviceName, serviceDesc, serviseCost, serviceDuration);

                if(currentService.checkEmptyFields()) {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises().add(currentService);
                    serviceAdapter.updateData(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises());

                    newServiceEditText.setText("");
                    serviceDescription.setText("");
                    serviseCostEditText.setText("");
                    serviceDuretionEditText.setText("");
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

        Dialog dialog = new Dialog(ServiceRegistration.this);
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