package com.example.project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageadapter extends FragmentPagerAdapter {


    int tabcount;
    public pageadapter(@NonNull FragmentManager fm, int behaviour) {
        super(fm);
        tabcount=behaviour;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:return new chatfragment();
           case 1:return new userfragemnt();
           default:return null;
       }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
