package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class message extends AppCompatActivity {
    TextView username;
    EditText message;
    ImageView send;
    ImageView img;
    DatabaseReference ref;
    String id;
    FirebaseUser firebaseUser;
    String currentuserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        username=(TextView)findViewById(R.id.uname);
        img=(ImageView)findViewById(R.id.simg);
        message=(EditText)findViewById(R.id.message);
        send=(ImageView)findViewById(R.id.send);
         id=getIntent().getStringExtra("id");
         firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
         currentuserid=firebaseUser.getUid();

        ref= FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                String ids=dataSnapshot.child("id").getValue().toString();
                if (id .equals(ids)){
                    String name=dataSnapshot.child("username").getValue().toString();
                    String imageurl=dataSnapshot.child("imageurl").getValue().toString();
                    username.setText(name);
                    if (imageurl.equals("default")){
                        img.setImageResource(R.drawable.ic_launcher_background);
                    }else {
                        Glide.with(getApplicationContext()).load(imageurl).into(img);
                    }

                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messge=message.getText().toString();
                if (TextUtils.isEmpty(messge)){
                    Toast.makeText(getApplicationContext(),"Empty message cannot be sent",Toast.LENGTH_SHORT).show();
                }else{
                    sendmessage(currentuserid,id,messge);
                }

            }
        });

    }
    private void sendmessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("chat").setValue(hashMap);


    }
}