package com.example.project;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class userfragemnt extends Fragment {

    private static final String TAG ="TAG" ;
    RecyclerView recyclerView;
ArrayList<modeluser> modelusers1;
adapter adapters;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_userfragemnt, container, false);
        recyclerView=view.findViewById(R.id.recycler);
        Log.e(TAG, "onCreateView: "+"userfragment" );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        modelusers1=new ArrayList<>();
        createdata();




        return view;
    }
    public  void createdata(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String currentuser=firebaseUser.getUid().toString();
        Log.e(TAG, "createdata: "+currentuser );
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        modelusers1.clear();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

             for (DataSnapshot dataSnapshot:snapshot.getChildren()){


                String id=dataSnapshot.child("id").getValue().toString();

                 //Log.e(TAG, "onDataChange: "+userd );
                 if (!id.equals(currentuser)) {
                     String name=dataSnapshot.child("username").getValue().toString();
                     String imageurl=dataSnapshot.child("imageurl").getValue().toString();
                     modelusers1.add(new modeluser(id,imageurl,name));





                 }



             }
                adapters =new adapter(getContext(),modelusers1);
             recyclerView.setAdapter(adapters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"unable to load",Toast.LENGTH_SHORT).show();
            }
        });


    }
}