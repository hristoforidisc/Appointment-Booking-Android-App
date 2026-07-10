package com.example.e_rendezvous.profUserActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import models.Service;


public class ServicesRendezvousAdapter extends RecyclerView.Adapter<ServicesRendezvousAdapter.ServiceRendezvousViewHolder> {

    private Context context;
    private ArrayList<Service> services;
    private ArrayList<Service> servicesShoppingCart;
    private static ServicesRendezvousAdapter servicesRendezvousAdapter = null;

    public ServicesRendezvousAdapter(){};

    public ServicesRendezvousAdapter(Context context, ArrayList<Service> services){

        this.servicesShoppingCart = new ArrayList<>();
        this.context = context;
        this.services = services;

    }

    public static ServicesRendezvousAdapter getInstance(){
        if (servicesRendezvousAdapter == null){
            servicesRendezvousAdapter = new ServicesRendezvousAdapter();
        }
        return servicesRendezvousAdapter;
    }

    public static ServicesRendezvousAdapter getInstance(Context context, ArrayList<Service> services){
        servicesRendezvousAdapter = new ServicesRendezvousAdapter(context, services);
        return servicesRendezvousAdapter;
    }

    public ArrayList<Service> getServicesShoppingCart() {
        return servicesShoppingCart;
    }

    public void updateData(ArrayList<Service> services) {

        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesRendezvousAdapter.ServiceRendezvousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.service_rendesvous_row, parent,false);
        return new ServicesRendezvousAdapter.ServiceRendezvousViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ServicesRendezvousAdapter.ServiceRendezvousViewHolder holder, int position) {

        holder.serviceName.setText(services.get(position).getServiceName());
        holder.serviceDuration.setText(services.get(position).getDuration());
        holder.serviceCost.setText(services.get(position).getCost());
    }


    @Override
    public void onBindViewHolder(@NonNull ServicesRendezvousAdapter.ServiceRendezvousViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.serviceAddToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                servicesShoppingCart.add(services.get(position));
                Toast.makeText(context, "Η υπηρεσία προσθέθηκε στο καλάθι!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ServiceRendezvousViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName;
        TextView serviceDuration;
        TextView serviceCost;
        ImageButton serviceAddToBasketButton;

        public ServiceRendezvousViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDuration = itemView.findViewById(R.id.durationTextView);
            serviceCost = itemView.findViewById(R.id.costTextView);
            serviceAddToBasketButton = itemView.findViewById(R.id.service_add_to_basket_button);
        }
    }
}

