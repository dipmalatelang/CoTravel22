package com.example.tgapplication.photo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.EditPhotoActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ImageViewHolder> {
    private Context mcontext;
    private List<Upload> mUploads;
    String TAG = "AdapterClass";
    String uid;
    private StorageReference storageReference;
    private DatabaseReference reference;

    public MyAdapter(Context context, String uid, List<Upload> uploads) {
        this.uid=uid;
        mcontext =context;
        mUploads =uploads;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.layout_images, parent,false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Upload uploadCurrent = mUploads.get(position);
//        holder.textViewName.setText(uploadCurrent.getName());

        if(position==0)
        {
            holder.below_opt.setVisibility(View.GONE);

            holder.imageView.setAdjustViewBounds(false);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
            holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Glide.with(mcontext)
                    .load(R.drawable.ic_gallery)
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.imageView);
        }
        else if(position==1)
        {
            holder.below_opt.setVisibility(View.GONE);

            holder.imageView.setAdjustViewBounds(false);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
            holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Glide.with(mcontext)
                    .load(R.drawable.ic_fb)
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.imageView);
        }
        else {
            holder.below_opt.setVisibility(View.VISIBLE);
            Glide.with(mcontext)
                    .load(uploadCurrent.getUrl())
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.imageView);

        }
//        Log.i(TAG, "onBindViewHolder: " + uploadCurrent.getUrl());

        holder.imageView.setOnClickListener(view -> {

            switch (position)
            {
                case 0:
                    Toast.makeText(mcontext, "Gallery", Toast.LENGTH_SHORT).show();
                    ((EditPhotoActivity)mcontext).showFileChooser();
                    break;
                case 1:
                    Toast.makeText(mcontext, "Facebook", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mcontext, "Uploaded Photo", Toast.LENGTH_SHORT).show();
                    break;
            }

           /* reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            HashMap<String, Object> map = new HashMap<>();
            map.put("imageURL", ""+uploadCurrent.getUrl());
            reference.updateChildren(map);

            holder.ivTitle.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_action_fav_remove));*/
        });

        holder.set_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext, "Set Main", Toast.LENGTH_SHORT).show();
            }
        });

        holder.pp_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext, "Public Private", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,ivTitle, set_main, pp_eye, delete;
        public LinearLayout below_opt;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            below_opt=itemView.findViewById(R.id.below_opt);
            imageView = itemView.findViewById(R.id.imageView);
            ivTitle=itemView.findViewById(R.id.ivTitle);

            set_main=itemView.findViewById(R.id.set_main);
            pp_eye=itemView.findViewById(R.id.pp_eye);
            delete=itemView.findViewById(R.id.delete);

        }
    }

}