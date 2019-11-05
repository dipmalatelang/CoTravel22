package com.example.tgapplication.fragment.favourite.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.parseColor;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ProfileVisitorViewHolder >
{

    private Context mContext;
    private List<UserImg> mTrip;
    private String uid;


    public FavouriteAdapter(Context mContext, String uid, List<UserImg> mTrip, FavouriteInterface listener) {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.listener = listener;
//        this.favArray=favArray;
    }



    @NonNull
    @Override
    public ProfileVisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favisit_item, parent, false);
        return new ProfileVisitorViewHolder(mView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProfileVisitorViewHolder holder, int position)
    {

        final User tList = mTrip.get(position).getUser();
        Glide.with(mContext).load(mTrip.get(position).getPictureUrl()).centerCrop().placeholder(R.drawable.ic_broken_image_primary_24dp).into(holder.mImage);

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
        if (position%3==0)
        {
            holder.linearLayout.setBackgroundColor(R.color.colorpurple1);
        } else if(position%2==0)
        {
            holder.linearLayout.setBackgroundColor(R.color.colorgreen2);
        } else if(position%4==0)
        {
            holder.linearLayout.setBackgroundColor(R.color.colorblue3);
        }

        else
            {
            holder.linearLayout.setBackgroundColor(R.color.colorbrowne4);
        }

        holder.ivTitle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_fav_remove));

        holder.ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).removeFav(uid, mTrip.get(position).getUser().getId());
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
        TextView mTitle, mCity;
        CardView mCardView;
        public ConstraintLayout cllist;
        LinearLayout linearLayout;


        ProfileVisitorViewHolder(View itemView)
        {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            ivTitle=itemView.findViewById(R.id.ivTitle);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
            // mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
            cllist =itemView.findViewById(R.id.cllist);
            linearLayout =itemView.findViewById(R.id.linearLayout);
        }
    }

    FavouriteInterface listener;
    public interface FavouriteInterface{
        void sendFavourite(String id);
        void setProfileVisit(String uid, String id);
        void setData(User mTrip,int position);
    }
}