package com.example.tgapplication.fragment.trip.adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new TripViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TripViewHolder holder, int position) {

        final TripList tList = mTrip.get(position);
            Glide.with(mContext).load(tList.getImageUrl())
                    .placeholder(R.drawable.ic_services_ratings_user_pic)
                    .into(holder.mImage);

        holder.mTitle.setText(tList.getName());

        holder.mCity.setText(tList.getPlanLocation());
        holder.mDate.setText(tList.getFrom_to_date());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setData(tList,position);
                setProfileVisit(uid,tList.getId());

               Log.i("Got Needed Value"," "+tList.getFavid());
            }
        });
    }


    ProfileData listener ;
    public interface ProfileData{
        void setData(TripList tList, int position);
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