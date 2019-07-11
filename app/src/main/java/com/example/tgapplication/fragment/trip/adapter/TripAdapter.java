package com.example.tgapplication.fragment.trip.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.tgapplication.fragment.trip.DetailActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder > {

    private Context mContext;
    private List<TripList> mTrip;
   private String uid;

    public TripAdapter(Context mContext, String uid, List<TripList> mTrip) {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
    }


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new TripViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
        if(tList.getImageUrl().equalsIgnoreCase("default"))
        {
            Glide.with(mContext).load(R.drawable.ic_services_ratings_user_pic).into(holder.mImage);
        }
        else {
            Glide.with(mContext).load(tList.getImageUrl()).into(holder.mImage);
        }

        holder.mTitle.setText(tList.getName());

        holder.mCity.setText(tList.getPlanLocation());
        holder.mDate.setText(tList.getFrom_to_date());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileVisit(uid,tList.getId());

               Log.i("Got Needed Value"," "+tList.getFavid());
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("MyObj", tList);
                mContext.startActivity(mIntent);
            }
        });
    }




    private void setProfileVisit(String uid, String id) {

        final DatabaseReference visitedRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(id)
                .child(uid);
        visitedRef.child("id").setValue(uid);

    }

    @Override
    public int getItemCount() {
        Log.i("Inside Adapter",""+mTrip.size());
        return mTrip.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle, mCity, mDate;
        CardView mCardView;

        TripViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}