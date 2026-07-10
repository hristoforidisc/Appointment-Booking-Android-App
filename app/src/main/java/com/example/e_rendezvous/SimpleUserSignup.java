package com.example.e_rendezvous;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_rendezvous.simpleUserActivities.SimpleUserMainActivity;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import dbengine.RegistrationEngine;
import models.SimpleUser;

public class SimpleUserSignup extends AppCompatActivity {

    private EditText simpleUserUsername, simpleUserPassword, simpleUserEmail, simpleUserName, simpleUserPhone;
    private Button simpleUserSignupButton;
    private ImageButton backButton;
    private SearchableSpinner simpleUserCitySpinner;
    private ProgressDialog progressDialog;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext(), PreSignup.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_singup);

        backButton = findViewById(R.id.backButton);
        simpleUserUsername = findViewById(R.id.username);
        simpleUserPassword = findViewById(R.id.password);
        simpleUserEmail = findViewById(R.id.EmailTextField);
        simpleUserName = findViewById(R.id.NameTextField);
        simpleUserPhone = findViewById(R.id.PhoneTextField);
        simpleUserCitySpinner = (SearchableSpinner) findViewById(R.id.CitySpinner);
        simpleUserSignupButton = findViewById(R.id.SimpleUserSignupButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreSignup.class);
                startActivity(intent);
                finish();
            }
        });

        simpleUserSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(SimpleUserSignup.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.pop_up_loading);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


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

                        SimpleUser.getInstance(username, password, email, phone, name, city);

                        String result = RegistrationEngine.getInstance().simpleUserRegistration();

                        if(result.equals("Sign Up Success")) {
                            Intent intent = new Intent(getApplicationContext(), SimpleUserMainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}