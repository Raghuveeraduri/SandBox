package com.example.sandbox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Payment extends AppCompatActivity implements PaymentResultListener {
    MaterialButton paybtn;String amount;FirebaseFirestore db;
    RecyclerView paymentRecycler;String userId;long docId;
    String game_name,product_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paybtn=findViewById(R.id.paybtn);
        paymentRecycler=findViewById(R.id.paymentRecycler);
        userId=getIntent().getExtras().getString("userId");
        ArrayList<PaymentDataModel> data=new ArrayList<PaymentDataModel>();
        PaymentAdapter adapter=new PaymentAdapter(this,data);
        paymentRecycler.setAdapter(adapter);
        paymentRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        paymentRecycler.setHasFixedSize(true);
        db= FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("Cart").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(Payment.this, "Error", Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType()== DocumentChange.Type.ADDED){
                        data.add(dc.getDocument().toObject(PaymentDataModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Checkout.preload(getApplicationContext());
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras=getIntent().getExtras();
                amount=extras.getString("amount");
                startPayment();
            }
        });
    }
    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_j7UWXyxHYnmOj0");
        checkout.setImage(R.drawable.joystick);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "SandBox Gaming Services");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", ""+Integer.parseInt(amount)*100);//500*100
            options.put("prefill.email", "raghuveeraduri@gmail.com");
            options.put("prefill.contact","6303037812");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("PayTAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        db.collection("Users").document(userId).collection("Orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                docId=queryDocumentSnapshots.size();
                db.collection("Users").document(userId).collection("Cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            game_name= (String) documentSnapshot.get("name");
                            product_logo= (String) documentSnapshot.get("product_logo");
                            HashMap<String,Object> orders=new HashMap<>();
                            orders.put("name",game_name);
                            orders.put("product_logo",product_logo);
                            docId++;
                            db.collection("Users").document(userId).collection("Orders").document(String.valueOf(docId)).set(orders);
                        }
                    }
                });
            }
        });
        sendMail();
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
    void sendMail(){
        try {
            String stringSenderEmail = "SenderEmail963@gmail.com";
            String stringReceiverEmail = "gakon85921@fectode.com";
            String stringPasswordSenderEmail = "Test*123";
            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            mimeMessage.setSubject("Subject: Android App email");
            mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}