package com.example.e_rendezvous.simpleUserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.e_rendezvous.R;
import com.example.e_rendezvous.profUserActivities.SendHttpRequestTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import models.ProfUser;

public class SearchResultsMainActivity extends AppCompatActivity {

    private ArrayList<ProfUser> profUsers;
    private RecyclerView profUsersRecyclerView;
    private ImageButton backButton;
    private SearchResultsAdapter searchResultsAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_main);

        profUsers = new ArrayList<>();
        profUsersRecyclerView = findViewById(R.id.profUsersRecyclerView);
        backButton = findViewById(R.id.backButton);
        searchView = findViewById(R.id.searchView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        String results = "";
        if (extras != null) {
            results = (String) extras.get("result");
        }


        if(results.contains("NEXT_BUS")){
            String[] allBusiness = results.split("NEXT_BUS");

            for(int i = 0; i < allBusiness.length; i++){
                String[] currentBusiness = allBusiness[i].split("NBF");

                try {
                    Bitmap returned_bitmap = new SendHttpRequestTask(currentBusiness[1]).execute().get();
                    profUsers.add(new ProfUser(currentBusiness[1],currentBusiness[0], returned_bitmap));

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        searchResultsAdapter = new SearchResultsAdapter(SearchResultsMainActivity.this, profUsers);
        profUsersRecyclerView.setAdapter(searchResultsAdapter);
        profUsersRecyclerView.setLayoutManager(new LinearLayoutManager(SearchResultsMainActivity.this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResultsAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }
}