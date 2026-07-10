package com.example.e_rendezvous.profUserActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import dbengine.RegistrationEngine;
import models.ProfUser;
import models.Service;


public class EditServiceAdapter extends RecyclerView.Adapter<EditServiceAdapter.EditServiceAdapterViewHolder> {

    private Context context;
    private ArrayList<Service> services;
    private int groupPosition;
    private EditText newServiceEditText,
            serviceDescription,
            serviseCostEditText,
            serviceDuretionEditText;
    private String oldServiceName;

    public EditServiceAdapter(Context context, int groupPosition, ArrayList<Service> services, EditText newServiceEditText,
                              EditText serviceDescription,
                              EditText serviseCostEditText,
                              EditText serviceDuretionEditText){

        this.context = context;
        this.services = services;
        this.groupPosition = groupPosition;
        this.newServiceEditText = newServiceEditText;
        this.serviceDescription = serviceDescription;
        this.serviseCostEditText = serviseCostEditText;
        this.serviceDuretionEditText = serviceDuretionEditText;
        this.oldServiceName = "";
    }

    public String getOldServiceName() {
        return oldServiceName;
    }

    public void setOldServiceName(String oldServiceName) {
        this.oldServiceName = oldServiceName;
    }

    public void updateData(ArrayList<Service> services) {

        this.services = services;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EditServiceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_service_row, parent,false);
        return new EditServiceAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EditServiceAdapterViewHolder holder, int position) {

        holder.serviceName.setText(services.get(position).getServiceName());
        holder.serviceDuration.setText(services.get(position).getDuration());
        holder.serviceCost.setText(services.get(position).getCost());

    }


    @Override
    public void onBindViewHolder(@NonNull EditServiceAdapterViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.serviceRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Διαγραφή υπηρεσίας");
                builder.setMessage("Θέλεις σίγουρα να διαγράψεις την υπηρεσία;");
                builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String result = RegistrationEngine.getInstance().serviceDelete(ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupName(), services.get(position));
                        if(result.equals("Success")){
                            Toast.makeText(context,"Η διαγραφή ολοκληρώθηκε!", Toast.LENGTH_SHORT).show();
                            ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().remove(position);
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Όχι", null);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });

        holder.serviceEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newServiceEditText.setText(ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().get(position).getServiceName());
                serviceDescription.setText(ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().get(position).getDescription());
                serviseCostEditText.setText(ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().get(position).getCost());
                serviceDuretionEditText.setText(ProfUser.getInstance().getBusiness().getGroups().get(groupPosition).getGroupServises().get(position).getDuration());
                oldServiceName = String.valueOf(holder.serviceName.getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class EditServiceAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName;
        TextView serviceDuration;
        TextView serviceCost;

        ImageButton serviceRemoveButton, serviceEditButton;

        public EditServiceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.customerName);
            serviceDuration = itemView.findViewById(R.id.durationTextView);
            serviceCost = itemView.findViewById(R.id.costTextView);
            serviceRemoveButton = itemView.findViewById(R.id.delete_rendezvous_button);
            serviceEditButton = itemView.findViewById(R.id.edit_rendezvous_button);
        }
    }
}
