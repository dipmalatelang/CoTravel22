package com.example.tgapplication.fragment.favourite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ProfileVisitorViewHolder >
{

    private Context mContext;
    private List<User> mTrip;
    private String uid;
    private int fav_int;
    private List<String> favArray;

    public FavouriteAdapter(Context mContext, String uid, List<User> mTrip,FavouriteInterface listener)
    {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.listener = listener;
//        this.favArray=favArray;
    }



    @Override
    public ProfileVisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favisit_item, parent, false);
        return new ProfileVisitorViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProfileVisitorViewHolder holder, int position)
    {

        final User tList = mTrip.get(position);
            Glide.with(mContext).load(R.drawable.ic_broken_image_primary_24dp).placeholder(R.drawable.ic_broken_image_primary_24dp).into(holder.mImage);

        holder.mTitle.setText(tList.getName());
//        holder.mCity.setVisibility(View.GONE);
        //holder.mDate.setVisibility(View.GONE);
        holder.mCity.setText(tList.getLocation());
////        holder.mDate.setText(tList.getFrom_to_date());

        /*holder.mCardView.setOnClickListener(new View.OnClickListener() {
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
        });*/

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setProfileVisit(uid,tList.getId());
//                int fav_id= getFav(favArray,tList.getId());
//                Log.i("Got Needed Value"," "+fav_id);
                listener.sendFavourite(tList.getId());
                listener.setData(tList,position);
//                Intent mIntent = new Intent(mContext, DetailActivity.class);
//                mIntent.putExtra("MyObj", tList);
////                mIntent.putExtra("FavId",fav_id);
//                mContext.startActivity(mIntent);
            }
        });

        holder.ivTitle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_fav_remove));

        holder.ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).removeFav(uid, mTrip.get(position).getId());
                mTrip.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrip.size();
    }

    class ProfileVisitorViewHolder extends RecyclerView.ViewHolder
    {

        ImageView mImage, ivTitle;
        TextView mTitle, mCity, mDate;
        CardView mCardView;

        ProfileVisitorViewHolder(View itemView)
        {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            ivTitle=itemView.findViewById(R.id.ivTitle);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
           // mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }

    FavouriteInterface listener;
    public interface FavouriteInterface{
        void sendFavourite(String id);
        void setProfileVisit(String uid, String id);
        void setData(User mTrip,int position);
    }
}