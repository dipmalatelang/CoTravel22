package com.example.tgapplication.fragment.trip.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;

import java.util.List;

import static android.graphics.Color.parseColor;

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

    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
        Log.i("TAGrecyclerview", "onBindViewHolder: "+tList.getImageUrl());
            Glide.with(mContext).load(tList.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.mImage);

        holder.mTitle.setText(tList.getName());

        holder.mCity.setText(tList.getPlanLocation());
        holder.mDate.setText(tList.getFrom_to_date());

        if (position%3==0) {
            holder.linearLayout.setBackgroundColor(parseColor("#26A69A"));
        } else if(position%2==0){
            holder.linearLayout.setBackgroundColor(parseColor("#EC407A"));
        } else if(position%4==0)
        {
            holder.linearLayout.setBackgroundColor(parseColor("#8D6E63"));
        }
        else if(position%5==0)
        {
            holder.linearLayout.setBackgroundColor(parseColor("#b0003a"));
        }
        else {
            holder.linearLayout.setBackgroundColor(parseColor("#FFCA28"));

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

        TripViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}