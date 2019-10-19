package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripData;

import java.util.ArrayList;

public class PlanTripsAdapter extends RecyclerView.Adapter<PlanTripsAdapter.PlanTripsHolder> {

    Context context;
    ArrayList<TripData> planTripsList;

    public PlanTripsAdapter(Context context, ArrayList<TripData> planTripsList)
    {
        this.context=context;
        this.planTripsList=planTripsList;
    }
    @Override
    public void onBindViewHolder(@NonNull PlanTripsAdapter.PlanTripsHolder holder, int position) {
       TripData tripData =planTripsList.get(position);
        holder.txt_place.setText(tripData.getLocation());
        holder.txt_date.setText(tripData.getFrom_date()+" - "+tripData.getTo_date());
        Log.i("TAG", "onBindViewHolder: Trip Note"+tripData.getTrip_note());


    }

    @NonNull
    @Override
    public PlanTripsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plan_trips,parent,false);
        return new PlanTripsHolder(v);
    }

    @Override
    public int getItemCount() {
        return planTripsList.size();
    }

    public class PlanTripsHolder extends RecyclerView.ViewHolder{

        TextView txt_place, txt_date;
        public PlanTripsHolder(View view)
        {
            super(view);
            txt_place=view.findViewById(R.id.txt_place);
            txt_date=view.findViewById(R.id.txt_date);
        }
    }
}
