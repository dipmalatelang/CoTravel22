package com.example.tgapplication.fragment.trip.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.AddTripActivity;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by user on 3/28/2018.
 */

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripHolder>{

    String uid;
    Context context;
    List<TripData> tripDataList;
//    TripData tripDetails;


    public TripListAdapter(Context context, String uid, List<TripData> tripDataList) {
        this.context = context;
        this.tripDataList = tripDataList;
        this.uid=uid;
    }


    @Override
    public int getItemCount() {
        Log.i("tripData",""+tripDataList.size());
        return tripDataList.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder{

        TextView tv_city,tv_date;
        Button btn_edit,btn_delete;
//        LinearLayout llSports;

        public TripHolder(View itemView) {
            super(itemView);
            tv_city= itemView.findViewById(R.id.tv_city);
            tv_date= itemView.findViewById(R.id.tv_date);
//            llSports= itemView.findViewById(R.id.llSports);
            btn_edit=itemView.findViewById(R.id.btn_edit);
            btn_delete=itemView.findViewById(R.id.btn_delete);

        }
    }

    @Override
    public void onBindViewHolder(final TripHolder holder, int position) {
        final TripData tripDetails=tripDataList.get(position);

        holder.tv_city.setText(tripDetails.getLocation());
        holder.tv_date.setText(tripDetails.getFrom_date()+" - "+tripDetails.getTo_date());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // remove your item from data base
//                Log.i("CheckId",tripDataList.get(position).getId());
                removeTrip(uid,tripDataList.get(position).getId());
                tripDataList.remove(position);  // remove the item from list
//                notifyDataSetChanged(); // notify the adapter about the removed item
                notifyItemRemoved(position);
                notifyDataSetChanged();
//                new AddTripActivity().displayTripList(uid);
            }
        });

//        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AddTripActivity().updateTrips(tripDetails);
//            }
//        });
        
    }

    private void removeTrip(String uid, String id) {
        final DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Trips")
                .child(uid);
        visitorRef.child(id).removeValue();
    }


    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_trips, parent, false);
        return new TripHolder(v);
    }
}
