package com.example.e_rendezvous.simpleUserActivities;

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
import com.example.e_rendezvous.profUserActivities.ProfUserShoppingCartAdapter;
import com.example.e_rendezvous.profUserActivities.ServicesRendezvousAdapter;


public class SimpleUserShoppingCart extends AppCompatActivity {

    private ImageButton backButton;
    private Button finishButton;
    private TextView finalDuration, finalCost;
    private RecyclerView cartServices;
    private ProfUserShoppingCartAdapter profUserShoppingCartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_shopping_cart);

        backButton = findViewById(R.id.backButton);
        finishButton = findViewById(R.id.finishButton);
        finalDuration = findViewById(R.id.finalDuration);
        finalCost = findViewById(R.id.finalCost);
        cartServices = findViewById(R.id.cartServices);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent("finish_activity2");
                sendBroadcast(intent2);
                Intent intent = new Intent(getApplicationContext(), SimpleUserSelectDate.class);
                startActivity(intent);
                finish();
            }
        });

        profUserShoppingCartAdapter = new ProfUserShoppingCartAdapter(SimpleUserShoppingCart.this, ServicesRendezvousAdapter.getInstance().getServicesShoppingCart());
        cartServices.setAdapter(profUserShoppingCartAdapter);
        cartServices.setLayoutManager(new LinearLayoutManager(SimpleUserShoppingCart.this));

        updateTextViewData();

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