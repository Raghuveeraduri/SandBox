package com.example.sandbox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;ArrayList<CartDataModel> data;FirebaseFirestore db;String userId;String docId;
    CartAdapter(Context context,ArrayList<CartDataModel> data,String userId){
        this.context=context;this.data=data;this.userId=userId;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cart_item_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        int pos=position;
        CartDataModel obj=data.get(position);
        holder.cart_discount.setText("-"+String.valueOf(obj.discount)+"%");
        holder.cart_prev_rate.setText(" ₹"+String.valueOf(obj.rate));
        holder.cart_rate.setText(" ₹"+String.valueOf(obj.rate-(obj.rate*obj.discount/100)-1));
        Glide.with(context).load(obj.product_logo).into(holder.cart_product_logo);
        db= FirebaseFirestore.getInstance();
        holder.cart_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(userId).collection("Cart").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            docId=documentSnapshot.getId();
                            break;
                        }
                        db.collection("Users").document(userId).collection("Cart").document(docId).delete();
                        data.remove(pos);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cart_product_logo;
        CircleButton cart_remove;
        TextView cart_discount;
        TextView cart_prev_rate;TextView cart_rate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_product_logo=itemView.findViewById(R.id.cart_product_logo);
            cart_remove=itemView.findViewById(R.id.cart_remove);
            cart_discount=itemView.findViewById(R.id.cart_discount);
            cart_prev_rate=itemView.findViewById(R.id.cart_prev_rate);
            cart_rate=itemView.findViewById(R.id.cart_rate);
        }
    }
}
