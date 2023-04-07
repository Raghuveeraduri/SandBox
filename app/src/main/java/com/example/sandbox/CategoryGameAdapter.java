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

public class CategoryGameAdapter extends RecyclerView.Adapter<CategoryGameAdapter.ViewHolder>{

    Context context;
    ArrayList<CategoryGameDataModel> dataList;String category;
    String game_name;String userId;String docId;
    FirebaseFirestore db;FirebaseAuth mAuth;boolean result;

    CategoryGameAdapter(Context context,ArrayList<CategoryGameDataModel> dataList,String category){
        this.context=context;
        this.dataList=dataList;
        this.category=category;
    }

    @NonNull
    @Override
    public CategoryGameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.catergory_game_item_design,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGameAdapter.ViewHolder holder, int position) {
        CategoryGameDataModel obj=dataList.get(position);
        holder.category_discount.setText("-"+String.valueOf(obj.discount)+"%");
        holder.category_prev_rate.setText(" ₹"+String.valueOf(obj.rate));
        holder.category_rate.setText(" ₹"+String.valueOf(obj.rate-(obj.rate*obj.discount/100)-1));
        Glide.with(context).load(obj.getProduct_logo()).into(holder.category_product_logo);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user =mAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,GameDescription.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id",holder.getBindingAdapterPosition()+1);
                i.putExtra("discount",String.valueOf(obj.discount));
                i.putExtra("rate",String.valueOf(obj.rate));
                i.putExtra("category",category);
                context.startActivity(i);
            }
        });
        holder.category_wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(""+category).whereEqualTo("id",holder.getBindingAdapterPosition()+1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                db.collection("Users").document(userId).collection("Wishlist").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        result=queryDocumentSnapshots.isEmpty();
                                        if (result){
                                            db.collection("Users").document(userId).collection("Wishlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    docId= String.valueOf(queryDocumentSnapshots.size()+1);
                                                    db.collection("Users").document(userId).collection("Wishlist").document(docId).set(cart);
                                                    Toast.makeText(context, "You added "+game_name+" to wishlist", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
        holder.category_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(""+category).whereEqualTo("id",holder.getBindingAdapterPosition()+1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                db.collection("Users").document(userId).collection("Cart").whereEqualTo("product_logo",""+obj.product_logo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        result=queryDocumentSnapshots.isEmpty();
                                        if (result){
                                            db.collection("Users").document(userId).collection("Cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    docId= String.valueOf(queryDocumentSnapshots.size()+1);
                                                    db.collection("Users").document(userId).collection("Cart").document(docId).set(cart);
                                                    Toast.makeText(context, "You added "+game_name+" to cart", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView category_discount;
        TextView category_prev_rate;TextView category_rate;
        ImageView category_product_logo;
        Button category_wishlist_btn;Button category_cart_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_discount=itemView.findViewById(R.id.category_discount);
            category_prev_rate=itemView.findViewById(R.id.category_prev_rate);
            category_rate=itemView.findViewById(R.id.category_rate);
            category_product_logo=itemView.findViewById(R.id.category_product_logo);
            category_wishlist_btn=itemView.findViewById(R.id.category_wishlist_btn);
            category_cart_btn=itemView.findViewById(R.id.category_cart_btn);
        }
    }
}