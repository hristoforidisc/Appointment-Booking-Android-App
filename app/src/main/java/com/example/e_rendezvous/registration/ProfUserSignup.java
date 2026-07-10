package com.example.e_rendezvous.registration;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_rendezvous.PreSignup;
import com.example.e_rendezvous.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import dbengine.RegistrationEngine;
import models.ProfUser;

public class ProfUserSignup extends AppCompatActivity {

    private Button profUserSignupButton;
    private EditText profUserUsername, profPassword, profUserName, profUserEmail;
    private ImageView profUserPhoto;
    private ImageButton backButton, profUserImageUploadButton;
    private Bitmap bitmap;
    private final int IMG_REQUEST = 1;
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
        setContentView(R.layout.activity_prof_user_signup);

        backButton = findViewById(R.id.backButton);
        profUserImageUploadButton = findViewById(R.id.UploadImageButton);
        profUserSignupButton = findViewById(R.id.ProfUserSignupButton);
        profUserPhoto = findViewById(R.id.ProfImage);
        profUserUsername = findViewById(R.id.ProfUsernameEditText);
        profPassword = findViewById(R.id.ProfPasswordEditText);
        profUserName = findViewById(R.id.ProfNameEditText);
        profUserEmail = findViewById(R.id.ProfEmailEditText);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PreSignup.class);
                startActivity(intent);
                finish();
            }
        });

        profUserSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ProfUserSignup.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.pop_up_loading);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        String username, password, email, name;
                        username = String.valueOf(profUserUsername.getText());
                        password = String.valueOf(profPassword.getText());
                        name = String.valueOf(profUserName.getText());
                        email = String.valueOf(profUserEmail.getText());

                        ProfUser.getInstance(username, password, name, email);

                        if(bitmap != null) {
                            String result = RegistrationEngine.getInstance().profUserRegistration(imageToString(bitmap));
                            if (result.equals("Sign Up Success")) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), BusinessRegistration.class);
                                startActivity(intent);
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Επέλεξε φωτογραφία προφίλ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        profUserImageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data!=null){
            Uri path = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profUserPhoto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String imageToString (Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imgBytes);
    }
}