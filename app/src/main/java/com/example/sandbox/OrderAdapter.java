package com.example.sandbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderDataModel> data;

    public OrderAdapter(Context context, ArrayList<OrderDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.order_item_design,parent,false);
        return new OrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderDataModel obj=data.get(position);
        holder.order_name.setText(obj.name);
        Glide.with(context).load(obj.getProduct_logo()).into(holder.order_product_logo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_name;
        ImageView order_product_logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_name=itemView.findViewById(R.id.order_name);
            order_product_logo=itemView.findViewById(R.id.order_product_logo);
        }
    }
}
