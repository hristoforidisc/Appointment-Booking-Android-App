package com.example.e_rendezvous.profUserActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import dbengine.RegistrationEngine;
import models.ProfUser;
import models.Rendezvous;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {
    private Context context;
    private ArrayList<Rendezvous> rendezvous;

    public NotificationAdapter(Context context, ArrayList<Rendezvous> rendezvous){
        this.context = context;
        this.rendezvous = rendezvous;
    }

    public void updateData(ArrayList<Rendezvous> data) {
        rendezvous = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NotificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_rendezvous_row, parent,false);
        return new NotificationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterViewHolder holder, int position) {
        holder.customerName.setText(rendezvous.get(position).getSimpleUser().getName());
        holder.durationTextView.setText(rendezvous.get(position).getStartTime() + " - " + rendezvous.get(position).getEndTime());
        holder.costTextView.setText(rendezvous.get(position).getBasket().getFinalCost());
        holder.date.setText(rendezvous.get(position).getDay() + "/" + (rendezvous.get(position).getMonth() + 1) + "/" + rendezvous.get(position).getYear());
        holder.services.setText(rendezvous.get(position).getBasket().getServicesForNotifications());
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = RegistrationEngine.getInstance().deleteRendezvous(rendezvous.get(position).getRendezvousID());
                if(result.equals("RendezvousDeleteSuccess")){

                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if(installed){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String message = ProfUser.getInstance().getBusiness().getBusinessName() + ": Σας ενημερώνουμε ότι το ραντεβού σας για τις " + rendezvous.get(position).getDate() + " δεν γίνεται δεκτό.";
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+30" + rendezvous.get(position).getSimpleUser().getPhone() +"&text=" + message));
                        context.startActivity(intent);
                    }

                    for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
                        if(ProfUser.getInstance().getRendezvous().get(i).getRendezvousID().equals(rendezvous.get(position).getRendezvousID())){
                            ProfUser.getInstance().getRendezvous().remove(i);
                            break;
                        }
                    }
                    rendezvous.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                }
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = RegistrationEngine.getInstance().setRendezvousStatus(rendezvous.get(position).getRendezvousID(), "true");
                if(result.equals("Rendezvous Status Update Success")){

                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if(installed){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String message = ProfUser.getInstance().getBusiness().getBusinessName() + ": Σας ενημερώνουμε ότι το ραντεβού σας για τις " + rendezvous.get(position).getDate() + " έγινε δεκτό.";
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+30" + rendezvous.get(position).getSimpleUser().getPhone() +"&text=" + message));
                        context.startActivity(intent);
                    }

                    for(int i = 0; i < ProfUser.getInstance().getRendezvous().size(); i++){
                        if(ProfUser.getInstance().getRendezvous().get(i).getRendezvousID().equals(rendezvous.get(position).getRendezvousID())){
                            ProfUser.getInstance().getRendezvous().get(i).setIsReserved("true");
                            break;
                        }
                    }
                    rendezvous.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return rendezvous.size();
    }

    public class NotificationAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, durationTextView, costTextView, date, services;
        CardView rendezvousCardView;
        ImageView confirmationDisplay;
        Button cancel, accept;

        public NotificationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            rendezvousCardView = itemView.findViewById(R.id.rendezvousCardView);
            customerName = itemView.findViewById(R.id.customerName);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            date = itemView.findViewById(R.id.date);
            confirmationDisplay = itemView.findViewById(R.id.confirmationDisplay);
            services = itemView.findViewById(R.id.services);
            accept = itemView.findViewById(R.id.acceptButton);
            cancel = itemView.findViewById(R.id.cancelButton);
        }
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = context.getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            Toast.makeText(context, "Για την αποστολή ενημερωτικού μηνύματος πρέπει να εγκαταστήσετε την εφαρμογή 'WhatsApp'", Toast.LENGTH_LONG).show();
            app_installed = false;
        }
        return app_installed;
    }
}