package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;

import java.util.ArrayList;

public class DetailFBAdapter extends RecyclerView.Adapter<DetailFBAdapter.DetailFBHolder> {

    Context context;
    ArrayList<String> urlImages;
    public DetailFBAdapter(Context context, ArrayList<String> urlImages)
    {
        this.context=context;
        this.urlImages=urlImages;
    }
    @NonNull
    @Override
    public DetailFBAdapter.DetailFBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_images,parent,false);
        return new DetailFBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFBAdapter.DetailFBHolder holder, int position) {
        Log.i("TAG", "onBindViewHolder: "+urlImages.get(position));
        Glide.with(context).load(urlImages.get(position)).placeholder(R.drawable.ic_broken_image_primary_24dp)
                .centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urlImages.size();
    }

    public class DetailFBHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public DetailFBHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);

        }
    }
}
