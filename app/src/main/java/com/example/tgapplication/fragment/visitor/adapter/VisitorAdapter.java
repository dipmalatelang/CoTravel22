package com.example.tgapplication.fragment.visitor.adapter;

import android.content.Context;
import android.content.Intent;
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

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder >
{

    private Context mContext;
    private List<TripList> mTrip;
    private String uid;
    private int fav_int;
    private List<String> favArray;

    public VisitorAdapter(Context mContext, String uid, List<TripList> mTrip)
    {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
//        this.favArray=favArray;
    }


    @Override
    public VisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favisit_item, parent, false);
        return new VisitorViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final VisitorViewHolder holder, int position)
    {

        final TripList tList = mTrip.get(position);
        if(tList.getImageUrl().equalsIgnoreCase("default"))
        {
            Glide.with(mContext).load(R.drawable.ic_services_ratings_user_pic).into(holder.mImage);
        }
        else
        {
            Glide.with(mContext).load(tList.getImageUrl()).into(holder.mImage);
        }

        holder.mTitle.setText(tList.getName());

        holder.mCity.setText(tList.getPlanLocation());
        holder.mDate.setText(tList.getFrom_to_date());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileVisit(uid,tList.getId());
//                int fav_id= getFav(favArray,tList.getId());
//                Log.i("Got Needed Value"," "+fav_id);
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("MyObj", tList);
//                mIntent.putExtra("FavId",fav_id);
                mContext.startActivity(mIntent);
            }
        });


        holder.ivTitle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.delete));

        holder.ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailActivity().removeFav(uid, mTrip.get(position).getId());
                mTrip.get(position).setFavid(0);
                notifyDataSetChanged();
            }
        });
    }

    private void setProfileVisit(String uid, String id)
    {

        final DatabaseReference visitedRef = FirebaseDatabase.getInstance().getReference("Visitor")
                .child(id)
                .child(uid);
        visitedRef.child("id").setValue(uid);

    }

    @Override
    public int getItemCount() {
        return mTrip.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder
    {

        ImageView mImage, ivTitle;
        TextView mTitle, mCity, mDate;
        CardView mCardView;

        VisitorViewHolder(View itemView)
        {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            ivTitle=itemView.findViewById(R.id.ivTitle);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}