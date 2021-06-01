package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class chatadapter extends RecyclerView.Adapter{
    ArrayList<modeluser> modelusers;
    Context context;

    public chatadapter(ArrayList<modeluser> modelusers, Context context) {
        this.modelusers = modelusers;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowuser,parent,false);
        return new view(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            view holders=(view) holder;
            holders.cuser.setText(modelusers.get(position).getText());
            String id=modelusers.get(position).getId().toString();
           if (modelusers.get(position).getImg().equals("default")){
                holders.cimg.setImageResource(R.drawable.imgbg);
           }else {
               Glide.with(context).load(modelusers.get(position).getImg()).into(holders.cimg);
           }
            holders.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,chatuser.class);
                    i.putExtra("id",modelusers.get(position).getId());
                    context.startActivity(i);
                }
            });

    }


    @Override
    public int getItemCount() {
        return modelusers.size();
    }


    class view extends RecyclerView.ViewHolder{
        TextView cuser;
        ImageView cimg;

        public view(@NonNull View itemView) {
            super(itemView);
            cimg=itemView.findViewById(R.id.imageview1);
            cuser=itemView.findViewById(R.id.textview1);
            
        }
    }
}
