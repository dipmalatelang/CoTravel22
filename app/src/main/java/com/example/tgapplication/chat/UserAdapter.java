package com.example.tgapplication.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;


import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UserImg> mUsers;
    private boolean ischat;




    public UserAdapter(Context mContext, List<UserImg> mUsers, boolean ischat, UserInterface listener){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
        this.listener=listener;
    }

    public static void remove(int position) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_user_chat, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUsers.get(position).getUser();
        Log.i("TAG", "onBindViewHolder: proper"+mUsers.get(position).getUser());
        holder.username.setText(user.getUsername());
    /*    if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {*/

        Glide.with(mContext).load(mUsers.get(position).getPictureUrl()).placeholder(R.drawable.ic_broken_image_primary_24dp).into(holder.profile_image);
//        }

      /*  holder.chat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();

                        if (id == R.id.one) {
                            Toast.makeText(mContext, "Add to fav", Toast.LENGTH_SHORT).show();
                            holder.ic_action_fav_remove.setVisibility(View.VISIBLE);
//                            listener.chatFavorite(user.getId());
                            return true;
                        }
                        if (id == R.id.two) {
                            Toast.makeText(mContext, "add to delete", Toast.LENGTH_LONG).show();
                            return true;
                        }


//                        Toast.makeText(mContext,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();//showing popup menu
                return false;
            }

        });*/

//      listener.highlightMsg(user.getId(),holder.last_msg);


        if (ischat){
            listener.lastMessage(mContext,user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }

        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
//                Toast.makeText(mContext, "u click one", Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });






        holder.tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUsers.remove(position);
                notifyDataSetChanged();
                makeText(mContext, "click on delete", LENGTH_SHORT).show();
            }
        });
        holder.tvfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fev.setVisibility(View.VISIBLE);
                makeText(mContext,"click on more",LENGTH_SHORT).show();

            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvfavourite.setVisibility(View.VISIBLE);
                holder.tvdelete.setVisibility(View.VISIBLE);
            }
        });




    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;

        private ImageView img_off;
        private TextView last_msg;
        RelativeLayout chat;
        ImageView ic_action_fav_remove;
        ImageView fev;
        TextView tvfavourite,tvdelete;


        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            chat =itemView.findViewById(R.id.chat);
            ic_action_fav_remove =itemView.findViewById(R.id.fev);
            tvdelete = itemView.findViewById(R.id.tvdelete);
            tvfavourite = itemView.findViewById(R.id.tvfavourite);
            fev =itemView.findViewById(R.id.fev);

        }
    }

    UserInterface listener;
    public interface UserInterface
    {
        void lastMessage(Context mContext, String userid, TextView last_msg);
//        void chatFavorite(String id );
    }


}
