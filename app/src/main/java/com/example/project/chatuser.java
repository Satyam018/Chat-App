package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chatuser extends AppCompatActivity {
    ImageView userimg,send;
    RecyclerView recyclerView5;
    TextView chatusername;
    List<chat> chats;
    String current;
    EditText chatentered;
    String imgurl;


    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatuser);
        userimg=(ImageView)findViewById(R.id.chatimg);
        chatusername=(TextView)findViewById(R.id.chatname);
        send=(ImageView)findViewById(R.id.sendchat);
        chatentered=(EditText)findViewById(R.id.chat) ;
        recyclerView5=(RecyclerView)findViewById(R.id.recycler5);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView5.setLayoutManager(linearLayoutManager);

        String id= getIntent().getStringExtra("id").toString();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
         current=firebaseUser.getUid().toString();

        getuserinfo(id);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat=chatentered.getText().toString();
                if (!TextUtils.isEmpty(chat)){
                    sendmessage(chat,current,id);


                }else {
                    Toast.makeText(getApplicationContext(),"blank message cannot be sent",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getuserinfo(String id){
        Log.e(TAG, "getuserinfo: "+id );
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String ids=dataSnapshot.child("id").getValue().toString();

                    if (ids.equals(id)){
                    String uname=dataSnapshot.child("username").getValue().toString();
                    chatusername.setText(uname);
                  imgurl=dataSnapshot.child("imageurl").getValue().toString();
                    if (imgurl.equals("default")){
                        userimg.setImageResource(R.drawable.imgbg);
                    }else
                    {
                        Glide.with(getApplicationContext()).load(imgurl).into(userimg);
                    }
                    Log.e(TAG, "onDataChange: "+uname+imgurl );


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"unable to obtain",Toast.LENGTH_SHORT).show();
            }
        });

        getmessage(current,id,imgurl);
    }
    public void getmessage(String cuserid, String chatuserid,String imgurl){
        chats=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String sender=dataSnapshot.child("sender").getValue().toString();
                    String receiver=dataSnapshot.child("receiver").getValue().toString();

                    if (sender.equals(cuserid)&&receiver.equals(chatuserid)||sender.equals(chatuserid)||receiver.equals(cuserid)){
                        String message=dataSnapshot.child("message").getValue().toString();
                        chats.add(new chat(sender,receiver,message));

                    }

                }
                messageadapter messageadapter=new messageadapter(getApplicationContext(),chats,imgurl);
                recyclerView5.setAdapter(messageadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendmessage(String message,String sender,String receiver){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("chat");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        ref.push().setValue(hashMap);
        this.chatentered.setText("");
    }

    @Override
    public void onBackPressed() {

        Intent i=new Intent(this,MainActivity2.class);
        startActivity(i);

    }
}