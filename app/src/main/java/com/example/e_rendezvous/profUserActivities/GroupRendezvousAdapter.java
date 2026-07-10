package com.example.e_rendezvous.profUserActivities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.e_rendezvous.R;

import models.ProfUser;

import static android.graphics.Color.WHITE;


public class GroupRendezvousAdapter extends RecyclerView.Adapter<GroupRendezvousAdapter.GroupRendezvousViewHolder> {

    private Context context;
    private ArrayList<String> groupNames;
    private int selectedButton;

    public GroupRendezvousAdapter(Context context, ArrayList<String> groupNames){

        this.context = context;
        this.groupNames = groupNames;
        this.selectedButton = 0;
    }


    public void updateData(ArrayList<String> data) {
        this.groupNames = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GroupRendezvousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_list_row, parent,false);
        return new GroupRendezvousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRendezvousViewHolder holder, int position) {
        holder.groupButton.setText(groupNames.get(position));
        paintButtonsWhite(holder);
        if(position == 0 && selectedButton == 0){
            codeToBeExecuted(holder);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRendezvousViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedButton != position) {
                    notifyItemChanged(selectedButton);
                    codeToBeExecuted(holder);
                }

                selectedButton = position;
                ServicesRendezvousAdapter.getInstance().updateData(ProfUser.getInstance().getBusiness().getGroups().get(position).getGroupServises());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    public class GroupRendezvousViewHolder extends RecyclerView.ViewHolder{

        Button groupButton;

        public GroupRendezvousViewHolder(@NonNull View itemView) {
            super(itemView);
            groupButton = itemView.findViewById(R.id.groupButton);
        }
    }

    public void codeToBeExecuted(@NonNull GroupRendezvousViewHolder holder){
        holder.groupButton.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_button_group));
        holder.groupButton.setTextColor(WHITE);
    }

    public void paintButtonsWhite(@NonNull GroupRendezvousViewHolder holder){
        holder.groupButton.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_no_button_group));
        holder.groupButton.setTextColor(holder.groupButton.getContext().getResources().getColor(R.color.blue_color));
    }
}