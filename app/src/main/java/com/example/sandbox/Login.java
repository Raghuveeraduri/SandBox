package com.example.sandbox;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextInputEditText email,password;
    TextInputLayout usernameLayout,passwordLayout;
    MaterialCheckBox RemeberMe;SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        check();
        TextView tv1=findViewById(R.id.tv1);
        ImageView iv1=findViewById(R.id.imageView2);
        Button register=findViewById(R.id.register);
        Button login=findViewById(R.id.login);
        Button forget=findViewById(R.id.forgot);
        email=findViewById(R.id.username);
        password=findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        usernameLayout=findViewById(R.id.usernameLayout);
        passwordLayout=findViewById(R.id.passwordLayout);
        RemeberMe=findViewById(R.id.RemeberMe);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(RemeberMe.isChecked()) {
                                    sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("status", "true");
                                    editor.commit();
                                }
                                sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username",""+email.getText().toString());
                                editor.commit();
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                finish();
                            } else
                                Toast.makeText(Login.this, "Login error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Forgot.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Signup.class);
                Pair[] pairs=new Pair[6];
                pairs[0] =new Pair<View,String>(iv1,"logo_image");
                pairs[1] =new Pair<View,String>(tv1,"logo_text");
                pairs[2] =new Pair<View,String>(register,"login_signup_trans");
                pairs[3] =new Pair<View,String>(login,"signup_trans");
                pairs[4] =new Pair<View,String>(email,"username_trans");
                pairs[5] =new Pair<View,String>(password,"password_trans");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(i,options.toBundle());
                finish();
            }
        });
    }

    public boolean validate(){
        String emailVal="[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-z]+";
        if(email.length()==0){
            usernameLayout.setError("Please provide your email");
            return false;
        }
        else if(!email.getText().toString().matches(emailVal)){
            usernameLayout.setError("Please enter a correct email");
            return false;
        }
        if(password.length()==0){
            passwordLayout.setError("Please provide your password");
            return false;
        }
        return true;
    }
    void check(){
        SharedPreferences sp=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String status=sp.getString("status","");
        if(status.equals("true")){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
    }
}