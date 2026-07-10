package com.example.e_rendezvous.simpleUserActivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_rendezvous.R;
import java.util.ArrayList;
import java.util.List;

public class SearchBusinessTypeAdapter extends RecyclerView.Adapter<SearchBusinessTypeAdapter.SearchBusinessTypeAdapterViewHolder> {

    private Context context;
    private ArrayList<String> businessCategories;
    private ArrayList<Integer> images;

    public SearchBusinessTypeAdapter(Context context, ArrayList<String> businessCategories, ArrayList<Integer> images){

        this.context = context;
        this.businessCategories = businessCategories;
        this.images = images;
    }


    @NonNull
    @Override
    public SearchBusinessTypeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_business_type_row, parent,false);
        return new SearchBusinessTypeAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchBusinessTypeAdapterViewHolder holder, int position) {

        holder.businessTypeName.setText(businessCategories.get(position));
        holder.businessImage.setImageResource(images.get(position));
    }


    @Override
    public void onBindViewHolder(@NonNull SearchBusinessTypeAdapterViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.businessCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchBusinessSpecialist.class);
                intent.putExtra("bysinessType", businessCategories.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return businessCategories.size();
    }


    public class SearchBusinessTypeAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView businessTypeName;
        ImageView businessImage;
        CardView businessCardView;

        public SearchBusinessTypeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            businessTypeName = itemView.findViewById(R.id.businessTypeName);
            businessImage = itemView.findViewById(R.id.businessImage);
            businessCardView = itemView.findViewById(R.id.businessCardView);
        }
    }
}
