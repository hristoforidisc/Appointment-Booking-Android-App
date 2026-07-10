package com.example.e_rendezvous.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.e_rendezvous.R;
import dbengine.RegistrationEngine;
import models.Business;
import models.Group;
import models.ProfUser;

public class GroupRegistration extends AppCompatActivity {

    private Business currentBusiness;
    private EditText newServiceGroupEditText;
    private ImageButton addGroupOfServicesButton, questionmarkButton;
    private Button finishGroupButton;
    private RecyclerView groupRecyclerView;
    private GroupAdapter groupAdapter;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupRegistration.this);
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
        setContentView(R.layout.activity_group_registration);

        questionmarkButton = findViewById(R.id.questionmarkButton);
        groupRecyclerView = findViewById(R.id.groupRecyclerView);
        newServiceGroupEditText = findViewById(R.id.newServiceEditText);
        addGroupOfServicesButton = findViewById(R.id.addServiceButton);
        currentBusiness = ProfUser.getInstance().getBusiness();
        finishGroupButton = findViewById(R.id.finishGroupButton);


        // Recycler view adapter that displays all the submitted names from the groups of services
        groupAdapter = new GroupAdapter(GroupRegistration.this, currentBusiness.getGroupsNames());
        groupRecyclerView.setAdapter(groupAdapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(com.example.e_rendezvous.registration.GroupRegistration.this));


        // Info button, brings up the information popup
        questionmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        addGroupOfServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = String.valueOf(newServiceGroupEditText.getText());

                int existGroup = -1;
                for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().size(); i++) {
                    if(groupName.equals(ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupName())){
                        existGroup = 0;
                        break;
                    }
                }

                if(!groupName.equals("") && existGroup != 0) {

                    if (existGroup == -1) {
                        Group currentGroup = new Group(groupName);
                        ProfUser.getInstance().getBusiness().getGroups().add(currentGroup);
                        groupAdapter.updateData(ProfUser.getInstance().getBusiness().getGroupsNames());
                        newServiceGroupEditText.setText("");
                    }
                }else if(existGroup == 0){
                    Toast.makeText(getApplicationContext(), "Η κατηγορία που προσπαθείς να προσθέσεις υπάρχει ήδη!", Toast.LENGTH_LONG).show();
                    newServiceGroupEditText.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "Πρόσθεσε μια νέα κατηγορία!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        finishGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String outputString = "";
                if (!currentBusiness.getGroups().isEmpty()) {
                    for (int i = 0; i < currentBusiness.getGroups().size(); i++) {
                        if (currentBusiness.getGroups().get(i).getGroupServises().isEmpty()) {
                            outputString += currentBusiness.getGroups().get(i).getGroupName() + ", ";
                        }
                    }
                    if (outputString.equals("")) {

                        String result = RegistrationEngine.getInstance().groupRegistration();
                        if (result.equals("Success")) {
                            Intent intent = new Intent(getApplicationContext(), WeeklyScheduleRegistration.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        outputString = outputString.substring(0, outputString.length() - 2);
                        if(outputString.contains(",")){
                            Toast.makeText(getApplicationContext(), "Οι κατηγορίες '" + outputString + "' δεν έχουν συμπληρωμένες υπηρεσίες!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Η κατηγορία '" + outputString + "' δεν έχει συμπληρωμένες υπηρεσίες!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Συμπλήρωσε τις κατηγορίες των υπηρεσιών σου!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showInfoDialog() {

        Dialog dialog = new Dialog(GroupRegistration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.info_group_popup);
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