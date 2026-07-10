package com.example.e_rendezvous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.e_rendezvous.registration.ProfUserSignup;

public class PreSignup extends AppCompatActivity {

    private ImageButton simpleUserSignup, profUserSignup;
    private ImageButton backButton;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_singup);

        backButton = findViewById(R.id.backButton);
        simpleUserSignup = findViewById(R.id.buttonSimpleUser);
        profUserSignup = findViewById(R.id.buttonProfUser);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        simpleUserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SimpleUserSignup.class);
                startActivity(intent);
                finish();
            }
        });

        profUserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfUserSignup.class);
                startActivity(intent);
                finish();
            }
        });
    }
}