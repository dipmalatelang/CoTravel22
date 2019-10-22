package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.photo.Upload;

import java.util.ArrayList;

public class CustomAdapter extends PagerAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<Upload> mUploads;
    String uid;

    public CustomAdapter(Context ctx, String uid, ArrayList<Upload> uploads) {
        this.ctx = ctx;
        this.mUploads= uploads;
        this.uid=uid;

    }

    @Override
    public int getCount() {
        return mUploads.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe,container,false);
        ImageView img = v.findViewById(R.id.imageView);
//        img.setImageResource(mUploads.get(position).url);
        Glide.with(ctx)
                .load(mUploads.get(position).getUrl())
                .fitCenter()
                .placeholder(R.drawable.ic_broken_image_primary_24dp)
                .into(img);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}