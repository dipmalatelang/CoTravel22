package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.visitor.UserImg;

import java.util.ArrayList;

public class ViewPhotoRequestAdapter extends RecyclerView.Adapter<ViewPhotoRequestAdapter.ViewPhotoRequestHolder> {

    Context context;
    int type;
    ArrayList<UserImg> userList;
    ViewPhotoRequestInterface viewPhotoRequestInterface;
    public ViewPhotoRequestAdapter(Context context, ArrayList<UserImg> userList, int type, ViewPhotoRequestInterface viewPhotoRequestInterface)
    {
        this.type=type;
        this.context=context;
        this.userList=userList;
        this.viewPhotoRequestInterface=viewPhotoRequestInterface;
    }

    @NonNull
    @Override
    public ViewPhotoRequestAdapter.ViewPhotoRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_request_accept_deny,parent,false);
        return new ViewPhotoRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPhotoRequestAdapter.ViewPhotoRequestHolder holder, int position) {
        String userInfo=userList.get(position).getUser().getName()+", "+userList.get(position).getUser().getAge();
        holder.tvName.setText(userInfo);
        Glide.with(context).load(userList.get(position).getPictureUrl()).centerCrop().placeholder(R.drawable.ic_broken_image_primary_24dp)
                .into(holder.ivImage);

        if(type==3)
        {
            holder.tvdesc.setText(R.string.can_see);
            holder.tv_hide.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_deny.setVisibility(View.GONE);
        }
       else if(type==2)
        {
            holder.tvdesc.setText(R.string.gave_access);
            holder.tv_hide.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_deny.setVisibility(View.GONE);
        }
        else {
            holder.tvdesc.setText(R.string.want_to_see_photo);
            holder.tv_hide.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_deny.setVisibility(View.VISIBLE);
        }

        holder.tv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhotoRequestInterface.hidePhotoRequest(userList.get(position).getUser().getId(),position);
            }
        });
        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhotoRequestInterface.acceptRequest(userList.get(position).getUser().getId(),position);
            }
        });

        holder.tv_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhotoRequestInterface.denyRequest(userList.get(position).getUser().getId(),position);
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.i("TAG", "getItemCount: "+userList.size());
        return userList.size();
    }

    public class ViewPhotoRequestHolder extends RecyclerView.ViewHolder {
        TextView tvName, tv_accept,tv_deny, tvdesc, tv_hide;
        ImageView ivImage;
        public ViewPhotoRequestHolder(@NonNull View itemView) {
            super(itemView);
            tv_hide=itemView.findViewById(R.id.tv_hide);
            tvdesc=itemView.findViewById(R.id.tvdesc);
            tvName=itemView.findViewById(R.id.tvName);
            ivImage=itemView.findViewById(R.id.ivImage);
            tv_accept=itemView.findViewById(R.id.tv_accept);
            tv_deny=itemView.findViewById(R.id.tv_deny);
        }
    }

    public interface ViewPhotoRequestInterface{
        void acceptRequest(String id, int pos);
        void denyRequest(String id, int pos);
        void hidePhotoRequest(String id, int pos);
    }
}
