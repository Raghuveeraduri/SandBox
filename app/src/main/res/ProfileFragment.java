package com.udemy.animations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        Button logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("status","");
                editor.commit();
                mAuth.signOut();
                startActivity(new Intent(getActivity().getApplicationContext(),Login.class));
            }
        });
        return view;
    }
}