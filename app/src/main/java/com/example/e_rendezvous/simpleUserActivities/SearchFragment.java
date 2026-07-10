package com.example.e_rendezvous.simpleUserActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private RecyclerView businessTypeRecyclerView;
    private SearchBusinessTypeAdapter businessTypeAdapter;
    private ArrayList<String> titles;
    private ArrayList<Integer> images;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        businessTypeRecyclerView = view.findViewById(R.id.businessTypeRecyclerView);

        titles = new ArrayList<>();
        images = new ArrayList<>();

        images.add(R.drawable.business_athlitismos);
        images.add(R.drawable.business_kataskeuastikos_klados);
        images.add(R.drawable.business_logistikos_klados);
        images.add(R.drawable.business_marketing);
        images.add(R.drawable.business_mixanologika);
        images.add(R.drawable.business_nomikos_klados);
        images.add(R.drawable.business_aisthitiki);
        images.add(R.drawable.business_responsive);
        images.add(R.drawable.business_doctor);
        images.add(R.drawable.business_psixologia);

        titles.add("Αθλητισμός");
        titles.add("Κατασκευαστικός Κλάδος");
        titles.add("Λογιστικά Επαγγέλματα");
        titles.add("Μarketing");
        titles.add("Μηχανολογικά Επαγγέλματα");
        titles.add("Νομικός Κλάδος");
        titles.add("Επαγγέλματα Αισθητικής");
        titles.add("Τεχνολογία Πληροφορικής");
        titles.add("Τομέας Υγείας");
        titles.add("Επαγγέλματα Ψυχολογίας");

        businessTypeAdapter = new SearchBusinessTypeAdapter(view.getContext(), titles, images);
        businessTypeRecyclerView.setAdapter(businessTypeAdapter);
        businessTypeRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2, GridLayoutManager.VERTICAL,false));

    }
}