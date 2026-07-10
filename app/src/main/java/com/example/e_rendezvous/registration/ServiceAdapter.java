package com.example.e_rendezvous.registration;

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
import models.ProfUser;
import models.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private ArrayList<Service> services;
    private int groupPosition;


    public ServiceAdapter(Context context, int groupPosition, ArrayList<Service> services){

        this.context = context;
        this.services = services;
        this.groupPosition = groupPosition;
    }


    public void updateData(ArrayList<Service> services) {

        this.services = services;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.service_registration_row, parent,false);
        return new ServiceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {

        holder.serviceName.setText(services.get(position).getServiceName());
        holder.serviceDuration.setText(services.get(position).getDuration());
        holder.serviceCost.setText(services.get(position).getCost());

    }


    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.serviceRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName;
        TextView serviceDuration;
        TextView serviceCost;

        ImageButton serviceRemoveButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.customerName);
            serviceDuration = itemView.findViewById(R.id.durationTextView);
            serviceCost = itemView.findViewById(R.id.costTextView);
            serviceRemoveButton = itemView.findViewById(R.id.delete_rendezvous_button);
        }
    }
}
