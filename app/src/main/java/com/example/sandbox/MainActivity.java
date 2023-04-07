package com.example.sandbox;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Animation top_anim,bottom_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ImageView iv=findViewById(R.id.iv);
        TextView tv=findViewById(R.id.textView);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        iv.setAnimation(top_anim);
        tv.setAnimation(bottom_anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), Login.class);
                Pair[] pairs=new Pair[2];
                pairs[0] =new Pair<View,String>(iv,"logo_image");
                pairs[1] =new Pair<View,String>(tv,"logo_text");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(i,options.toBundle());
                finish();
            }
        },5000);
    }
}