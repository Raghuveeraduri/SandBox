package com.example.sandbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Signup extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    long id;
    Button toLogin,signup;
    TextInputEditText ed1,ed2,ed3,ed4,ed5;
    TextInputLayout ed1_layout,ed2_layout,ed3_layout,ed4_layout,ed5_layout;
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
        ed1_layout=findViewById(R.id.ed1_layout);
        ed2_layout=findViewById(R.id.ed2_layout);
        ed3_layout=findViewById(R.id.ed3_layout);
        ed4_layout=findViewById(R.id.ed4_layout);
        ed5_layout=findViewById(R.id.ed5_layout);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=ed1.getText().toString();
                String username=ed2.getText().toString();
                String email=ed3.getText().toString();
                String phone=ed4.getText().toString();
                String password=ed5.getText().toString();
                if(validate()) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        id=queryDocumentSnapshots.size()+1;
                                        userdetails obj=new userdetails(name,username,email,phone);
                                        db.collection("Users").document(""+id).set(obj);
                                    }
                                });
                                Toast.makeText(Signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            } else
                                Toast.makeText(Signup.this, "Registration error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }
    public boolean validate(){
        String emailVal="[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-z]+";
        String phoneVal="[1-9]{1}[0-9]{9}";
        String passVal="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
        if(ed1.length()==0){
            ed1_layout.setError("Please provide your full name");
            return false;
        }
        if(ed2.length()==0){
            ed2_layout.setError("Please provide your username");
            return false;
        }
        if(ed3.length()==0){
            ed3_layout.setError("Please provide your email");
            return false;
        }
        else if(!ed3.getText().toString().matches(emailVal)){
            ed3_layout.setError("Please enter a correct email");
            return false;
        }
        if(ed4.length()==0){
            ed4_layout.setError("Please provide your phone number");
            return false;
        }
        else if(!ed4.getText().toString().matches(phoneVal)){
            ed4_layout.setError("Phone number should be 10 digits");
            return false;
        }
        if(ed5.length()==0){
            ed5_layout.setError("Please provide your password");
            return false;
        }
        else if(!ed5.getText().toString().matches(passVal)){
            ed5_layout.setError("Password should contain atleast one capital letter,number,small and a special character");
            return false;
        }
        return true;
    }
}

class userdetails{
    public String fullname,username,email,phone;
    userdetails(){};
    userdetails(String fullname,String username,String email,String phone){
        this.email=email;this.fullname=fullname;this.username=username;this.phone=phone;
    }
}