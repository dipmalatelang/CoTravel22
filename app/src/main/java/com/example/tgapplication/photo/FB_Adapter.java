package com.example.tgapplication.photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.FacebookImage;
import com.google.firebase.storage.StorageReference;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;


public class FB_Adapter extends RecyclerView.Adapter<FB_Adapter.ImageViewHolder> {
    private Context mcontext;
    private List<FacebookImage.Images> mUploads;
    String TAG = "AdapterClass";
    String uid;
    private StorageReference storageReference;
    String previousValue="";


    public FB_Adapter(Context context, String uid, List<FacebookImage.Images> uploads, FbInterface fbInterface) {
        this.uid=uid;
        mcontext =context;
        mUploads =uploads;
        this.fbInterface=fbInterface;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.layout_album, parent,false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

//        holder.textViewName.setText(uploadCurrent.getName());
//          holder.below_opt.setVisibility(View.VISIBLE);
        Log.i(TAG, "onBindViewHolder: "+mUploads.get(position).getName()+" - "+mUploads.get(position).getImage_Url().size());
            holder.imageView.setAdjustViewBounds(true);
//          holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.imageView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

            holder.txt_title.setText(mUploads.get(position).getName());
            holder.txt_body.setText(String.valueOf(mUploads.get(position).getImage_Url().size()));

            holder.cl_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbInterface.proceed(mUploads.get(position).getImage_Url());
                }
            });

            if(mUploads.get(position).getImage_Url().size()>0)
            Glide.with(mcontext)
                    .load(mUploads.get(position).getImage_Url().get(0))
                    .placeholder(R.drawable.ic_broken_image_primary_24dp)
                    .into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,ivTitle;
        ConstraintLayout cl_image;
        TextView set_main, pp_eye, delete;
        EasyFlipView flipView;
        TextView txt_title, txt_body;
//        public LinearLayout below_opt;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

//            below_opt=itemView.findViewById(R.id.below_opt);
            imageView = itemView.findViewById(R.id.imageView);
            txt_title=itemView.findViewById(R.id.txt_title);
            txt_body=itemView.findViewById(R.id.txt_body);
            cl_image=itemView.findViewById(R.id.cl_image);
           /* ivTitle=itemView.findViewById(R.id.ivTitle);

            flipView=itemView.findViewById(R.id.flipView);
            set_main=itemView.findViewById(R.id.set_main);
            pp_eye=itemView.findViewById(R.id.pp_eye);
            delete=itemView.findViewById(R.id.delete);*/

        }
    }

    FbInterface fbInterface;

    public interface FbInterface{
        void proceed(ArrayList<String> image_url);
    }

}