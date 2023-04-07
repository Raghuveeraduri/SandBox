package com.example.sandbox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    FirebaseFirestore db;String userId;
    FirebaseAuth mAuth;
    RecyclerView ordersRecycler;
    FirebaseUser user;ArrayList<OrderDataModel> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_menu, container, false);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        ordersRecycler=v.findViewById(R.id.ordersRecycler);
        ordersRecycler.setHasFixedSize(true);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        data=new ArrayList<OrderDataModel>();
        OrderAdapter adapter=new OrderAdapter(getContext(),data);
        ordersRecycler.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();
        db.collection("Users").whereEqualTo("email",""+user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    userId=documentSnapshot.getId();
                    break;
                }
                db.collection("Users").document(""+userId).collection("Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error !=null){
                            Log.e("Firebase error",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType()== DocumentChange.Type.ADDED){
                                data.add(dc.getDocument().toObject(OrderDataModel.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        return v;
    }
}