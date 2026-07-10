package com.example.e_rendezvous.profUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.e_rendezvous.R;

import java.util.Calendar;

public class ProfUserShoppingCart extends AppCompatActivity {


    private ImageButton backButton;
    private Button finishButton;
    private TextView finalDuration, finalCost;
    private RecyclerView cartServices;
    private ProfUserShoppingCartAdapter profUserShoppingCartAdapter;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_user_shopping_cart);

        backButton = findViewById(R.id.backButton);
        finishButton = findViewById(R.id.finishButton);
        finalDuration = findViewById(R.id.finalDuration);
        finalCost = findViewById(R.id.finalCost);
        cartServices = findViewById(R.id.cartServices);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedDate = (Calendar) extras.get("selectedDate");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent("finish_activity");
                sendBroadcast(intent2);

                Intent intent = new Intent(getApplicationContext(), AddRendezvous.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
                finish();
            }
        });

        profUserShoppingCartAdapter = new ProfUserShoppingCartAdapter(ProfUserShoppingCart.this, ServicesRendezvousAdapter.getInstance().getServicesShoppingCart());
        cartServices.setAdapter(profUserShoppingCartAdapter);
        cartServices.setLayoutManager(new LinearLayoutManager(ProfUserShoppingCart.this));

        // Initializing basket final cost & duration
        updateTextViewData();

        // If the services in the basket get changed, we update the basket cost & duration
        profUserShoppingCartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateTextViewData();
            }
        });
    }

    // Basket final cost & duration set text view
    public void updateTextViewData(){
        finalCost.setText(profUserShoppingCartAdapter.getCostSum() + " €");
        finalDuration.setText(profUserShoppingCartAdapter.getDurationSum() + " λεπτά");
    }
}