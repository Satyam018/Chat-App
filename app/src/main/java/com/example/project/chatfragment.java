package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class chatfragment extends Fragment {
    RecyclerView recyclerView2;
    chatadapter chatadapter;
    ArrayList<modeluser> userinfo;
    ArrayList<modeluser>userfinalinfo;


    private static final String TAG ="TAG" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_chatfragment, container, false);
      recyclerView2=view.findViewById(R.id.recycler3);
      recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String currentuseid=firebaseUser.getUid().toString();
        getuser(currentuseid);



      return view;
    }
    public void getuser(String currentuserid){
        userinfo=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               userinfo.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String userd=dataSnapshot.child("id").getValue().toString();
                    if (!userd.equals(currentuserid)){
                        String name=dataSnapshot.child("username").getValue().toString();
                        String imgurl=dataSnapshot.child("imageurl").getValue().toString();
                       Log.e(TAG, "onDataChange: "+name );
                       userinfo.add(new modeluser(userd,imgurl,name));
                    }
                }
                getchatuser(currentuserid,userinfo);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getchatuser(String currentuserid,ArrayList<modeluser> model){
        userfinalinfo=new ArrayList<>();
        Log.e(TAG, "getchatuser: "+model.size() );
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userfinalinfo.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String senderid=dataSnapshot.child("sender").getValue().toString();
                    String receiverid=dataSnapshot.child("receiver").getValue().toString();

                        if (senderid.equals(currentuserid)){
                            for (int i=0;i<model.size();i++){
                                String arrayid=model.get(i).getId();
                                if (receiverid.equals(arrayid)){
                                    String name=model.get(i).getText();
                                    String img=model.get(i).getImg();
                                    userfinalinfo.add(new modeluser(receiverid,img,name));
                                    model.remove(i);

                                }
                            }

                        }else if (receiverid.equals(currentuserid)){
                            for (int i=0;i<model.size();i++){
                                String arrayid=model.get(i).getId();
                                if (senderid.equals(arrayid)){
                                    String name=model.get(i).getText();
                                    String img=model.get(i).getImg();
                                    userfinalinfo.add(new modeluser(senderid,img,name));
                                    model.remove(i);
                                }
                            }

                        }
                    }
                Log.e(TAG, "onDataChange: "+userfinalinfo.size() );
                chatadapter=new chatadapter(userfinalinfo,getContext());
                recyclerView2.setAdapter(chatadapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"unable to obtain data",Toast.LENGTH_SHORT).show();
            }
        });

    }



}