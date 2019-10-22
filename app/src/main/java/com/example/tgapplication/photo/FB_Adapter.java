package com.example.tgapplication.photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.EditPhotoActivity;
import com.example.tgapplication.fragment.account.profile.FacebookImage;
import com.google.firebase.storage.StorageReference;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;


public class FB_Adapter extends RecyclerView.Adapter<FB_Adapter.ImageViewHolder> {
    private Context mcontext;
    private List<FacebookImage.Images> mUploads;
    String TAG = "AdapterClass";
    String uid;
    private StorageReference storageReference;
    String previousValue="";


    public FB_Adapter(Context context, String uid, List<FacebookImage.Images> uploads) {
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

//        holder.textViewName.setText(uploadCurrent.getName());
//          holder.below_opt.setVisibility(View.VISIBLE);

            holder.imageView.setAdjustViewBounds(true);
//          holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            Glide.with(mcontext)
                    .load(mUploads.get(position).getImage_Url())
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.imageView);


    }



    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,ivTitle;
        TextView set_main, pp_eye, delete;
        EasyFlipView flipView;
//        public LinearLayout below_opt;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

//            below_opt=itemView.findViewById(R.id.below_opt);
            imageView = itemView.findViewById(R.id.imageView);
            ivTitle=itemView.findViewById(R.id.ivTitle);

            flipView=itemView.findViewById(R.id.flipView);
            set_main=itemView.findViewById(R.id.set_main);
            pp_eye=itemView.findViewById(R.id.pp_eye);
            delete=itemView.findViewById(R.id.delete);

        }
    }

}