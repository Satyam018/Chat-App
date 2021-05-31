package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageadapter extends RecyclerView.Adapter{
    public static final int msgleft=0;
    public static final int msgright=1;
    private static final String TAG ="TAG" ;
    private Context context;
    List<chat> chats;
    FirebaseUser firebaseUser;
    String imgurl;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==msgleft){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chattinglayout,parent,false);

            return new viewholder1(view);
        }else if (viewType==msgright){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chattinglayout2,parent,false);
            return new viewholder2(view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==msgleft){
            viewholder1 holders=(viewholder1)holder;
         holders.showmessage.setText(chats.get(position).getMessage());

        }else {
            viewholder2 holders=(viewholder2) holder;
            holders.tx1.setText(chats.get(position).getMessage());
        }

    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())){

            Log.e(TAG, "getItemViewType: "+msgright );
            return msgright;
        }else {
            Log.e(TAG, "getItemViewType: "+msgleft );
            return msgleft;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public messageadapter(Context context, List<chat> chats,String imgurl) {

        this.context = context;
        this.chats = chats;
        this.imgurl=imgurl;
    }


    class viewholder1 extends RecyclerView.ViewHolder{
        TextView showmessage;
        ImageView img;

        public viewholder1(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.profileimg1);
            showmessage=itemView.findViewById(R.id.usertext1);
        }
    }
    class viewholder2 extends RecyclerView.ViewHolder{
        TextView tx1;
        ImageView im;

        public viewholder2(@NonNull View itemView) {
            super(itemView);
            tx1=itemView.findViewById(R.id.usertext2);
            im=itemView.findViewById(R.id.profileimg2);
        }
    }
}
