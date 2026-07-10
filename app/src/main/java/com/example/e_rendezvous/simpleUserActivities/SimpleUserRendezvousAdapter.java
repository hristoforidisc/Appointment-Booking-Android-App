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

import models.Rendezvous;

public class SimpleUserRendezvousAdapter extends RecyclerView.Adapter<SimpleUserRendezvousAdapter.SimpleUserRendezvousViewHolder>{

    private Context context;
    private ArrayList<Rendezvous> rendezvous;
    private static SimpleUserRendezvousAdapter currentAdapter = null;


    public SimpleUserRendezvousAdapter(Context context, ArrayList<Rendezvous> rendezvous){
        this.context = context;
        this.rendezvous = rendezvous;
    }

    public SimpleUserRendezvousAdapter(){ }

    public static SimpleUserRendezvousAdapter getInstance(Context context, ArrayList<Rendezvous> rendezvous){
        currentAdapter = new SimpleUserRendezvousAdapter(context, rendezvous);
        return currentAdapter;
    }

    public static SimpleUserRendezvousAdapter getInstance(){
        if (currentAdapter == null){
            currentAdapter = new SimpleUserRendezvousAdapter();
        }
        return currentAdapter;
    }

    public void updateData(ArrayList<Rendezvous> data) {
        rendezvous = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SimpleUserRendezvousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.simple_user_rendezvous_row, parent,false);
        return new SimpleUserRendezvousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleUserRendezvousViewHolder holder, int position) {

        holder.customerName.setText(rendezvous.get(position).getProfUser().getBusiness().getBusinessName());
        holder.durationTextView.setText(rendezvous.get(position).getStartTime() + " - " + rendezvous.get(position).getEndTime());
        holder.costTextView.setText(rendezvous.get(position).getBasket().getFinalCost());
        holder.date.setText(rendezvous.get(position).getDay() + "/" + (rendezvous.get(position).getMonth() + 1) + "/" + rendezvous.get(position).getYear());
        if(rendezvous.get(position).getIsReserved().equals("true")){
            holder.confirmationDisplay.setImageResource(R.drawable.reserved_rendezvous);
        }else{
            holder.confirmationDisplay.setImageResource(R.drawable.reserved_rendezvous_not);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleUserRendezvousViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.rendezvousCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SimpleUserRendezvousInfo.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rendezvous.size();
    }

    public class SimpleUserRendezvousViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, durationTextView, costTextView, date;
        CardView rendezvousCardView;
        ImageView confirmationDisplay;

        public SimpleUserRendezvousViewHolder(@NonNull View itemView) {
            super(itemView);
            rendezvousCardView = itemView.findViewById(R.id.rendezvousCardView);
            customerName = itemView.findViewById(R.id.customerName);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            date = itemView.findViewById(R.id.date);
            confirmationDisplay = itemView.findViewById(R.id.confirmationDisplay);
        }
    }
}
