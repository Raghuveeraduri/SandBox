package com.example.sandbox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryGameDescription extends AppCompatActivity {
    public String category;
    private FirebaseFirestore db;
    TextView category_textview;
    private CategoryGameAdapter adapter;
    ArrayList<CategoryGameDataModel> dataList;
    RecyclerView categoryGameRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_game_description);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        category=getIntent().getExtras().getString("category");
        categoryGameRecycler=findViewById(R.id.categoryGameRecycler);
        category_textview=findViewById(R.id.category_textview);
        category_textview.setText(""+category+" GAMES");
        category_textview.setAllCaps(true);
        categoryGameRecycler();
    }

    void categoryGameRecycler(){
        categoryGameRecycler.setHasFixedSize(true);
        categoryGameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        dataList=new ArrayList<CategoryGameDataModel>();
        adapter=new CategoryGameAdapter(getApplicationContext(),dataList,category);
        categoryGameRecycler.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        db.collection(""+category).orderBy("id", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Log.e("Firebase error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        dataList.add(dc.getDocument().toObject(CategoryGameDataModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}