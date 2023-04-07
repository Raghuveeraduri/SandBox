package com.example.sandbox;

import android.content.Context;
import android.util.Log;
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
import java.util.HashMap;

import at.markushi.ui.CircleButton;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    Context context;
    FirebaseFirestore db;String userId;String docId;String game_name;
    ArrayList<WishlistDataModel> data;
    WishlistAdapter(Context context,ArrayList<WishlistDataModel> data,String userId) {
        this.context=context;
        this.data=data;
        this.userId=userId;
    }
    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.wishlist_item_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        int pos=position;
        WishlistDataModel obj=data.get(position);
        holder.wishlist_discount.setText("-"+String.valueOf(obj.discount)+"%");
        holder.wishlist_prev_rate.setText(" ₹"+String.valueOf(obj.rate));
        holder.wishlist_rate.setText(" ₹"+String.valueOf(obj.rate-(obj.rate*obj.discount/100)-1));
        Glide.with(context).load(obj.product_logo).into(holder.wishlist_product_logo);
        db=FirebaseFirestore.getInstance();
        holder.move_to_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(userId).collection("Wishlist").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            docId=documentSnapshot.getId();
                            game_name= (String) documentSnapshot.get("name");
                            break;
                        }
                        HashMap<String,Object> cart=new HashMap<>();
                        cart.put("name",game_name);
                        cart.put("product_logo",obj.product_logo);
                        cart.put("discount",obj.discount);
                        cart.put("rate",obj.rate);
                        db.collection("Users").document(userId).collection("Cart").document(docId).set(cart);
                        db.collection("Users").document(userId).collection("Wishlist").document(docId).delete();
                        data.remove(pos);
                    }
                });
            }
        });
        holder.wishlist_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(userId).collection("Wishlist").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            docId=documentSnapshot.getId();
                            game_name= (String) documentSnapshot.get("name");
                            break;
                        }
                        db.collection("Users").document(userId).collection("Wishlist").document(docId).delete();
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
        MaterialButton move_to_bag;
        ImageView wishlist_product_logo;
        CircleButton wishlist_remove;
        TextView wishlist_discount;
        TextView wishlist_prev_rate;TextView wishlist_rate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            move_to_bag=itemView.findViewById(R.id.move_to_bag);
            wishlist_product_logo=itemView.findViewById(R.id.wishlist_product_logo);
            wishlist_remove=itemView.findViewById(R.id.wishlist_remove);
            wishlist_discount=itemView.findViewById(R.id.wishlist_discount);
            wishlist_prev_rate=itemView.findViewById(R.id.wishlist_prev_rate);
            wishlist_rate=itemView.findViewById(R.id.wishlist_rate);
        }
    }
}
