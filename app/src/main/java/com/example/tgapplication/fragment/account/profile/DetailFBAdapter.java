package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class DetailFBAdapter extends RecyclerView.Adapter<DetailFBAdapter.DetailFBHolder> {

    Context context;
    ArrayList<FacebookImage.FbImage> urlImages;
    public DetailFBAdapter(Context context, ArrayList<FacebookImage.FbImage> urlImages, DetailFbInterface detailFbInterface)
    {
        this.context=context;
        this.urlImages=urlImages;
        this.detailFbInterface=detailFbInterface;
    }
    @NonNull
    @Override
    public DetailFBAdapter.DetailFBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_fb_images,parent,false);
        return new DetailFBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFBAdapter.DetailFBHolder holder, int position) {
        Log.i("TAG", "onBindViewHolder: "+urlImages.get(position));

        if(urlImages.get(position).getStatus()==1)
        {
            holder.ivTitle.setVisibility(View.VISIBLE);
            holder.ivadd.setVisibility(View.INVISIBLE);
        }
        else{
            holder.ivTitle.setVisibility(View.INVISIBLE);
            holder.ivadd.setVisibility(View.VISIBLE);
        }




        holder.rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailFbInterface.fetchFbImage(urlImages.get(position).getUrl());
                urlImages.get(position).setStatus(1);
                holder.ivTitle.setVisibility(View.VISIBLE);
                holder.ivadd.setVisibility(View.INVISIBLE);


                Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(context).load(urlImages.get(position).getUrl()).placeholder(R.drawable.ic_broken_image_primary_24dp)
                .centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urlImages.size();
    }

    public class DetailFBHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivTitle,ivadd;
        RelativeLayout rl_image;

        public DetailFBHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            rl_image=itemView.findViewById(R.id.rl_image);
            ivTitle=itemView.findViewById(R.id.ivTitle);
            ivadd=itemView.findViewById(R.id.ivadd);


        }
    }

    DetailFbInterface detailFbInterface;

    public interface DetailFbInterface{
        void fetchFbImage(String imgUrl);
    }
}
