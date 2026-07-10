package com.example.e_rendezvous.profUserActivities;

import android.content.Context;
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
import models.Service;

public class ProfUserShoppingCartAdapter extends RecyclerView.Adapter<ProfUserShoppingCartAdapter.ProfUserShoppingCartViewHolder> {

    private Context context;
    private ArrayList<Service> servicesShoppingCart;
    private float costSum;
    private int durationSum;

    public ProfUserShoppingCartAdapter(Context context, ArrayList<Service> servicesShoppingCart){

        this.context = context;
        this.servicesShoppingCart = servicesShoppingCart;
        this.costSum = 0;
        this.durationSum = 0;
        calculateBasketCostAndDuration();
    }

    public float getCostSum() {
        return costSum;
    }

    public int getDurationSum() {
        return durationSum;
    }

    public void updateData(ArrayList<Service> data) {
        this.servicesShoppingCart = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProfUserShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.prof_user_shoping_cart_row, parent,false);
        return new ProfUserShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfUserShoppingCartViewHolder holder, int position) {

        holder.serviceName.setText(servicesShoppingCart.get(position).getServiceName());
        holder.serviceDuration.setText(servicesShoppingCart.get(position).getDuration());
        holder.serviceCost.setText(servicesShoppingCart.get(position).getCost());

    }

    @Override
    public void onBindViewHolder(@NonNull ProfUserShoppingCartViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        holder.serviceRemoveFromBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicesShoppingCart.remove(position);
                calculateBasketCostAndDuration();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesShoppingCart.size();
    }

    public class ProfUserShoppingCartViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName, serviceDuration, serviceCost;
        ImageButton serviceRemoveFromBasket;

        public ProfUserShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDuration = itemView.findViewById(R.id.serviceDuration);
            serviceCost = itemView.findViewById(R.id.serviceCost);
            serviceRemoveFromBasket = itemView.findViewById(R.id.service_remove_from_basket_button);

        }
    }

    public void calculateBasketCostAndDuration(){

        costSum = 0;
        durationSum = 0;
        for(int i = 0; i < servicesShoppingCart.size(); i++){
            costSum += Float.parseFloat(servicesShoppingCart.get(i).getCost());
            durationSum += Integer.parseInt(servicesShoppingCart.get(i).getDuration());
        }
    }
}