package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView username,logout;
    ImageView image;
        FirebaseUser firebaseUser;
        DatabaseReference ref;
        Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        username=(TextView)findViewById(R.id.setname);
        image=(ImageView)findViewById(R.id.setimg);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        logout=(TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Successfulluy logout",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),MainActivity3.class);
                startActivity(i);
                finish();
            }
        });

        ref= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernames=snapshot.child("username").getValue().toString();
                Log.e(TAG, "onDataChange: "+usernames );

                username.setText(usernames);

                if (snapshot.child("imageurl").getValue().toString().equals("default")){
                    image.setImageResource(R.drawable.imgbg);
                }else{
               img= (Uri) snapshot.child("imageurl").getValue();
                Glide.with(getApplicationContext()).load(img).into(image);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"cancelled",Toast.LENGTH_SHORT).show();
            }
        });

    }
}