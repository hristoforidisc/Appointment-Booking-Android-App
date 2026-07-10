package com.example.e_rendezvous.profUserActivities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import models.Rendezvous;

public class ProfUserRendezvousAdapter extends RecyclerView.Adapter<ProfUserRendezvousAdapter.ProfUserRendezvousViewHolder>{

    private Context context;
    private ArrayList<Rendezvous> rendezvous;
    private String dayClicked;

    public ProfUserRendezvousAdapter(Context context, ArrayList<Rendezvous> rendezvous, String dayClicked){

        this.dayClicked = dayClicked;
        this.context = context;
        this.rendezvous = rendezvous;
    }


    public void updateData(ArrayList<Rendezvous> data) {
        this.rendezvous = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProfUserRendezvousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rendezvous_row, parent,false);
        return new ProfUserRendezvousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfUserRendezvousViewHolder holder, int position) {

        holder.customerName.setText(rendezvous.get(position).getSimpleUser().getName());
        holder.durationTextView.setText(rendezvous.get(position).getStartTime() + " - " + rendezvous.get(position).getEndTime());
        holder.costTextView.setText(rendezvous.get(position).getBasket().getFinalCost());
    }

    @Override
    public void onBindViewHolder(@NonNull ProfUserRendezvousViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.rendezvousCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RendezvousInfo.class);
                intent.putExtra("position", position);
                intent.putExtra("dayClicked", dayClicked);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rendezvous.size();
    }

    public class ProfUserRendezvousViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, durationTextView, costTextView;
        CardView rendezvousCardView;

        public ProfUserRendezvousViewHolder(@NonNull View itemView) {
            super(itemView);
            rendezvousCardView = itemView.findViewById(R.id.rendezvousCardView);
            customerName = itemView.findViewById(R.id.customerName);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
        }
    }
}
