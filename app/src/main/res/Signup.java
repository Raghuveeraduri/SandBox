package com.udemy.animations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference ref;
    long id;
    Button toLogin,signup;
    TextInputEditText ed1,ed2,ed3,ed4,ed5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        toLogin=findViewById(R.id.toLogin);signup=findViewById(R.id.signup);
        ed1=findViewById(R.id.ed1);
        ed2=findViewById(R.id.ed2);
        ed3=findViewById(R.id.ed3);
        ed4=findViewById(R.id.ed4);
        ed5=findViewById(R.id.ed5);
        ref=FirebaseDatabase.getInstance().getReference("Details");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    id=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ed1.getText().toString();
                String username=ed2.getText().toString();
                String email=ed3.getText().toString();
                String phone=ed4.getText().toString();
                String password=ed5.getText().toString();
                userdetails obj=new userdetails(name,username,email,phone,password);
                ref.child("User "+(id+1)).setValue(obj);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else
                            Toast.makeText(Signup.this, "Registration error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
    }
}
class userdetails{
    public String fullname,username,email,phone,password;
    userdetails(){};
    userdetails(String fullname,String username,String email,String phone,String password){
        this.email=email;this.fullname=fullname;this.password=password;this.username=username;this.phone=phone;
    }
}