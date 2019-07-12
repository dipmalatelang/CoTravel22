package com.example.tgapplication.fragment.trip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripData;

import java.util.List;

/**
 * Created by user on 3/28/2018.
 */

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripHolder>{

    Context context;
    List<TripData> tripDataList;
    TripData tripDetails;


    public TripListAdapter(Context context, List<TripData> tripDataList) {
        this.context = context;
        this.tripDataList = tripDataList;
    }


    @Override
    public int getItemCount() {
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
                TripData RemoveItem = tripDataList.get(position);
                // remove your item from data base
                tripDataList.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
            }
        });

//        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AddTripActivity().updateTrips(tripDetails);
//            }
//        });
        
    }



    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_trips, parent, false);
        return new TripHolder(v);
    }
}
