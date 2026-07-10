package com.example.e_rendezvous.profUserActivities;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_rendezvous.R;

import java.util.ArrayList;
import java.util.List;

import models.SimpleUser;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private Context context;
    private ArrayList<SimpleUser> contacts;
    private SimpleUser selectedUser;
    private Dialog dialog;
    private EditText customerName;
    private EditText customerPhone;

    public ContactsAdapter(Context context, Dialog dialog, EditText customerName, EditText customerPhone, ArrayList<SimpleUser> contacts){

        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.dialog = dialog;
        this.context = context;
        this.contacts = contacts;
        this.selectedUser = new SimpleUser();
    }

    public void updateData(ArrayList<SimpleUser> data) {
        this.contacts = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contacts_row, parent,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
       holder.clientName.setText(contacts.get(position).getName());
       holder.clientPhone.setText(contacts.get(position).getPhone());
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUser = contacts.get(position);
                customerName.setText(selectedUser.getName());
                customerPhone.setText((selectedUser.getPhone()));
                dialog.dismiss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView clientName, clientPhone;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.clientName);
            clientPhone = itemView.findViewById(R.id.clientPhone);
        }
    }
}
