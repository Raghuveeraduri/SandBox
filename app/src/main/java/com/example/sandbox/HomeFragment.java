package com.example.sandbox;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    View view;
    private FirebaseFirestore db;
    TextView name;String userId;
    private FeaturedAdapter adapter;String username;
    SharedPreferences sp;
    SearchView searchView;
    RecyclerView categoriesRecycler,featuredRecycler;
    ArrayList<FeaturedDataModel> dataList;
    MaterialButton cart_icon,wishlist_icon;FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);
        categoriesRecycler=view.findViewById(R.id.categoriesRecycler);
        featuredRecycler=view.findViewById(R.id.featuredRecycler);
        wishlist_icon=view.findViewById(R.id.wishlist_icon);
        cart_icon=view.findViewById(R.id.cart_icon);
        searchView=view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        sp=getActivity().getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        username=sp.getString("username","");
        name=view.findViewById(R.id.Name);
        db=FirebaseFirestore.getInstance();
        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    if(documentSnapshot.get("email").equals(username)){
                        username=documentSnapshot.get("username").toString();
                        break;
                    }
                }
                name.setText(""+username+"\uD83D\uDC4B");
            }
        });
        categoriesRecycler();
        featuredRecycler();
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user =mAuth.getCurrentUser();
        wishlist_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity().getApplicationContext(),WishlistActivity.class);
                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            if(documentSnapshot.get("email").equals(user.getEmail())){
                                userId=documentSnapshot.getId();
                                break;
                            }
                        }
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }
                });
            }
        });
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity().getApplicationContext(),CartActivity.class);
                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            if(documentSnapshot.get("email").equals(user.getEmail())){
                                userId=documentSnapshot.getId();
                                break;
                            }
                        }
                        i.putExtra("userId",userId);
                        startActivity(i);
                    }
                });
            }
        });
        return view;
    }

    private void filterList(String newText) {
        ArrayList<FeaturedDataModel> filteredList=new ArrayList<>();
        for (FeaturedDataModel item: dataList){
            if (item.name.toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
            if (filteredList.isEmpty()){
            }
            else{
                adapter.setFilteredList(filteredList);
            }
        }
    }

    void categoriesRecycler(){
        CatergoryDataModel[] data=new CatergoryDataModel[]{
                new CatergoryDataModel("Action",R.drawable.action),
                new CatergoryDataModel("Adventure",R.drawable.adventure),
                new CatergoryDataModel("Racing",R.drawable.racing),
                new CatergoryDataModel("Sport",R.drawable.sport),
                new CatergoryDataModel("Fighting",R.drawable.fighting)
        };
        CategoryAdapter adapter=new CategoryAdapter(data);
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        categoriesRecycler.setAdapter(adapter);
    }

    void featuredRecycler(){
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        dataList=new ArrayList<FeaturedDataModel>();
        adapter=new FeaturedAdapter(getContext(),dataList);
        featuredRecycler.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        db.collection("Games").orderBy("id", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Log.e("Firebase error",error.getMessage());
                    return;
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        dataList.add(dc.getDocument().toObject(FeaturedDataModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}