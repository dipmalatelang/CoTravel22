package com.example.tgapplication.fragment.member.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;

import java.util.List;

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


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member, parent, false);
        return new TripViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
     /*   if(tList.getImageUrl().equalsIgnoreCase("default")||tList.getImageUrl().equalsIgnoreCase(""))
        {
            Glide.with(mContext).load(R.drawable.ic_broken_image_primary_24dp).into(holder.mImage);
        }
        else {*/
        Log.i("TAG", "onBindViewHolder: "+tList.getImageUrl());
            Glide.with(mContext).load(tList.getImageUrl()).placeholder(R.drawable.ic_broken_image_primary_24dp).into(holder.mImage);
//        }

        holder.mTitle.setText(tList.getName()+" "+tList.getAge());

        holder.mCity.setText(tList.getUserLocation());

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
        TextView mTitle, mCity;
        CardView mCardView;

        TripViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}