package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adapter  extends RecyclerView.Adapter<adapter.viewholder>{

        ArrayList<modeluser> modelusers;
        Context context;
   adapter(Context context, ArrayList<modeluser> modelusers){
       this.context=context;
       this.modelusers=modelusers;
   }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowuser,parent,false);
       return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
       if (modelusers.get(position).getImg().equals("default")){
        holder.img.setImageResource(R.drawable.imgbg);}
       else {
           Glide.with(context).load(modelusers.get(position).getImg()).into(holder.img);
       }

        holder.tx1.setText(modelusers.get(position).getText());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(context,message.class);
               i.putExtra("id",modelusers.get(position).getId());
               context.startActivity(i);
           }
       });
    }

    @Override
    public int getItemCount() {
        return modelusers.size();
    }


    class viewholder extends RecyclerView.ViewHolder{
            TextView tx1;
            ImageView img;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tx1=itemView.findViewById(R.id.textview1);
            img=itemView.findViewById(R.id.imageview1);

        }
    }
}
