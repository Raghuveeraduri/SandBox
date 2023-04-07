package com.example.sandbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.getstream.avatarview.AvatarView;
import io.getstream.avatarview.coil.Avatar;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView profile_username,profile_name,profile_email,profile_phone;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        Button logout=view.findViewById(R.id.logout);
        profile_username=view.findViewById(R.id.profile_username);
        profile_name=view.findViewById(R.id.profile_name);
        profile_email=view.findViewById(R.id.profile_email);
        profile_phone=view.findViewById(R.id.profile_phone);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        db.collection("Users").whereEqualTo("email",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    profile_username.setText("@"+documentSnapshot.get("username").toString());
                    profile_name.setText(documentSnapshot.get("fullname").toString());
                    profile_email.setText(documentSnapshot.get("email").toString());
                    profile_phone.setText(documentSnapshot.get("phone").toString());
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("status","");
                editor.commit();
                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(getActivity().getApplicationContext(),Login.class));
            }
        });
        return view;
    }
}