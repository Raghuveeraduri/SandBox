package com.example.sandbox;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import ru.nikartm.support.ImageBadgeView;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder>{
    FirebaseFirestore db;
    Context context;String docId;String userId;String game_name;
    ArrayList<FeaturedDataModel> dataList;boolean result;
    FirebaseAuth mAuth;
    FeaturedAdapter(Context context,ArrayList<FeaturedDataModel> dataList){
        this.context=context;
        this.dataList=dataList;
    }

    public void setFilteredList(ArrayList<FeaturedDataModel> filteredList){
        this.dataList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeaturedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.featured_item_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedAdapter.ViewHolder holder, int position) {
        int pos=position;
        FeaturedDataModel obj=dataList.get(position);
        holder.discount.setText("-"+String.valueOf(obj.discount)+"%");
        holder.prev_rate.setText(" ₹"+String.valueOf(obj.rate));
        holder.rate.setText(" ₹"+String.valueOf(obj.rate-(obj.rate*obj.discount/100)-1));
        Glide.with(context).load(obj.getProduct_logo()).into(holder.product_logo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,GameDescription.class);
                i.putExtra("id",holder.getBindingAdapterPosition()+1);
                i.putExtra("discount",String.valueOf(obj.discount));
                i.putExtra("rate",String.valueOf(obj.rate));
                i.putExtra("category","Games");
                context.startActivity(i);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        holder.featured_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Games").whereEqualTo("id",holder.getBindingAdapterPosition()+1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            game_name=documentSnapshot.get("name").toString();
                        }
                        db.collection("Users").whereEqualTo("email",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                    userId=documentSnapshot.getId();
                                    break;
                                }
                                HashMap<String,Object> cart=new HashMap<>();
                                cart.put("name",game_name);
                                cart.put("product_logo",obj.product_logo);
                                cart.put("discount",obj.discount);
                                cart.put("rate",obj.rate);
                                db.collection("Users").document(userId).collection("Cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        docId= String.valueOf(queryDocumentSnapshots.size()+1);
                                        db.collection("Users").document(userId).collection("Cart").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                result=queryDocumentSnapshots.isEmpty();
                                                if(result){
                                                    db.collection("Users").document(userId).collection("Cart").document(docId).set(cart);
                                                    Toast.makeText(context, "You added "+game_name+" to cart", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        holder.featured_wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Games").whereEqualTo("id",holder.getBindingAdapterPosition()+1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            game_name=documentSnapshot.get("name").toString();
                        }
                        db.collection("Users").whereEqualTo("email",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                    userId=documentSnapshot.getId();
                                    break;
                                }
                                HashMap<String,Object> cart=new HashMap<>();
                                cart.put("name",game_name);
                                cart.put("product_logo",obj.product_logo);
                                cart.put("discount",obj.discount);
                                cart.put("rate",obj.rate);
                                db.collection("Users").document(userId).collection("Wishlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        docId= String.valueOf(queryDocumentSnapshots.size()+1);
                                        db.collection("Users").document(userId).collection("Wishlist").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                result=queryDocumentSnapshots.isEmpty();
                                                if(result){
                                                    db.collection("Users").document(userId).collection("Wishlist").document(docId).set(cart);
                                                    Toast.makeText(context, "You added "+game_name+" to wishlist", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView discount;
        TextView prev_rate;TextView rate;
        ImageView product_logo;
        Button featured_cart_btn;
        Button featured_wishlist_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            discount=itemView.findViewById(R.id.discount);
            prev_rate=itemView.findViewById(R.id.prev_rate);
            rate=itemView.findViewById(R.id.rate);
            product_logo=itemView.findViewById(R.id.product_logo);
            featured_cart_btn=itemView.findViewById(R.id.featured_cart_btn);
            featured_wishlist_btn=itemView.findViewById(R.id.featured_wishlist_btn);
        }
    }
}