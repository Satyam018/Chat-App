package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText username,password,email;
    Button createaccount;
    FirebaseAuth auth;
    DatabaseReference ref;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email);
        createaccount=(Button)findViewById(R.id.createaccount);
        signin=(TextView)findViewById(R.id.signin);

        auth=FirebaseAuth.getInstance();
       createaccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String usernames=username.getText().toString();
               String passwords=password.getText().toString();
               String emails=email.getText().toString();

               if (TextUtils.isEmpty(usernames)||TextUtils.isEmpty(passwords)||TextUtils.isEmpty(emails)){
                   Toast.makeText(getApplicationContext(),"All field are necessary",Toast.LENGTH_SHORT).show();
               }else  if (passwords.length()<6){
                   Toast.makeText(getApplicationContext(),"Passowrd must be greater than 6",Toast.LENGTH_SHORT).show();
               }else {
                   register(usernames,emails,passwords);
               }
           }
       });
       signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),Login.class);
               startActivity(i);
           }
       });
    }
    public void register(String username,String email,String password){
       auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
               FirebaseUser firebaseUser=auth.getCurrentUser();
               String userid=firebaseUser.getUid();
               ref= FirebaseDatabase.getInstance().getReference("users").child(userid);
               HashMap<String,String>hashMap=new HashMap<>();
               hashMap.put("id",userid);
               hashMap.put("username",username);
               hashMap.put("imageurl","default");
               ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Intent i=new Intent(getApplicationContext(),MainActivity2.class);
                       startActivity(i);
                       finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"failed to create",Toast.LENGTH_SHORT).show();
                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(),"Failed to authenticate",Toast.LENGTH_SHORT).show();
           }
       });

    }
}