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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    Context context;
    ArrayList<PaymentDataModel> data;

    public PaymentAdapter(Context context, ArrayList<PaymentDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.payment_item_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        PaymentDataModel obj=data.get(position);
        holder.payment_discount.setText("-"+String.valueOf(obj.discount)+"%");
        holder.payment_prev_rate.setText(" ₹"+String.valueOf(obj.rate));
        holder.payment_rate.setText(" ₹"+String.valueOf(obj.rate-(obj.rate*obj.discount/100)-1));
        holder.payment_game_name.setText(""+obj.name);
        Glide.with(context).load(obj.getProduct_logo()).into(holder.payment_product_logo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView payment_product_logo;
        TextView payment_discount,payment_rate,payment_prev_rate,payment_game_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            payment_product_logo=itemView.findViewById(R.id.payment_product_logo);
            payment_discount=itemView.findViewById(R.id.payment_discount);
            payment_rate=itemView.findViewById(R.id.payment_rate);
            payment_prev_rate=itemView.findViewById(R.id.payment_prev_rate);
            payment_game_name=itemView.findViewById(R.id.payment_game_name);
        }
    }
}
