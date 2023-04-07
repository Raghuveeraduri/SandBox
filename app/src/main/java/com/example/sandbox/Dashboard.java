package com.example.sandbox;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity{
    FirebaseAuth mAuth;
    FrameLayout fl;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        fl=findViewById(R.id.fl);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl,new HomeFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replace(new HomeFragment());
                        break;
                    case R.id.profile:
                        replace(new ProfileFragment());
                        break;
                    case R.id.menu:
                        replace(new MenuFragment());
                }
                return true;
            }
        });
    }
    void replace(Fragment f){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl,f).commit();
    }

}