package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView username,logout;
    ImageView image;
        FirebaseUser firebaseUser;
        DatabaseReference ref;
        Uri img;
        TabLayout tabLayout;
        ViewPager viewPager2;
        TabItem item1,item2;
        pageadapter pageadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        username = (TextView) findViewById(R.id.setname);
        image = (ImageView) findViewById(R.id.setimg);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        logout = (TextView) findViewById(R.id.logout);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager2 = (ViewPager) findViewById(R.id.viewpagers);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Successfulluy logout", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(i);
                finish();
            }

        });


        ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernames = snapshot.child("username").getValue().toString();
                Log.e(TAG, "onDataChange: " + usernames);

                username.setText(usernames);

                if (snapshot.child("imageurl").getValue().toString().equals("default")) {
                    image.setImageResource(R.drawable.imgbg);
                } else {
                   String img =  snapshot.child("imageurl").getValue().toString();
                    Glide.with(getApplicationContext()).load(img).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        Viewpageradapter viewpageradapter=new Viewpageradapter(getSupportFragmentManager());
        viewpageradapter.addfragment(new chatfragment(),"chat");
        viewpageradapter.addfragment(new userfragemnt(),"User");
        viewPager2.setAdapter(viewpageradapter);
        tabLayout.setupWithViewPager(viewPager2);


    }
    class Viewpageradapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragment;
        private ArrayList<String> title;

        public Viewpageradapter(@NonNull FragmentManager fm) {
            super(fm);
           this.fragment=new ArrayList<>();
           this.title=new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

        public void addfragment(Fragment fragments,String titles){
            fragment.add(fragments);
            title.add(titles);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}