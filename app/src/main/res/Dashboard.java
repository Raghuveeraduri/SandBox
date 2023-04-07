package com.udemy.animations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth mAuth;
    BottomNavigationView bottom;
    FrameLayout fl;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        bottom = findViewById(R.id.bottom);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigational_view, new HomeFragment()).commit();
        bottom.setSelectedItemId(R.id.home);
        bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navigational_view, new HomeFragment()).commit();
                        break;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navigational_view, new ProfileFragment()).commit();
                        break;
                    case R.id.menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navigational_view, new MenuFragment()).commit();
                        break;
                }
                return true;
            }
        }
        );
    }
}