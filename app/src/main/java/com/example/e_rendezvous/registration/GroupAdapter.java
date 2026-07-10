package com.example.e_rendezvous.registration;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import models.ProfUser;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<String> groupNames;

    public GroupAdapter(Context context, ArrayList<String> groupNames){

        this.context = context;
        this.groupNames = groupNames;
    }


    public void updateData(ArrayList<String> data) {
        this.groupNames = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_registration_row, parent,false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.groupTitle.setText(groupNames.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.nextGroupButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ServiceRegistration.class);
            intent.putExtra("selectedGroup", position);
            context.startActivity(intent);
            }
        });

        holder.groupRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupNames.remove(position);
                ProfUser.getInstance().getBusiness().getGroups().remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView groupTitle;
        ImageButton groupRemoveButton, nextGroupButton;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.groupTitle);
            groupRemoveButton = itemView.findViewById(R.id.delete_group_button);
            nextGroupButton = itemView.findViewById(R.id.next_group_button);
        }
    }

}
