package com.example.sandbox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    RecyclerView wishlistRecycler;
    FirebaseFirestore db;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wishlistRecycler=findViewById(R.id.wishlistRecycler);
        userId=getIntent().getExtras().getString("userId");
        ArrayList<WishlistDataModel> data=new ArrayList<WishlistDataModel>();
        WishlistAdapter adapter=new WishlistAdapter(this,data,userId);
        wishlistRecycler.setAdapter(adapter);
        wishlistRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        wishlistRecycler.setHasFixedSize(true);
        db=FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("Wishlist").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(WishlistActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        data.add(dc.getDocument().toObject(WishlistDataModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}