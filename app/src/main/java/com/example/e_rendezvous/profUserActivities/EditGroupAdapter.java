package com.example.e_rendezvous.profUserActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import models.Group;
import models.ProfUser;

public class EditGroupAdapter extends RecyclerView.Adapter<EditGroupAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<Group> groups;
    private EditText groupEditText;
    private String oldGroupName;

    public EditGroupAdapter(Context context, ArrayList<Group> groups, EditText groupEditText){

        this.context = context;
        this.groups = groups;
        this.groupEditText = groupEditText;
        this.oldGroupName = "";
    }


    public void updateData(ArrayList<Group> data) {
        this.groups = data;
        notifyDataSetChanged();
    }

    public String getOldGroupName() {
        return oldGroupName;
    }

    public void setOldGroupName(String oldGroupName) {
        this.oldGroupName = oldGroupName;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_groups_row, parent,false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.groupTitle.setText(groups.get(position).getGroupName());
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.editGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupEditText.setText(holder.groupTitle.getText());
                oldGroupName = String.valueOf(holder.groupTitle.getText());
            }
        });

        holder.nextGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfUserServices.class);
                intent.putExtra("selectedGroup", position);
                context.startActivity(intent);
            }
        });

        holder.groupRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Διαγραφή κατηγορίας υπηρεσιών");
                builder.setMessage("Θέλεις σίγουρα να διαγράψεις την κατηγορία;");
                builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String result = RegistrationEngine.getInstance().deleteGroup(ProfUser.getInstance().getBusiness().getGroups().get(position));
                        if(result.equals("Success")){
                            Toast.makeText(context,"Η διαγραφή ολοκληρώθηκε!", Toast.LENGTH_SHORT).show();
                            ProfUser.getInstance().getBusiness().getGroups().remove(position);
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
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView groupTitle;
        ImageButton groupRemoveButton, nextGroupButton, editGroupButton;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.groupTitle);
            groupRemoveButton = itemView.findViewById(R.id.delete_group_button);
            nextGroupButton = itemView.findViewById(R.id.next_group_button);
            editGroupButton = itemView.findViewById(R.id.edit_group_button);
        }
    }
}