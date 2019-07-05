package com.example.tgapplication.trips;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProfileVisitorAdapter extends RecyclerView.Adapter<ProfileVisitorAdapter.ProfileVisitorViewHolder > {

    private Context mContext;
    private List<TripList> mTrip;
    private String uid;
    private int fav_int;
    private List<String> favArray;

    public ProfileVisitorAdapter(Context mContext, String uid, List<String> favArray,List<TripList> mTrip) {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.favArray=favArray;
    }


    @Override
    public ProfileVisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new ProfileVisitorViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProfileVisitorViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
        if(tList.getImageUrl().equalsIgnoreCase("default"))
        {
            Glide.with(mContext).load(R.drawable.ic_action_girl).into(holder.mImage);
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
                int fav_id= getFav(favArray,tList.getId());
                Log.i("Got Needed Value"," "+fav_id);
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("MyObj", tList);
                mIntent.putExtra("FavId",fav_id);
                mContext.startActivity(mIntent);
            }
        });
    }

    private int getFav(List<String> favArray, String id) {
        for(int i=0;i<favArray.size();i++)
        {
            if(favArray.get(i).equalsIgnoreCase(id))
            {
                fav_int=1;
                return fav_int;
            }
            else {
                fav_int=0;
            }
        }
        return fav_int;
    }

    private void setProfileVisit(String uid, String id) {

        final DatabaseReference visitedRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(id)
                .child(uid);
        visitedRef.child("id").setValue(uid);

    }

    @Override
    public int getItemCount() {
        return mTrip.size();
    }

    class ProfileVisitorViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle, mCity, mDate;
        CardView mCardView;

        ProfileVisitorViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}