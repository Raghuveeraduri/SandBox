package com.example.sandbox;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ru.nikartm.support.ImageBadgeView;

public class GameDescription extends AppCompatActivity {
    ImageView product_logo;
    TextView discount;TextView game_prev_rate;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView game_rate;TextView game_name;String userId;
    TextView product_buy_name;
    FirebaseFirestore db;
    DocumentReference ref;
    String url;int id;String name;String category;
    MaterialButton cart;
    ImageBadgeView wishlist_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_description);
        product_logo=findViewById(R.id.product_logo);
        discount=findViewById(R.id.discount);
        game_rate=findViewById(R.id.game_rate);
        game_name=findViewById(R.id.name);
        game_prev_rate=findViewById(R.id.game_prev_rate);
        cart=findViewById(R.id.cart);
        product_buy_name=findViewById(R.id.product_buy_name);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        setResources();
        getImage();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").whereEqualTo("email",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            userId=documentSnapshot.getId();
                            break;
                        }
                        Intent i=new Intent(getApplication().getApplicationContext(),Payment.class);
                        i.putExtra("amount",game_rate.getText());
                        i.putExtra("userId",""+userId);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }
    void setResources(){
        db=FirebaseFirestore.getInstance();
        id=getIntent().getExtras().getInt("id");
        String disc=getIntent().getExtras().getString("discount");
        String prev_rat=getIntent().getExtras().getString("rate");
        discount.setText("-"+disc+" %");
        int rat= (Integer.parseInt(prev_rat)-(Integer.parseInt(prev_rat)*Integer.parseInt(disc))/100-1);
        game_rate.setText(""+rat);
        game_prev_rate.setText("₹"+prev_rat);
        game_prev_rate.setPaintFlags(game_prev_rate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
    void getImage(){
        category=getIntent().getExtras().getString("category");
        ref=db.collection(""+category).document(""+id);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    url=documentSnapshot.getString("product_logo");
                    name=documentSnapshot.getString("name");
                    game_name.setText(name);
                    product_buy_name.setText("Buy "+name);
                    Glide.with(getApplicationContext()).load(url).into(product_logo);
                }
            }
        });
    }
}