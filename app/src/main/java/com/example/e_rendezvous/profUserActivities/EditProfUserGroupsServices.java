package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
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
import models.Group;
import models.ProfUser;

public class EditProfUserGroupsServices extends AppCompatActivity {

    private EditText newServiceGroupEditText;
    private ImageButton addGroupOfServicesButton, questionmarkButton, backButton;
    private Button finishGroupButton;
    private RecyclerView groupRecyclerView;
    private EditGroupAdapter groupAdapter;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String outputString = "";
        if (!ProfUser.getInstance().getBusiness().getGroups().isEmpty()) {
            for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().size(); i++) {
                if (ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupServises().isEmpty()) {
                    outputString += ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupName() + ", ";
                }
            }
            if (outputString.equals("")) {
                finish();
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
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof_user_groups_services);

        questionmarkButton = findViewById(R.id.questionmarkButton);
        groupRecyclerView = findViewById(R.id.groupRecyclerView);
        newServiceGroupEditText = findViewById(R.id.newServiceEditText);
        addGroupOfServicesButton = findViewById(R.id.addServiceButton);
        finishGroupButton = findViewById(R.id.finishGroupButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String outputString = "";
                if (!ProfUser.getInstance().getBusiness().getGroups().isEmpty()) {
                    for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().size(); i++) {
                        if (ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupServises().isEmpty()) {
                            outputString += ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupName() + ", ";
                        }
                    }
                    if (outputString.equals("")) {
                        finish();
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


        // Recycler view adapter that displays all the submitted names from the groups of services
        groupAdapter = new EditGroupAdapter(EditProfUserGroupsServices.this, ProfUser.getInstance().getBusiness().getGroups(), newServiceGroupEditText);
        groupRecyclerView.setAdapter(groupAdapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(EditProfUserGroupsServices.this));

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

                    for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().size(); i++) {
                        if (groupAdapter.getOldGroupName().equals(ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupName())) {

                            String result = RegistrationEngine.getInstance().updateGroupRegistration(groupAdapter.getOldGroupName(), groupName);
                            if(result.equals("Success")){
                                ProfUser.getInstance().getBusiness().getGroups().get(i).setGroupName(groupName);
                                Toast.makeText(getApplicationContext(), "Επιτυχής ενημέρωση στοιχείων(updateGroupRegistration)!", Toast.LENGTH_SHORT).show();
                                groupAdapter.updateData(ProfUser.getInstance().getBusiness().getGroups());
                            }else{
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            newServiceGroupEditText.setText("");
                            existGroup = 1;
                            groupAdapter.setOldGroupName("");
                            break;
                        }
                    }

                    if (existGroup == -1) {
                        Group currentGroup = new Group(groupName);
                        String result = RegistrationEngine.getInstance().groupRegistrationFromUpdate(currentGroup);
                        if(result.equals("Success")) {
                            ProfUser.getInstance().getBusiness().getGroups().add(currentGroup);
                            Toast.makeText(getApplicationContext(), "Επιτυχής ενημέρωση στοιχείων(groupRegistrationFromUpdate)!", Toast.LENGTH_SHORT).show();
                            groupAdapter.updateData(ProfUser.getInstance().getBusiness().getGroups());
                        }else{
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                        newServiceGroupEditText.setText("");

                    }
                }else if(existGroup == 0){
                    Toast.makeText(getApplicationContext(), "Η κατηγορία που προσπαθείς να προσθέσεις υπάρχει ήδη!", Toast.LENGTH_LONG).show();
                    groupAdapter.setOldGroupName("");
                    newServiceGroupEditText.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "Πρόσθεσε μια νέα κατηγορία!", Toast.LENGTH_SHORT).show();
                    groupAdapter.setOldGroupName("");
                }
            }
        });

        finishGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String outputString = "";
                if (!ProfUser.getInstance().getBusiness().getGroups().isEmpty()) {
                    for (int i = 0; i < ProfUser.getInstance().getBusiness().getGroups().size(); i++) {
                        if (ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupServises().isEmpty()) {
                            outputString += ProfUser.getInstance().getBusiness().getGroups().get(i).getGroupName() + ", ";
                        }
                    }
                    if (outputString.equals("")) {
                        Toast.makeText(getApplicationContext(), "Η ενημέρωση ήταν επιτυχής!", Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ProfUserMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showInfoDialog() {

        Dialog dialog = new Dialog(EditProfUserGroupsServices.this);
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