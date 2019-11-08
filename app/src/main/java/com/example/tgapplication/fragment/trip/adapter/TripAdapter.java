package com.example.tgapplication.fragment.trip.adapter;

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
import com.bumptech.glide.request.target.BitmapThumbnailImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;

import java.util.List;

import static android.graphics.Color.parseColor;
import static com.example.tgapplication.R.color.color8;
import static com.example.tgapplication.R.color.color9;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder > {

    private Context mContext;
    private List<TripList> mTrip;
   private String uid;

    public TripAdapter(Context mContext, String uid, List<TripList> mTrip,ProfileData listener) {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_users_trip, parent, false);
        return new TripViewHolder(mView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
        Log.i("TAGrecyclerview", "onBindViewHolder: "+tList.getAccount_type());


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


        holder.mTitle.setText(tList.getName());

        holder.mCity.setText(tList.getPlanLocation());
        holder.mDate.setText(tList.getFrom_to_date());

        if (position%2==0) holder.linearLayout.setBackgroundColor(color8);
        else if(position%3==0){
            holder.linearLayout.setBackgroundColor(color9);
        } else if(position%4==0)
        {
            holder.linearLayout.setBackgroundColor(R.color.color6);
        }
        else if(position%5==0)
        {
            holder.linearLayout.setBackgroundColor(R.color.color5);
        }
        else {
            holder.linearLayout.setBackgroundColor(R.color.colorbrowne4);

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
        return mTrip.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle, mCity, mDate;
        CardView mCardView;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        TripViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            progressBar=itemView.findViewById(R.id.progressBar);
        }
    }
}