package com.example.sandbox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView cartRecycler;
    FirebaseFirestore db;long rate,discount,final_rate;
    String userId;
    MaterialButton checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userId=getIntent().getExtras().getString("userId");
        checkout=findViewById(R.id.checkout);
        cartRecycler=findViewById(R.id.cartRecycler);
        ArrayList<CartDataModel> data=new ArrayList<CartDataModel>();
        CartAdapter adapter=new CartAdapter(this,data,userId);
        cartRecycler.setAdapter(adapter);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        cartRecycler.setHasFixedSize(true);
        db=FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("Cart").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        data.add(dc.getDocument().toObject(CartDataModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(userId).collection("Cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            rate= (long) documentSnapshot.get("rate");
                            discount=(long) documentSnapshot.get("discount");
                            final_rate+=rate-(rate*discount/100)-1;
                        }
                        Intent i=new Intent(getApplicationContext(),Payment.class);
                        i.putExtra("userId",userId);
                        i.putExtra("amount",""+String.valueOf(final_rate));
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }
}