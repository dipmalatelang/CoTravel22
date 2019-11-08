package com.example.tgapplication.fragment.member.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;

import java.util.List;

import static com.example.tgapplication.R.color.color8;
import static com.example.tgapplication.R.color.color9;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.TripViewHolder > {

    private Context mContext;
    private List<TripList> mTrip;
   private String uid;

    public MembersAdapter(Context mContext, String uid, List<TripList> mTrip, ProfileData listener) {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member, parent, false);
        return new TripViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {

        TripList tList = mTrip.get(position);
     /*   if(tList.getImageUrl().equalsIgnoreCase("default")||tList.getImageUrl().equalsIgnoreCase(""))
        {
            Glide.with(mContext).load(R.drawable.ic_broken_image_primary_24dp).into(holder.mImage);
        }
        else {*/
//        Log.i("TAG", "onBindViewHolder: "+tList.getImageUrl());
//            Glide.with(mContext).load(tList.getImageUrl()).placeholder(R.drawable.ic_broken_image_primary_24dp).centerCrop().into(holder.mImage);
//        }

        if(tList.getGender().equalsIgnoreCase("Female"))
        {
            if(tList.getAccount_type()==1)
            {
                Glide.with(mContext).asBitmap().load(tList.getImageUrl())
                        .centerCrop()
                        .override(450,600)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.no_photo_female);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.mImage.setImageBitmap(resource);
                            }
                        });
            }
            else {
                Glide.with(mContext).load(R.drawable.hidden_photo_female_thumb)
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.hidden_photo_female_thumb);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.mImage);
            }
        }
        else {
            if (tList.getAccount_type() == 1) {
                Glide.with(mContext).asBitmap().load(tList.getImageUrl())
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.no_photo_male);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.mImage.setImageBitmap(resource);
                            }
                        });
            }
            else {
                Glide.with(mContext).load(R.drawable.hidden_photo_male_thumb)
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.hidden_photo_male_thumb);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.mImage);
            }
        }

        holder.mTitle.setText(tList.getName()+" "+tList.getAge());

        holder.mCity.setText(tList.getUserLocation());

        if (position%2==0) holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color8));
        else if(position%3==0){
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color9));
        } else if(position%4==0)
        {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color6));
        }
        else if(position%5==0)
        {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color5));
        }
        else {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorbrowne4));

        }


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setData(tList,position);
                listener.setProfileVisit(uid,tList.getId());

               Log.i("Got Needed Value"," "+tList.getFavid());
            }
        });
    }


    ProfileData listener ;
    public interface ProfileData{
        void setData(TripList tList, int position);
        void setProfileVisit(String uid, String id);
    }

    @Override
    public int getItemCount() {
        Log.i("TAGtrip", "getItemCount: "+mTrip.size());
        return mTrip.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        ProgressBar progressBar;
        TextView mTitle, mCity;
        CardView mCardView;
        LinearLayout linearLayout;

        TripViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mCardView = itemView.findViewById(R.id.cardview);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            progressBar=itemView.findViewById(R.id.progressBar);
        }
    }
}