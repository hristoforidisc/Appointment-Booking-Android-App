package com.example.e_rendezvous.simpleUserActivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import models.ProfUser;


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ProfUser> profUsers;
    private ArrayList<ProfUser> profUsersAll;


    public SearchResultsAdapter(Context context, ArrayList<ProfUser> profUsers){

        this.context = context;
        this.profUsers = profUsers;
        this.profUsersAll = new ArrayList<>(profUsers);
    }


    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_results_row, parent,false);
        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {

        holder.businessName.setText(profUsers.get(position).getBusiness().getBusinessName());
        holder.businessLogo.setImageBitmap(profUsers.get(position).getPhotoProfil());

    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.searchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfUserProfile.class);
                intent.putExtra("username", profUsers.get(position).getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profUsers.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<ProfUser> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(profUsersAll);
            }else{
                for(ProfUser profUser: profUsersAll){
                    if(profUser.getBusiness().getBusinessName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(profUser);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            profUsers.clear();
            profUsers.addAll((Collection<? extends ProfUser>) results.values);
            notifyDataSetChanged();
        }
    };

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder{

        TextView businessName;
        ImageView businessLogo;
        CardView searchCardView;

        public SearchResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            searchCardView = itemView.findViewById(R.id.searchCardView);
            businessName = itemView.findViewById(R.id.businessName);
            businessLogo = itemView.findViewById(R.id.businessLogo);
        }
    }
}
