package com.example.tgapplication.fragment.visitor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.favourite.adapter.FavouriteAdapter;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.adapter.VisitorAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class VisitorFragment extends BaseFragment {


    private RecyclerView myVisitRV;
    private FirebaseUser fuser;
    View view;
    private List<User> myFavArray=new ArrayList<>();
    


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_visitor, container, false);

        myVisitRV = view.findViewById(R.id.myVisitRV);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        myVisitRV.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        visitList(fuser);
//
//        favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
//        favArray = (List<String>) getIntent().getSerializableExtra("ListFav");
      /*  Log.i("myFavArray",""+tripList.size());
        for(int i=0;i<tripList.size();i++)
        {
            if(tripList.get(i).getVisit_id()==1)
            {
                myFavArray.add(tripList.get(i));
            }}

        VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray);
        myVisitRV.setAdapter(tripAdapter);*/

        return view;
    }

    public void visitList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myFavArray.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {
//                                getFav(fuser.getUid(),user.getId());
                                // HERE WHAT CORRESPONDS TO JOIN
                                DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                                        .child(fuser.getUid());

                                visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        if (snapshot.hasChild(user.getId())) {
                                            // run some code
                                            myFavArray.add(user);
                                        }

                                        VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray);
                                        myVisitRV.setAdapter(tripAdapter);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

}
