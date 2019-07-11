package com.example.tgapplication.photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
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

    public MyAdapter(Context context, String uid, List<Upload> uploads)
    {
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

        Glide
                .with(mcontext)
                .load(uploadCurrent.getimage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.imageView);


//        Log.i(TAG, "onBindViewHolder: " + uploadCurrent.getUrl());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                HashMap<String, Object> map = new HashMap<>();
                map.put("imageURL", ""+uploadCurrent.getimage());
                reference.updateChildren(map);

                holder.ivTitle.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_action_fav_remove));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView,ivTitle;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
         imageView = itemView.findViewById(R.id.imageView);
        ivTitle=itemView.findViewById(R.id.ivTitle);
    }
}

}